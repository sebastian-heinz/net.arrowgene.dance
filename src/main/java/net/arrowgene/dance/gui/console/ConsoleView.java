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

package net.arrowgene.dance.gui.console;

import net.arrowgene.dance.editor.EditorConfig;
import net.arrowgene.dance.editor.EditorFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleView extends EditorFrame {

    private JPanel panel1;
    private JTextPane logPane;
    private JScrollPane logScroll;

    public ConsoleView(EditorConfig config) {
        super(config, "ConsoleView");
    }

    @Override
    public String getMenuCategory() {
        return "Tools";
    }

    @Override
    public void menuClicked() {
        this.setVisible(true);
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

        // this.panel1 = new JPanel();
        this.add(panel1);

        //  this.logPane = new JTextPane();
        //  this.logScroll = new JScrollPane();

        final OutputStream stream = new OutputStream() {
            public StringBuilder text = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                if ((char) b != '\n') {
                    text.append((char) b);
                } else {
                    logPane.setText(logPane.getText() + text.toString());
                    text = new StringBuilder();

                    int extent = logScroll.getVerticalScrollBar().getModel().getExtent();
                    logScroll.getVerticalScrollBar().setValue(logScroll.getVerticalScrollBar().getValue() + extent);
                }
            }
        };
        PrintStream outStream = new PrintStream(stream);
        System.setOut(outStream);

        super.init();
    }

}
