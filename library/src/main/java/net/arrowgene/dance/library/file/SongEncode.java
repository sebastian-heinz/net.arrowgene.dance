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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Read/Write SongEncode.dat
 */
public class SongEncode {

    public static final int KEY_LENGTH = 520;

    private int maxFileId;
    private List<Item> items;

    public SongEncode() {
        maxFileId = -1;
        items = new ArrayList<>();
    }

    public SongEncode(String songencodePath) throws Exception {
        this(Paths.get(songencodePath));
    }

    public SongEncode(Path songencodePath) throws Exception {
        this();
        if (!Files.isRegularFile(songencodePath)) {
            throw new Exception("Invalid songencodePath");
        }
        byte[] file = FileOp.readFile(songencodePath);
        ByteBuffer buffer = new ByteBuffer(file);
        maxFileId = buffer.getInt32();
        while (buffer.getCurrentPos() > buffer.getSize()) {
            int songId = buffer.getInt32();
            byte[] key = buffer.getBytes(KEY_LENGTH);
            Item item = new Item();
            item.setSongId(songId);
            item.setKey(key);
            items.add(item);
        }
    }

    public int getMaxFileId() {
        return maxFileId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void add(Item item) {
        items.add(item);
        updateMaxFileId();
    }

    public void remove(Item item) {
        items.remove(item);
        updateMaxFileId();
    }

    public void clear() {
        items.clear();
        updateMaxFileId();
    }

    public void save() {

    }

    private void updateMaxFileId() {
        int maxFileId = -1;
        for (Item item : items) {
            if (item.songId > maxFileId) {
                maxFileId = item.songId;
            }
        }
        this.maxFileId = maxFileId;
    }

    public static class Item {
        private int songId;
        private byte[] key;

        public int getSongId() {
            return songId;
        }

        public void setSongId(int songId) {
            this.songId = songId;
        }

        public byte[] getKey() {
            return key;
        }

        public void setKey(byte[] key) {
            this.key = key;
        }
    }

}
