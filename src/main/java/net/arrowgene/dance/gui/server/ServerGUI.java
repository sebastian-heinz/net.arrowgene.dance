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

package net.arrowgene.dance.gui.server;

import net.arrowgene.dance.editor.EditorConfig;
import net.arrowgene.dance.editor.EditorFrame;
import net.arrowgene.dance.server.DanceServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

public class ServerGUI extends EditorFrame implements ActionListener {

    private static final Logger logger = LogManager.getLogger(ServerGUI.class);

    private JPanel panel1;
    private JTextPane logPane;
    private JScrollPane logScroll;
    private JButton serverToggleButton;
    private JButton saveLogsButton;
    private JButton serverNewInstanceButton;
    private DanceServer server;
    private ArrayList<ServerCreatedListener> serverCreatedListener;

    public ServerGUI(EditorConfig config) {
        super(config, "Server");
        this.serverCreatedListener = new ArrayList<>();
    }

    public DanceServer getServer() {
        return server;
    }

    @Override
    public void menuClicked() {
        try {
            this.setMaximum(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

        this.pack();
        this.setMinimumSize(new Dimension(300, 200));
        this.setSize(new Dimension(300, 200));
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);

        //  this.panel1 = new JPanel();
        this.add(panel1);

        //  this.serverToggleButton = new JButton();
        this.serverToggleButton.setActionCommand("start");
        this.serverToggleButton.addActionListener(this);

        //  this.saveLogsButton = new JButton();
        this.saveLogsButton.setActionCommand("save-logs");
        this.saveLogsButton.addActionListener(this);

        //  this.serverNewInstanceButton = new JButton();
        this.serverNewInstanceButton.setActionCommand("new-instance");
        this.serverNewInstanceButton.addActionListener(this);

        super.init();
    }

    @Override
    public String getMenuCategory() {
        return "Server";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {
            this.start();
        } else if (e.getActionCommand().equals("stop")) {
            this.stop();
        } else if (e.getActionCommand().equals("new-instance")) {
            this.newInstance();
        }
    }


    // TODO display logs from log4j in window
    public void OnwriteLog() {
        // logPane.setText(logPane.getText() + log.toString() + "\n");
        // int extent = logScroll.getVerticalScrollBar().getModel().getExtent();
        // logScroll.getVerticalScrollBar().setValue(logScroll.getVerticalScrollBar().getValue() + extent);
    }

    public void addServerCreatedListener(ServerCreatedListener listener) {
        this.serverCreatedListener.add(listener);
    }

    private void newInstance() {
        if (this.server != null) {
            this.stop();
        }
        this.server = new DanceServer();
        for (ServerCreatedListener listener : this.serverCreatedListener) {
            listener.serverCreated(this.server);
        }
    }

    private void start() {
        if (this.server != null) {
            this.server.start();
            this.serverToggleButton.setActionCommand("stop");
            this.serverToggleButton.setText("Stop");
        } else {
            logger.error("No Server Instance");
        }
    }

    private void stop() {
        if (this.server != null) {
            this.server.stop();
            this.serverToggleButton.setActionCommand("start");
            this.serverToggleButton.setText("Start");
        } else {
            logger.error("No Server Instance");
        }
    }

}
