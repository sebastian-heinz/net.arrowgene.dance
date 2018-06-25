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

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Converter {

    public static String getAddressString(SocketAddress socAddress) {
        String address = null;
        if (socAddress != null) {
            if (socAddress instanceof InetSocketAddress) {
                InetSocketAddress iNetAddress = (InetSocketAddress) socAddress;
                String ip = iNetAddress.getAddress().toString();
                int port = iNetAddress.getPort();
                address = ip + ":" + port;
            }
        }
        return address;
    }

    public static byte[] getByteArray(String hexString) {
        String s = hexString.replaceAll(" ", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    public static String getHEXString(byte[] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length; i++) {
            sb.append(String.format("%02X", array[i]));
        }

        return sb.toString();
    }

    public static String getString(byte[] array) {
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(new String(array, "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static byte[] getByteArray(int value) {
        byte[] data = new byte[4];
        data[0] = (byte) (value & 0xff);
        data[1] = (byte) ((value & 0xff00) >> 8);
        data[2] = (byte) ((value & 0xff0000) >> 16);
        data[3] = (byte) ((value & 0xff000000) >> 24);

        return data;
    }

    public static int getInt(byte[] data) {
        int retValue = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            retValue <<= 8;
            retValue |= data[i] & 0xff;
        }
        return retValue;
    }

    public static int getInt(byte[] data, int offset, int length) {
        byte[] value = new byte[length];
        for (int i = offset; i < offset + length; i++) {
            value[i - offset] = data[i];
        }

        return getInt(value);
    }
}
