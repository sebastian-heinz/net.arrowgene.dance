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

package net.arrowgene.dance.gui.config;

import net.arrowgene.dance.editor.EditorConfig;
import net.arrowgene.dance.editor.EditorFrame;
import net.arrowgene.dance.library.common.FileOp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ConfigView extends EditorFrame implements ActionListener {

    private JPanel mainPanel;
    private JButton buttonDancePath;
    private JLabel labelDancePath;
    private JButton buttonSave;

    public ConfigView(EditorConfig config) {
        super(config, "Configuration");
    }

    @Override
    public void init() {

        this.pack();
        this.setMinimumSize(new Dimension(600, 400));
        this.setSize(new Dimension(600, 400));
        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setLocation(0, 0);

        //  this.mainPanel = new JPanel();
        this.add(this.mainPanel);

        //  this.buttonDancePath = new JButton();
        this.buttonDancePath.setActionCommand("dance-path");
        this.buttonDancePath.addActionListener(this);

        //  this.buttonSave = new JButton();
        this.buttonSave.setActionCommand("save");
        this.buttonSave.addActionListener(this);

        this.load();

        super.init();
    }

    @Override
    public void menuClicked() {

    }

    @Override
    public String getMenuCategory() {
        return "Tools";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("dance-path")) {
            this.chooseDancePath();
        } else if (e.getActionCommand().equals("save")) {
            this.save();
        }
    }

    private void chooseDancePath() {
        File f = FileOp.chooseDirectory(this, "Dance Folder");
        if (f != null) {
            this.labelDancePath.setText(f.getPath());
            this.config.setDancePath(f.getPath());
        }
    }

    private void save() {
        this.config.save();
    }

    private void load() {
        this.labelDancePath.setText(this.config.getDancePath());
    }

}
