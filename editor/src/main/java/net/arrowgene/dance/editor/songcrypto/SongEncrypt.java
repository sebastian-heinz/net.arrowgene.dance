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

package net.arrowgene.dance.editor.songcrypto;

import net.arrowgene.dance.editor.EditorConfig;
import net.arrowgene.dance.editor.EditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

/**
 * Created by Daniel on 01.04.2017.
 */
public class SongEncrypt extends EditorFrame implements ActionListener {
    private JTextField txtFileName;
    private JButton chooseButton;
    private JTextField txtKey;
    private JTextArea txtPreData;
    private JButton btnStart;
    private JCheckBox checkEncrypt;
    private JPanel panel1;

    public SongEncrypt(EditorConfig config) {
        super(config, "Song Encrypt");
    }

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
        this.add(this.panel1);

        //  this.chooseButton = new JButton();
        this.chooseButton.setActionCommand("chooseFile");
        this.chooseButton.addActionListener(this);

        //  this.btnStart = new JButton();
        this.btnStart.setActionCommand("start");
        this.btnStart.addActionListener(this);

        super.init();
    }

    @Override
    public String getMenuCategory() {
        return "Tools";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("start")) {

        } else if (e.getActionCommand().equals("stop")) {
            //   this.serverToggleButton.setActionCommand("start");
            //   this.serverToggleButton.setText("Start");
        } else if (e.getActionCommand().equals("save-logs")) {
            //   this.server.getLogger().saveLogs();
        }
    }

}
