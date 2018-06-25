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

package net.arrowgene.dance.library.common;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class FileOp {

    public static byte[] readFile(String fileName) {
        return FileOp.readFile(Paths.get(fileName));
    }

    public static byte[] readFile(Path path) {
        byte[] fileData = null;
        try {
            fileData = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileData;
    }

    public static boolean writeFile(String path, String fileName, byte[] data) {
        boolean success = true;
        OutputStream stream = null;
        try {
            File file = new File(path, fileName);
            String filePath = file.getAbsolutePath();
            stream = new BufferedOutputStream(new FileOutputStream(filePath));
            stream.write(data);
        } catch (IOException ex) {
            success = false;
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex) {
                success = false;
            }
        }
        return success;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static File chooseDirectory(Component parent, String title) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(title);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    public static File chooseFile(String title, String[] allowedExtensions) {
        return FileOp.chooseFile(title, allowedExtensions, "");
    }

    public static File chooseFile(String title, String[] allowedExtensions, Component parent) {
        return FileOp.chooseFile(title, allowedExtensions, "", parent);
    }

    public static File chooseFile(String title, String[] allowedExtensions, String currentDirectory) {
        return FileOp.chooseFile(title, allowedExtensions, currentDirectory, null);
    }

    public static File chooseFile(String title, String[] allowedExtensions, String currentDirectory, Component parent) {
        File file = null;
        if (currentDirectory != null && currentDirectory.isEmpty()) {
            file = (new File(currentDirectory));
        }
        return FileOp.chooseFile(title, allowedExtensions, file, parent);
    }

    public static File chooseFile(String title, String[] allowedExtensions, File currentDirectory, Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(title);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (currentDirectory != null) {
            fc.setCurrentDirectory(currentDirectory);
        }

        if (allowedExtensions != null && allowedExtensions.length > 0) {

            final List<String> extensions = Arrays.asList(allowedExtensions);
            String desc = "";
            for (String ext : extensions) {
                desc += "." + ext + " ";
            }
            final String description = desc;

            fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    String extension = FileOp.getExtension(f);
                    return (extension != null && extensions.contains(extension));
                }

                @Override
                public String getDescription() {
                    return description;
                }
            });
        }

        int returnVal = fc.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

}
