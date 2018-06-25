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

import java.util.Arrays;

public class DES {
    private byte[] pc1 = new byte[]{0x39, 0x31, 0x29, 0x21, 0x19, 0x11, 0x09,
        0x01, 0x3a, 0x32, 0x2a, 0x22, 0x1a, 0x12, 0x0a, 0x02, 0x3b, 0x33,
        0x2b, 0x23, 0x1b, 0x13, 0x0b, 0x03, 0x3c, 0x34, 0x2c, 0x24, 0x3f,
        0x37, 0x2f, 0x27, 0x1f, 0x17, 0x0f, 0x07, 0x3e, 0x36, 0x2e, 0x26,
        0x1e, 0x16, 0x0e, 0x06, 0x3d, 0x35, 0x2d, 0x25, 0x1d, 0x15, 0x0d,
        0x05, 0x1c, 0x14, 0x0c, 0x04};
    private byte[] left_shift = new byte[]{0x01, 0x01, 0x02, 0x02, 0x02,
        0x02, 0x02, 0x02, 0x01, 0x02, 0x02, 0x02, 0x02, 0x02, 0x02, 0x01};
    private byte[] pc2 = new byte[]{0x0e, 0x11, 0x0b, 0x18, 0x01, 0x05, 0x03,
        0x1C, 0x0F, 0x06, 0x15, 0x0A, 0x17, 0x13, 0x0C, 0x04, 0x1A, 0x08,
        0x10, 0x07, 0x1B, 0x14, 0x0D, 0x02, 0x29, 0x34, 0x1F, 0x25, 0x2F,
        0x37, 0x1E, 0x28, 0x33, 0x2D, 0x21, 0x30, 0x2C, 0x31, 0x27, 0x38,
        0x22, 0x35, 0x2E, 0x2A, 0x32, 0x24, 0x1D, 0x20};
    private byte[] ip = new byte[]{0x3A, 0x32, 0x2A, 0x22, 0x1A, 0x12, 0x0A,
        0x02, 0x3C, 0x34, 0x2C, 0x24, 0x1C, 0x14, 0x0C, 0x04, 0x3E, 0x36,
        0x2E, 0x26, 0x1E, 0x16, 0x0E, 0x06, 0x40, 0x38, 0x30, 0x28, 0x20,
        0x18, 0x10, 0x08, 0x39, 0x31, 0x29, 0x21, 0x19, 0x11, 0x09, 0x01,
        0x3B, 0x33, 0x2B, 0x23, 0x1B, 0x13, 0x0B, 0x03, 0x3D, 0x35, 0x2D,
        0x25, 0x1D, 0x15, 0x0D, 0x05, 0x3F, 0x37, 0x2F, 0x27, 0x1F, 0x17,
        0x0F, 0x07};
    private byte[] reverseIP = new byte[]{40, 8, 48, 16, 56, 24, 64, 32, 39,
        7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5,
        45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43,
        11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9,
        49, 17, 57, 25};
    private byte[] eBitSelectionTable = new byte[]{32, 1, 2, 3, 4, 5, 4, 5,
        6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17,
        18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28,
        29, 30, 31, 32, 1};
    private byte[] pTable = new byte[]{16, 7, 20, 21, 29, 12, 28, 17, 1, 15,
        23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6,
        22, 11, 4, 25};
    private byte[][] sTables = new byte[][]{
        {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7,
            4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8,
            13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4,
            9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},
        {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4,
            7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11,
            10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3,
            15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},
        {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0,
            9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8,
            15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9,
            8, 7, 4, 15, 14, 3, 11, 5, 2, 12},
        {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11,
            5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12,
            11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1,
            13, 8, 9, 4, 5, 11, 12, 7, 2, 14},
        {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2,
            12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10,
            13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14,
            2, 13, 6, 15, 0, 9, 10, 4, 5, 3},
        {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4,
            2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2,
            8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15,
            10, 11, 14, 1, 7, 6, 0, 8, 13},
        {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11,
            7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13,
            12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4,
            10, 7, 9, 5, 0, 15, 14, 2, 3, 12},
        {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13,
            8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9,
            12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10,
            8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

    public byte[] decrypt(byte[] key, byte[] data) {
        byte[][] pKey = createKey(key);

        byte[] ret = decryptMessage(pKey, data);

        return ret;
    }

    public byte[] encrypt(byte[] key, byte[] data) {
        byte[][] pKey = createKey(key);

        byte[] ret = encryptMessage(pKey, data);

        return ret;
    }

    private byte[] encryptMessage(byte[][] K, byte[] data) {
        byte[] ret = new byte[data.length];

        for (int i = 0; i < data.length / 8; i++) {
            byte[] block = Arrays.copyOfRange(data, i * 8, i * 8 + 8);
            byte[] tmp = encryptBlock(K, block);
            for (int j = 0; j < block.length; j++) {
                ret[i * 8 + j] = tmp[j];
            }
        }

        return ret;
    }

    private byte[] decryptMessage(byte[][] K, byte[] data) {
        byte[] ret = new byte[data.length];

        for (int i = 0; i < data.length / 8; i++) {
            byte[] block = Arrays.copyOfRange(data, i * 8, i * 8 + 8);
            byte[] tmp = decryptBlock(K, block);
            for (int j = 0; j < block.length; j++) {
                ret[i * 8 + j] = tmp[j];
            }
        }

        return ret;
    }

    private byte[] encryptBlock(byte[][] K, byte[] data) {
        byte[] binData = byteArrayToBinArray(data);

        byte[] Mip = new byte[ip.length];
        for (int i = 0; i < ip.length; i++) {
            Mip[i] = binData[ip[i] - 1];
        }
        byte[][] L = new byte[17][];
        byte[][] R = new byte[17][];
        L[0] = Arrays.copyOfRange(Mip, 0, 32);
        R[0] = Arrays.copyOfRange(Mip, 32, 64);

        for (int i = 1; i <= 16; i++) {
            L[i] = R[i - 1];
            R[i] = new byte[32];

            byte[] f = f(R[i - 1], K[i]);
            for (int j = 0; j < f.length; j++) {
                R[i][j] = (byte) (L[i - 1][j] ^ f[j]);
            }
        }

        byte[] ret = new byte[64];
        for (int j = 0; j < 32; j++) {
            ret[j] = L[16][j];
            ret[j + 32] = R[16][j];
        }
        byte[] realRet = new byte[64];
        for (int j = 0; j < 64; j++) {
            realRet[j] = ret[reverseIP[j] - 1];
        }

        return binArrayToByteArray(realRet);
    }

    private byte[] decryptBlock(byte[][] K, byte[] data) {
        byte[] binData = byteArrayToBinArray(data);

        byte[] Mip = new byte[ip.length];
        for (int i = 0; i < ip.length; i++) {
            Mip[i] = binData[ip[i] - 1];
        }
        byte[][] L = new byte[17][];
        byte[][] R = new byte[17][];
        L[0] = Arrays.copyOfRange(Mip, 0, 32);
        R[0] = Arrays.copyOfRange(Mip, 32, 64);

        for (int i = 1; i <= 16; i++) {
            // L[i] = R[i - 1];
            R[i] = L[i - 1];
            L[i] = new byte[32];

            // byte[] f = f(R[i - 1], K[i]);
            byte[] f = f(L[i - 1], K[17 - i]);
            for (int j = 0; j < f.length; j++) {
                // R[i][j] = (byte) (L[i - 1][j] ^ f[j]);
                L[i][j] = (byte) (R[i - 1][j] ^ f[j]);
            }
        }

        byte[] ret = new byte[64];
        for (int j = 0; j < 32; j++) {
            ret[j] = L[16][j];
            ret[j + 32] = R[16][j];
        }
        byte[] realRet = new byte[64];
        for (int j = 0; j < 64; j++) {
            realRet[j] = ret[reverseIP[j] - 1];
        }

        return binArrayToByteArray(realRet);
    }

    private byte[] f(byte[] R, byte[] K) {
        byte[] ret = new byte[K.length];
        byte[] realRet = new byte[32];
        byte[] realRealRet = new byte[32];

        byte[] expR = e(R);

        for (int i = 0; i < K.length; i++) {
            ret[i] = (byte) (K[i] ^ expR[i]);
        }
        for (int i = 0; i < ret.length; i += 6) {
            byte[] sBlock = Arrays.copyOfRange(ret, i, i + 6);
            int row = (sBlock[0] << 1) + sBlock[5];
            int col = (sBlock[1] << 3) + (sBlock[2] << 2) + (sBlock[3] << 1)
                + sBlock[4];
            byte[] afterS = byteArrayToQuadBinArray(sTables[i / 6][row * 16
                + col]);

            for (int j = 0; j < 4; j++) {
                realRet[(i / 6) * 4 + j] = afterS[j];
            }
        }
        for (int j = 0; j < 32; j++) {
            realRealRet[j] = realRet[pTable[j] - 1];
        }

        return realRealRet;
    }

    private byte[] e(byte[] R) {
        byte[] ret = new byte[eBitSelectionTable.length];

        for (int i = 0; i < eBitSelectionTable.length; i++) {
            ret[i] = R[eBitSelectionTable[i] - 1];
        }

        return ret;
    }

    private byte[][] createKey(byte[] key) {
        byte[][] k = null;
        byte[][] c = new byte[17][];
        byte[][] d = new byte[17][];

        byte[] binKey = byteArrayToBinArray(key);
        byte[] perm1Key = permutatePC1(binKey);
        calcSubkeys(perm1Key, c, d);
        k = permutatePC2(c, d);

        return k;
    }

    private byte[][] permutatePC2(byte[][] c, byte[][] d) {
        byte[][] k = new byte[17][];

        for (int i = 1; i <= 16; i++) {
            byte[] data = new byte[56];
            for (int j = 0; j < 28; j++) {
                data[j] = c[i][j];
                data[j + 28] = d[i][j];
            }
            k[i] = new byte[pc2.length];
            for (int j = 0; j < pc2.length; j++) {
                k[i][j] = data[pc2[j] - 1];
            }
        }

        return k;
    }

    private void calcSubkeys(byte[] perm1Key, byte[][] c, byte[][] d) {
        c[0] = Arrays.copyOfRange(perm1Key, 0, 28);
        d[0] = Arrays.copyOfRange(perm1Key, 28, 56);

        for (int i = 0; i < left_shift.length; i++) {
            int curShift = left_shift[i];
            byte[] tmpC = Arrays.copyOfRange(c[i], 0, curShift);
            byte[] tmpD = Arrays.copyOfRange(d[i], 0, curShift);

            c[i + 1] = new byte[28];
            d[i + 1] = new byte[28];
            for (int j = curShift; j < 28; j++) {
                c[i + 1][j - curShift] = c[i][j];
                d[i + 1][j - curShift] = d[i][j];
            }
            for (int j = 0; j < curShift; j++) {
                c[i + 1][28 - curShift + j] = tmpC[j];
                d[i + 1][28 - curShift + j] = tmpD[j];
            }
        }
    }

    private byte[] permutatePC1(byte[] binKey) {
        byte[] data = new byte[pc1.length];

        for (int i = 0; i < pc1.length; i++) {
            data[i] = binKey[pc1[i] - 1];
        }

        return data;
    }

    private byte[] byteArrayToBinArray(byte[] b) {
        byte[] data = new byte[b.length * 8];
        for (int i = 0; i < b.length; i++) {
            for (int j = 7; j >= 0; j--) {
                int pos = i * 8 + (j);
                if ((byte) (b[i] & (int) Math.pow(2, j)) != 0) {
                    data[pos] = 1;
                }
            }
        }
        return data;
    }

    private byte[] binArrayToByteArray(byte[] b) {
        byte[] data = new byte[b.length / 8];
        for (int i = 0; i < b.length / 8; i++) {
            byte dat = 0;
            dat = (byte) (b[i * 8 + 7] << 7);
            dat += (byte) (b[i * 8 + 6] << 6);
            dat += (byte) (b[i * 8 + 5] << 5);
            dat += (byte) (b[i * 8 + 4] << 4);
            dat += (byte) (b[i * 8 + 3] << 3);
            dat += (byte) (b[i * 8 + 2] << 2);
            dat += (byte) (b[i * 8 + 1] << 1);
            dat += (byte) (b[i * 8]);
            data[i] = dat;
        }
        return data;
    }

    private byte[] byteArrayToQuadBinArray(byte b) {
        byte[] data = new byte[4];

        for (int j = 3; j >= 0; j--) {
            int pos = j;
            if ((byte) (b & (int) Math.pow(2, j)) != 0) {
                data[pos] = 1;
            }
        }
        return data;
    }
}
