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

package net.arrowgene.dance.editor;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Provides general functions for adding a window to the GUI.
 */
public abstract class EditorFrame extends JInternalFrame {

    private String title;
    private boolean initialized;
    protected EditorConfig config;

    /**
     * Initializes a new Editor Frame.
     * The Title needs to be unique.
     *
     * @param title
     */
    public EditorFrame(EditorConfig config, String title) {
        this.config = config;
        this.title = title;
        this.setTitle(this.title);
        this.initialized = false;
    }

    /**
     * Called only when the menu is clicked.
     * Note: Some windows may become visible programmatically without clicking the menu.
     */
    public abstract void menuClicked();

    /**
     * Called only the first time before the EditorFrame is displayed.
     * <p>
     * If the method is overridden don't forget to call super,
     * otherwise init will execute whenever the menu is clicked or the EditorFrame is displayed.
     */
    public void init() {
        this.initialized = true;
    }

    /**
     * The name of the Menu Item,
     * Items with the same name will be grouped together.
     *
     * @return
     */
    public abstract String getMenuCategory();

    /**
     * An unique identifier, the title is used here.
     * You can not have two EditorFrames with the same title.
     *
     * @return
     */
    public String getActionCommand() {
        return this.title;
    }

    /**
     * Determines wether the init() method should be called or not.
     *
     * @return
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Provides the menu entry.
     *
     * @param listener
     * @return
     */
    public JMenuItem getMenuItem(ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(this.title);
        menuItem.setActionCommand(this.getActionCommand());
        menuItem.addActionListener(listener);
        return menuItem;
    }

}
