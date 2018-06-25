/*
 * This file is part of net.arrowgene.dance.
 *
 * net.arrowgene.dance is a server implementation for the game "Dance! Online".
 * Copyright (C) 2013-2018  Sebastian Heinz (github: sebastian-heinz)
 * Copyright (C) 2013-2018  Daniel Neuendorf
 *
 * Github: https://github.com/Arrowgene/net.arrowgene.dance
 * Web: https://arrowgene.net
 *
 * net.arrowgene.dance is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * net.arrowgene.dance is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.arrowgene.dance.gui;

import net.arrowgene.dance.database.Database;
import net.arrowgene.dance.editor.EditorConfig;
import net.arrowgene.dance.editor.EditorFrame;
import net.arrowgene.dance.editor.itemlist.ItemListEditor;
import net.arrowgene.dance.editor.songcrypto.SongEncrypt;
import net.arrowgene.dance.editor.stepfile.StepFileEditor;
import net.arrowgene.dance.gui.config.ConfigView;
import net.arrowgene.dance.gui.console.ConsoleView;
import net.arrowgene.dance.gui.injector.ServerPacketInjector;
import net.arrowgene.dance.gui.server.ServerGUI;
import net.arrowgene.dance.library.common.FileOp;
import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.library.file.SongFiles;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class frmMain extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JDesktopPane desktopPane;
    private JMenuBar menuBar;
    private HashMap<String, EditorFrame> frames;
    private HashMap<String, JMenu> menus;
    private EditorConfig config;
    private ServerGUI serverGui;

    public frmMain() {

        this.frames = new HashMap<>();
        this.menus = new HashMap<>();
        this.config = new EditorConfig();
        this.config.load();

        // this.mainPanel = new JPanel();
        this.add(this.mainPanel);

        this.menuBar = new JMenuBar();
        this.setJMenuBar(this.menuBar);

        // this.desktopPane = new JDesktopPane();

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            // handle exception
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) (screenSize.getWidth() / 2) - 300, (int) (screenSize.getHeight() / 2) - 200, 1024, 800);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.initViews();
        this.setVisible(true);
        this.arrangeStartup();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        EditorFrame editorFrame = this.frames.get(e.getActionCommand());
        if (editorFrame != null) {
            this.showFrame(editorFrame);
            editorFrame.menuClicked();

        } else if (e.getActionCommand().equals("songImport")) {
            songImport();
        }
    }

    /**
     * Imports songs into DB
     */
    private void songImport() {
        final File f = FileOp.chooseDirectory(this, "Select game folder");
        if (f != null) {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    SongFiles songFiles = null;
                    try {
                        songFiles = new SongFiles(f.toPath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (songFiles != null) {
                        List<Song> songs = songFiles.getSongs();
                        Database db = serverGui.getServer().getDatabase();
                        db.insertSongs(songs);
                        System.out.println("Import done");
                    } else {
                        System.out.println("Import failed");
                    }
                }
            });
            th.start();
        }
    }

    private void initViews() {

        ServerPacketInjector injector = new ServerPacketInjector(this.config);

        serverGui = new ServerGUI(this.config);
        serverGui.addServerCreatedListener(injector);

        this.loadFrame(new ConsoleView(this.config));
        this.loadFrame(serverGui);
        this.loadFrame(injector);
        this.loadFrame(new StepFileEditor(this.config));
        this.loadFrame(new SongEncrypt(this.config));
        this.loadFrame(new ItemListEditor(this.config));
        this.loadFrame(new ConfigView(this.config));
        this.loadDevelop();
    }

    /**
     * Load developer menu
     * This menu is for testing or non gui commands.
     * <p>
     * note: These menu entries need to be added manually.
     */
    private void loadDevelop() {
        JMenu menuDevelop = new JMenu("Develop");

        JMenuItem menuEntry = new JMenuItem("Import Songs");
        menuEntry.setActionCommand("songImport");
        menuEntry.addActionListener(this);
        menuDevelop.add(menuEntry);

        this.menuBar.add(menuDevelop);
    }

    /**
     * Loads an EditorFrame, and creates menu entries if necessary.
     *
     * @param editorFrame
     */
    private void loadFrame(EditorFrame editorFrame) {
        if (this.frames.containsKey(editorFrame.getActionCommand())) {
            throw new KeyAlreadyExistsException(editorFrame.getActionCommand()
                + " <-- a frame with this command was already added, please choose another identifier");
        } else {
            JMenu menu = this.menus.get(editorFrame.getMenuCategory());
            if (menu == null) {
                menu = new JMenu(editorFrame.getMenuCategory());
                this.menus.put(editorFrame.getMenuCategory(), menu);
                this.menuBar.add(menu);
            }
            menu.add(editorFrame.getMenuItem(this));
            this.frames.put(editorFrame.getActionCommand(), editorFrame);
        }
    }

    /**
     * Provide a default screen arrangement
     */
    private void arrangeStartup() {

        EditorFrame consoleView = null;
        EditorFrame serverGUI = null;
        EditorFrame injector = null;

        for (EditorFrame frame : this.frames.values()) {
            if (frame instanceof ConsoleView) {
                consoleView = frame;
            }
            if (frame instanceof ServerGUI) {
                serverGUI = frame;
            }
            if (frame instanceof ServerPacketInjector) {
                injector = frame;
            }
        }

        if (consoleView != null && serverGUI != null && injector != null) {

            this.showFrame(serverGUI);
            this.showFrame(consoleView);
            this.showFrame(injector);

            Dimension desktopSize = this.desktopPane.getSize();

            Dimension consoleFrameSize = consoleView.getSize();
            consoleView.setLocation(0, desktopSize.height - consoleFrameSize.height);
            consoleView.setSize(desktopSize.width, consoleFrameSize.height);

            serverGUI.setLocation(0, 0);
            serverGUI.setSize(desktopSize.width / 2, desktopSize.height - consoleFrameSize.height);

            injector.setLocation(desktopSize.width / 2, 0);
            injector.setSize(desktopSize.width / 2, desktopSize.height - consoleFrameSize.height);
        }
    }

    /**
     * Display a frame and bring it to the front.
     *
     * @param frame
     */
    private void showFrame(EditorFrame frame) {
        boolean onPane = false;
        for (JInternalFrame jFrame : this.desktopPane.getAllFrames()) {
            if (jFrame == frame) {
                onPane = true;
                break;
            }
        }
        if (!frame.isInitialized()) {
            frame.init();
        }
        if (!onPane) {
            this.desktopPane.add(frame);
        }
        this.desktopPane.moveToFront(frame);
        frame.setVisible(true);
    }

}
