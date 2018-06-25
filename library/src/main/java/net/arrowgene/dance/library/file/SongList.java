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
 * Read/Write SongList.dat
 */
public class SongList {

    public static final int ITEM_LENGTH = 752;

    private List<Item> items;

    public SongList() {
        items = new ArrayList<>();
    }

    public SongList(String songlistPath) throws Exception {
        this(Paths.get(songlistPath));
    }

    public SongList(Path songlistPath) throws Exception {
        this();
        if (!Files.isRegularFile(songlistPath)) {
            throw new Exception("Invalid songlistPath");
        }
        byte[] file = FileOp.readFile(songlistPath);
        ByteBuffer buffer = new ByteBuffer(file);
        while (buffer.getCurrentPos() > buffer.getSize()) {
            short unknown = buffer.getInt16();
            String sdomkGn = buffer.getStringFixed(32);
            String dps = buffer.getStringFixed(32);
            String png_1 = buffer.getStringFixed(32);
            String png_2 = buffer.getStringFixed(32);
            String png_3 = buffer.getStringFixed(32);
            String ogg = buffer.getStringFixed(32);
            byte[] unknown1 = buffer.getBytes(279);
            int fileId = buffer.getInt32();
            int experFileId = buffer.getInt32();
            byte[] unknown2 = buffer.getBytes(16);
            short difficultEasy = buffer.getInt16();
            short difficultNormal = buffer.getInt16();
            short difficultHard = buffer.getInt16();
            byte[] unknown3 = buffer.getBytes(82);
            String song = buffer.getStringFixed(64);
            String artist = buffer.getStringFixed(32);
            String producer = buffer.getStringFixed(32);
            String sdomkGm = buffer.getStringFixed(32);
            byte[] unknown4 = buffer.getBytes(30);
            Item item = new Item();
            item.setSdomkGn(sdomkGn);
            item.setDps(dps);
            item.setPng_1(png_1);
            item.setPng_2(png_2);
            item.setPng_3(png_3);
            item.setOgg(ogg);
            item.setFileId(fileId);
            item.setExperFileId(experFileId);
            item.setDifficultEasy(difficultEasy);
            item.setDifficultNormal(difficultNormal);
            item.setDifficultHard(difficultHard);
            item.setSong(song);
            item.setArtist(artist);
            item.setProducer(producer);
            item.setSdomkGm(sdomkGm);
            items.add(item);
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public void add(Item item) {
        items.add(item);
    }

    public void remove(Item item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public void save() {

    }

    public static class Item {
        private String sdomkGn;
        private String dps;
        private String png_1;
        private String png_2;
        private String png_3;
        private String ogg;
        private int fileId;
        private int experFileId;
        private short difficultEasy;
        private short difficultNormal;
        private short difficultHard;
        private String song;
        private String artist;
        private String producer;
        private String sdomkGm;

        public String getSdomkGn() {
            return sdomkGn;
        }

        public void setSdomkGn(String sdomkGn) {
            this.sdomkGn = sdomkGn;
        }

        public String getDps() {
            return dps;
        }

        public void setDps(String dps) {
            this.dps = dps;
        }

        public String getPng_1() {
            return png_1;
        }

        public void setPng_1(String png_1) {
            this.png_1 = png_1;
        }

        public String getPng_2() {
            return png_2;
        }

        public void setPng_2(String png_2) {
            this.png_2 = png_2;
        }

        public String getPng_3() {
            return png_3;
        }

        public void setPng_3(String png_3) {
            this.png_3 = png_3;
        }

        public String getOgg() {
            return ogg;
        }

        public void setOgg(String ogg) {
            this.ogg = ogg;
        }

        public int getFileId() {
            return fileId;
        }

        public void setFileId(int fileId) {
            this.fileId = fileId;
        }

        public int getExperFileId() {
            return experFileId;
        }

        public void setExperFileId(int experFileId) {
            this.experFileId = experFileId;
        }

        public short getDifficultEasy() {
            return difficultEasy;
        }

        public void setDifficultEasy(short difficultEasy) {
            this.difficultEasy = difficultEasy;
        }

        public short getDifficultNormal() {
            return difficultNormal;
        }

        public void setDifficultNormal(short difficultNormal) {
            this.difficultNormal = difficultNormal;
        }

        public short getDifficultHard() {
            return difficultHard;
        }

        public void setDifficultHard(short difficultHard) {
            this.difficultHard = difficultHard;
        }

        public String getSong() {
            return song;
        }

        public void setSong(String song) {
            this.song = song;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getProducer() {
            return producer;
        }

        public void setProducer(String producer) {
            this.producer = producer;
        }

        public String getSdomkGm() {
            return sdomkGm;
        }

        public void setSdomkGm(String sdomkGm) {
            this.sdomkGm = sdomkGm;
        }
    }

}
