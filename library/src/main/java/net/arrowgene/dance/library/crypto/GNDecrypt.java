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

package net.arrowgene.dance.library.crypto;


import net.arrowgene.dance.library.models.stepfile.StepFileType;
import net.arrowgene.dance.library.common.Converter;
//import net.arrowgene.dance.database.Database;
//import net.arrowgene.dance.database.sqlite.SQLiteDb;
//import net.arrowgene.dance.log.Logger;
import net.arrowgene.dance.library.models.song.Song;


public class GNDecrypt {
    //private Database db;
    //private Song[] songs;

    //public static void main(String[] argv) {
    //    new GNDecrypt();
    //}

    public GNDecrypt() {
        //db = new SQLiteDb(new Logger());
        //songs = db.getSongs();

        //decryptTest("sdom0266K.gn");
        // encryptTest("de_sdom0266K.gn", "sdom0266K.gn");

        //byte[] data = readFile("sdom0266K.gn");
        //byte[] crc32 = Encryption.createGNChecksum(data);
        //System.out.println(crc32);
    }

    /*
        public void encryptTest(String fromFilename, String toFilename) {
            byte[] data = readFile(fromFilename);
            writeFile(toFilename, this.encryptGN(data, this.getSong(266), StepFileType.KEYBOARD, true));
        }

        public void decryptTest(String fileName) {
            byte[] data = readFile(fileName);

            byte[] dec = this.decryptGN(data, this.getSong(266), StepFileType.KEYBOARD);
            byte[] enc = this.encryptGN(dec, this.getSong(266), StepFileType.KEYBOARD, false);

            writeFile("de_" + fileName, dec);
            writeFile("en_" + fileName, enc);
        }
    */
/*
    public void decryptToDisk(String fileName, int songId, StepFileType stepFileType) {
        File file = new File(fileName);

        byte[] data = readFile(file.getPath());
        byte[] dec = this.decryptGN(data, this.getSong(songId), stepFileType);
        writeFile("de_" + file.getName(), dec);
    }

    public void encryptGN(String destinationFile, byte[] data, int songId, StepFileType stepFileType){//}, boolean updateInDb) {
        byte[] enc = this.encryptGN(data, this.getSong(songId), stepFileType);
        this.writeFile(destinationFile, enc);
    }
*/
    public byte[] encryptGN(byte[] data, Song song, StepFileType stepFileType) {
        byte[] encrypted = null;

        if (song != null && stepFileType != null) {
            if (stepFileType == StepFileType.KEYBOARD) {
                encrypted = SongEncryption.encryptData(Converter.getByteArray(song.getNoteK().getKey()), data);
                byte[] crc32 = SongEncryption.createGNChecksum(encrypted);
                song.getNoteK().setCrc32(Converter.getInt(crc32));
            } else {
                encrypted = SongEncryption.encryptData(Converter.getByteArray(song.getNoteT().getKey()), data);
                byte[] crc32 = SongEncryption.createGNChecksum(encrypted);
                song.getNoteT().setCrc32(Converter.getInt(crc32));
            }
/*
            if (updateInDb) {
                this.db.updateSong(song);
            }
            */
        }
        return encrypted;
    }

    /*
        public byte[] decryptGN(String fileName, int songId, StepFileType stepFileType) {
            byte[] data = readFile(fileName);
            return this.decryptGN(data, this.getSong(songId), stepFileType);
        }
    */
    public byte[] decryptGN(byte[] data, int key) {
        byte[] decrypted = null;
        //Check Keys
        if (key != 0) {
            decrypted = SongEncryption.decryptData(Converter.getByteArray(key), data);
        }
        return decrypted;
    }
}
