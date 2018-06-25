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

package net.arrowgene.dance.library.models.song;


import net.arrowgene.dance.library.common.Converter;
import net.arrowgene.dance.library.crypto.SongEncryption;

import java.nio.ByteBuffer;


public class Song {

    private int fileId;
    private int songId = -1;
    private String key;
    private String name;
    private SongNote noteT;
    private SongNote noteK;

    public Song() {

    }



    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Song: "+getName()+ "("+getSongId()+") ("+noteK+") ("+noteT+")");

        return sb.toString();
    }
    public byte[] getDancepadData() {
        byte[] Key = new byte[]{(byte) 0xcf, (byte) 0x95, (byte) 0xa1, 0x00};

        ByteBuffer buffer = ByteBuffer.allocate(0x54);

        buffer.put(new byte[]{'d', 'd', 'r', 'm'});
        buffer.put(Converter.getByteArray(1));
        buffer.put(Converter.getByteArray(this.getNoteT().getSize() + 84));
        buffer.put(Key);
        // 16 Byte

        // NetworkEncryption
        ByteBuffer toEncryptBuffer = ByteBuffer.allocate(52);
        toEncryptBuffer.put(new byte[]{0, 0, 0, 0});
        toEncryptBuffer.put(Converter.getByteArray(this.getNoteT().getKey()));
        toEncryptBuffer.put(Converter.getByteArray(this.getNoteT().getCrc32()));
        toEncryptBuffer.put(new byte[]{0, 0, 0, 0});
        toEncryptBuffer.put(new byte[]{(byte) 0xda, (byte) 0xd8, (byte) 0x7c, (byte) 0x5d, (byte) 0x51, (byte) 0xa3, (byte) 0xab, (byte) 0xe8});
        toEncryptBuffer.put(new byte[]{(byte) 0x69, (byte) 0xe2, (byte) 0xaf, (byte) 0xa0, (byte) 0x32, (byte) 0x8b, (byte) 0xc8, (byte) 0xf1});
        toEncryptBuffer.put(new byte[]{(byte) 0x20, (byte) 0xe3, (byte) 0x7b, (byte) 0x45, (byte) 0xc9, (byte) 0x6b, (byte) 0xb2, (byte) 0xed});
        toEncryptBuffer.put(new byte[]{(byte) 0x6e, (byte) 0x9d, (byte) 0x77, (byte) 0x13, (byte) 0xda, (byte) 0xb5, (byte) 0xfc, (byte) 0xb2});
        toEncryptBuffer.put(new byte[]{(byte) 0x6a, (byte) 0xa8, (byte) 0xf6, (byte) 0xb7});

        byte[] encryptedData = SongEncryption.encryptData(Key, toEncryptBuffer.array());
        buffer.put(SongEncryption.calculateChecksum(encryptedData)); //20
        buffer.put(encryptedData);

        return buffer.array();
    }

    public byte[] getKeyboardData() {
        byte[] Key = new byte[]{(byte) 0xcf, (byte) 0x95, (byte) 0xa1, 0x00};

        ByteBuffer buffer = ByteBuffer.allocate(0x54);

        buffer.put(new byte[]{'d', 'd', 'r', 'm'});
        buffer.put(Converter.getByteArray(1));
        buffer.put(Converter.getByteArray(this.getNoteK().getSize() + 84));
        buffer.put(Key);

        // NetworkEncryption
        ByteBuffer toEncryptBuffer = ByteBuffer.allocate(52);
        toEncryptBuffer.put(new byte[]{0, 0, 0, 0});
        toEncryptBuffer.put(Converter.getByteArray(this.getNoteK().getKey()));
        toEncryptBuffer.put(Converter.getByteArray(this.getNoteK().getCrc32()));
        toEncryptBuffer.put(new byte[]{0, 0, 0, 0});
        toEncryptBuffer.put(new byte[]{(byte) 0xda, (byte) 0xd8, (byte) 0x7c, (byte) 0x5d, (byte) 0x51, (byte) 0xa3, (byte) 0xab, (byte) 0xe8});
        toEncryptBuffer.put(new byte[]{(byte) 0x69, (byte) 0xe2, (byte) 0xaf, (byte) 0xa0, (byte) 0x32, (byte) 0x8b, (byte) 0xc8, (byte) 0xf1});
        toEncryptBuffer.put(new byte[]{(byte) 0x20, (byte) 0xe3, (byte) 0x7b, (byte) 0x45, (byte) 0xc9, (byte) 0x6b, (byte) 0xb2, (byte) 0xed});
        toEncryptBuffer.put(new byte[]{(byte) 0x6e, (byte) 0x9d, (byte) 0x77, (byte) 0x13, (byte) 0xda, (byte) 0xb5, (byte) 0xfc, (byte) 0xb2});
        toEncryptBuffer.put(new byte[]{(byte) 0x6a, (byte) 0xa8, (byte) 0xf6, (byte) 0xb7});

        byte[] encryptedData = SongEncryption.encryptData(Key, toEncryptBuffer.array());
        buffer.put(SongEncryption.calculateChecksum(encryptedData));
        buffer.putInt(0x8baa06dc);
        buffer.putInt(0xb38a65d5);
        buffer.putInt(0xec7a02a6);
        buffer.put(encryptedData);

        return buffer.array();
    }

    public byte[] getDESKey() {
        byte[] data = null;
        if (!this.key.isEmpty()) {
            data = Converter.getByteArray(this.key.substring(0, 16));
        }

        return data;
    }

    public void setDESKey(String data) {
        if (this.key.isEmpty()) {
            this.key = "0000000000000000";
            for (int i = 0; i < 512; i++) {
                this.key += "00";
            }
        }
        this.key = data + this.key.substring(16);
    }

    public void setPreData(String data) {
        if (this.key.isEmpty()) {
            this.key = "0000000000000000";
            for (int i = 0; i < 512; i++) {
                this.key += "00";
            }
        }
        this.key = this.key.substring(0, 16) + data;
    }

    public byte[] getPreData() {
        byte[] data = null;
        if (!this.key.isEmpty()) {
            data = Converter.getByteArray(this.key.substring(16));
        }

        return data;
    }

    public SongNote getNoteK() {
        return noteK;
    }

    public void setNoteK(SongNote noteK) {
        this.noteK = noteK;
    }

    public SongNote getNoteT() {
        return noteT;
    }

    public void setNoteT(SongNote noteT) {
        this.noteT = noteT;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setKey(byte[] key) {
        this.key = Converter.getHEXString(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
