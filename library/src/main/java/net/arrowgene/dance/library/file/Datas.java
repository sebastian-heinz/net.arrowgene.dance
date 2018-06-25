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

package net.arrowgene.dance.library.file;

import net.arrowgene.dance.library.common.ByteBuffer;
import net.arrowgene.dance.library.common.FileOp;
import net.arrowgene.dance.library.crypto.SongEncryption;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Read/Write Datas.sac and Datas.sai
 */
public class Datas {

    private static final byte[] password = {0x61, (byte) 0xf9, 0x53, 0x7c};
    private List<Item> items;
    private Path saiPath;
    private Path sacPath;
    private RandomAccessFile sacFile;

    public Datas() {
        items = new ArrayList<>();
    }

    public Datas(String saiPath, String sacPath) throws Exception {
        this(Paths.get(saiPath), Paths.get(sacPath));
    }

    public Datas(Path saiPath, Path sacPath) throws Exception {
        this();
        this.saiPath = saiPath;
        this.sacPath = sacPath;
        if (!Files.isRegularFile(this.saiPath)) {
            throw new Exception("Invalid saiPath");
        }
        if (!Files.isRegularFile(this.sacPath)) {
            throw new Exception("Invalid sacPath");
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                close();
            }
        });
        this.sacFile = new RandomAccessFile(this.sacPath.toFile(), "r");
        loadIndexFile();
    }

    public void extract(String path) {
        int count = 0;
        int lastPercent = 0;
        for (Item item : items) {
            File objFile = new File(path + item.getPath());
            if (objFile.isDirectory() || objFile.mkdirs()) {
                count++;
                byte[] data = getFile(item);
                FileOp.writeFile(path + item.getPath(), item.getFilename(), data);
                if ((count * 100 / items.size()) != lastPercent) {
                    lastPercent = count * 100 / items.size();
                    System.out.println(lastPercent + "%");
                }
            } else {
                System.out.println("Error creating directories");
                break;
            }
        }
    }

    public byte[] getFile(String filename) {
        for (Item item : items) {
            if (item.getFilename().equals(filename)) {
                return getFile(item);
            }
        }

        return null;
    }

    public byte[] getFile(Item item) {
        byte[] data = null;
        try {
            sacFile.seek(item.getOffset());
            data = new byte[item.getSize()];
            sacFile.read(data, 0, item.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void close() {
        if (sacFile != null) {
            try {
                sacFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadIndexFile() {
        byte[] saiFile = FileOp.readFile(saiPath);
        if (saiFile != null) {
            ByteBuffer saiBuffer = new ByteBuffer(saiFile);
            saiBuffer.getInt32();
            int entrySize = saiBuffer.getInt32() * 16;
            int fileNameSize = saiBuffer.getInt32();
            saiBuffer.getInt32();
            byte[] dataAttribs = saiBuffer.getBytes(entrySize);
            dataAttribs = SongEncryption.decryptData(password, dataAttribs);
            byte[] dataFileNames = saiBuffer.getBytes(fileNameSize);
            dataFileNames = SongEncryption.decryptData(password, dataFileNames);
            parseFiles(dataFileNames, dataAttribs);
        }
    }

    private void parseFiles(byte[] fileName, byte[] attribs) {
        String tmp = "";
        ByteBuffer attributBuffer = new ByteBuffer(attribs);
        for (int a = 0; a < attribs.length / 16; a++) {
            Item item = new Item();
            item.setId(attributBuffer.getInt32());
            item.setSize(attributBuffer.getInt32());
            item.setOffset(attributBuffer.getInt32());
            item.setFilenameOffset(attributBuffer.getInt32());
            for (int i = item.getFilenameOffset(); i < fileName.length; i++) {
                if (fileName[i] != 0)
                    tmp += (char) fileName[i];
                else {
                    // Path und Filename trennen
                    int lastSlash = tmp.lastIndexOf("\\");
                    if (lastSlash >= 0) {
                        item.setPath(tmp.substring(0, lastSlash + 1));
                        item.setFilename(tmp.substring(lastSlash + 1));
                    } else {
                        item.setPath("");
                        item.setFilename(tmp);
                    }
                    tmp = "";
                    break;
                }
            }
            items.add(item);
        }
    }

    public static class Item {
        private String filename;
        private String path;
        private int size;
        private int offset;
        private int id;
        private int filenameOffset;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFilenameOffset() {
            return filenameOffset;
        }

        public void setFilenameOffset(int filenameOffset) {
            this.filenameOffset = filenameOffset;
        }
    }
}
