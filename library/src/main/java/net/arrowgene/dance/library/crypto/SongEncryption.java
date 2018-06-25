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


import net.arrowgene.dance.library.common.Converter;

import java.util.Arrays;
import java.util.zip.CRC32;

public class SongEncryption {

    private static int[] pTable = new int[256];

    public static byte[] calculateChecksum(byte[] crypted) {
        byte[] data = new byte[0x20];
        for (int i = 0; i < data.length; i++) {
            data[i] = crypted[i];
        }
        CRC32 crc32 = new CRC32();
        crc32.update(data);

        return Converter.getByteArray((int) crc32.getValue());
    }

    public static byte[] createGNChecksum(byte[] data) {
        createBaseValues();

        int CRC = 0xFFFFFFFF;
        for (byte b : data) {
            CRC = (CRC >>> 8) ^ pTable[(int) (b ^ (CRC & 0xFF)) & 0xFF];
        }
        CRC = (0xffffffff - CRC) & 0x00ffffffff;
        return Converter.getByteArray(CRC);
    }

    private static void createBaseValues() {
        int Poly = 0xEDB88320;

        int CRC;

        for (int i = 0; i < 256; i++) {
            CRC = i;
            for (int j = 0; j < 8; j++) {
                if ((CRC & 0x1) == 1) {
                    CRC = ((CRC >>> 1) ^ Poly);
                } else {
                    CRC = (CRC >>> 1);
                }
            }
            pTable[i] = (int) (CRC);
        }
    }

    public static byte[] encryptData(byte[] key, byte[] data) {
        byte[] encrypted = new byte[data.length];
        long lKey = (long) ((key[0] & 0xff) | ((key[1] & 0xff) << 8) | ((key[2] & 0xff) << 16) | ((key[3] & 0xff) << 24));
        long ltmpKey = lKey;

        for (int i = 0; i < data.length; i++) {
            lKey = lKey * 0x3d09;
            ltmpKey = lKey >> 0x10;
            encrypted[i] = (byte) (data[i] + (byte) (ltmpKey & 0xff));
        }
        return encrypted;
    }

    public static boolean isValidOgg(byte[] oggFile) {
        boolean ret = false;

        if (oggFile[0] != 'O' || oggFile[1] != 'g' || oggFile[2] != 'g' || oggFile[3] != 'S')
            return false;

        //Header laden
        if (oggFile.length < 0x1B)
            return false;
/*
        int headerSize = 0x1B+oggFile[0x1B];
        byte[] header = new byte[headerSize];
        for(int i = 0; i < headerSize; i++)header[i]=oggFile[i];

        int currentPage = 1;
        while(oggFile.length > currentPage * 255)
*/
        for (int i = 0x200; i < oggFile.length; i++) {
            if (oggFile[i] == 'O' && oggFile[i + 1] == 'g' && oggFile[i + 2] == 'g' && oggFile[i + 3] == 'S') {
                ret = true;
                break;
            }
        }

        return ret;
    }
/*
    public static byte[] crc32OGG(byte[] oggData, byte page)
    {
        if(oggData.length > 0x10) {
            int pageSize = oggData[0x1A] * 0xff;

            byte[] crcData = new byte[]
            //set crc to zero

            CRC32 crc32 = new CRC32();
            crc32.update(data);


           /* crc = ^crc
            for i:=range data {
                crc = tab[byte(crc >> 24) ^ data[i]] ^(crc << 8)
            }*//*
        }
        return ^crc
    }*/

    public static byte[] encryptSDM(byte[] key, byte[] fileData) {
        byte[] data = Arrays.copyOfRange(fileData, 0, 1024);

        DES d = new DES();
        byte[] encrypted = d.encrypt(key, data);

        data = new byte[fileData.length + 1];
        for (int i = 0; i < encrypted.length / 2; i++) {
            data[i] = encrypted[i];
        }
        data[encrypted.length / 2] = 0;
        for (int i = (encrypted.length / 2) + 1; i < encrypted.length + 1; i++) {
            data[i] = encrypted[i - 1];
        }
        for (int i = encrypted.length + 1; i < fileData.length + 1; i++) {
            data[i] = (byte) ((249 - (int) (fileData[i - 1] & 0xff)) % 256);
        }
        return data;
    }

    public static byte[] decryptSDM(byte[] key, byte[] dataPrefix, byte[] fileData) {
        byte[] data = new byte[fileData.length + dataPrefix.length - 1];

        for (int i = 0; i < dataPrefix.length; i++) {
            data[i] = dataPrefix[i];
        }
        // erste Byte von fileData auslassen
        for (int i = 1; i < fileData.length; i++) {
            data[dataPrefix.length + i - 1] = fileData[i];
        }


        byte[] desEncrypted = new byte[1024];
        for (int i = 0; i < 1024; i++) {
            desEncrypted[i] = data[i];
        }
        DES d = new DES();
        byte[] decrypted = d.decrypt(key, desEncrypted);


        for (int i = 0; i < decrypted.length; i++) {
            data[i] = decrypted[i];
        }
        for (int i = decrypted.length; i < data.length; i++) {
            data[i] = (byte) ((505 - data[i]) % 256);
        }
        return data;
    }

    public static byte[] decryptData(byte[] key, byte[] data) {
        return decryptData(key, data, data.length);
    }

    public static byte[] decryptData(byte[] key, byte[] data, int length) {
        byte[] decrypted = new byte[length];
        long lKey = (long) ((key[0] & 0xff) | ((key[1] & 0xff) << 8) | ((key[2] & 0xff) << 16) | ((key[3] & 0xff) << 24));
        long ltmpKey = lKey;

        for (int i = 0; i < length; i++) {
            lKey = (lKey * 0x3d09) & 0xffffffffL;
            ltmpKey = lKey >> 0x10;
            decrypted[i] = (byte) (data[i] - (byte) (ltmpKey & 0xff));
        }
        return decrypted;
    }

    public static byte[] crackKey(byte[] data, byte[] expectedValue) {
        byte[] testData;
        int c = 0;
        int d = 0;

        for (long i = 0; i < 0xffffffffL; i++) {
            testData = decryptData(Converter.getByteArray((int) i), data, expectedValue.length);

            for (int b = 0; b < data.length; b++) {
                if (testData[b] == expectedValue[b]) {
                    d++;
                    c = b;
                    if (d == expectedValue.length) {
                        return Converter.getByteArray((int) i);
                    }
                } else {
                    d = 0;
                    break;
                }
            }
            if (c == data.length - 1)
                return Converter.getByteArray((int) i);
        }
        return null;
    }
}
