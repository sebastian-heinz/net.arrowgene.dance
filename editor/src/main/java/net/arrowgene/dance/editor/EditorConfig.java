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

import java.io.*;
import java.util.Properties;

/**
 * Configuration for the editor
 */
public class EditorConfig {

    private final static String CONFIG_FILE = "editor.properties";
    private String dancePath;

    public EditorConfig() {

    }

    public String getDancePath() {
        return dancePath;
    }

    public void setDancePath(String dancePath) {
        this.dancePath = dancePath;
    }

    /**
     * Loads the config from a file.
     */
    public void load() {
        File configFile = new File(CONFIG_FILE);
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            this.dancePath = props.getProperty("dance_path");

            reader.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }
    }

    /**
     * Saves the config to a file.
     */
    public void save() {
        File configFile = new File(CONFIG_FILE);
        try {
            Properties props = new Properties();

            props.setProperty("dance_path", this.dancePath);

            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "editor config");
            writer.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            // I/O error
        }
    }

}
