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


public class ByteBuffer {

    private final static int bufferSize = 1024;

    private byte[] buffer;
    private int size;
    private int currentPos;

    public ByteBuffer(byte[] data) {
        this.buffer = data;
        this.size = this.buffer.length;
        this.currentPos = 0;
    }

    public ByteBuffer() {
        this.buffer = new byte[ByteBuffer.bufferSize];
        this.size = 0;
        this.currentPos = 0;
    }

    public ByteBuffer(int len) {
        this.buffer = new byte[len];
        this.size = 0;
        this.currentPos = 0;
    }

    /**
     * Returns all written bytes without affecting the position.
     *
     * @return byte[] of all written bytes.
     */
    public byte[] getAllBytes() {
        byte[] bytes = new byte[this.size];
        System.arraycopy(this.buffer, 0, bytes, 0, this.size);
        return bytes;
    }

    public byte[] getAllBytes(int offset) {
        return this.getBytes(offset, this.size - offset);
    }

    public int getSize() {
        return this.size;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int newCurrentPos) {
        this.extendBufferForPositionIfNecessary(newCurrentPos);
        this.currentPos = newCurrentPos;
    }

    public void setCurrentPosStart() {
        this.setCurrentPos(0);
    }

    public void setCurrentPosEnd() {
        this.setCurrentPos(this.size);
    }

    public void addInt16(short value) {
        this.addByte(value & 0xff);
        this.addByte((value & 0xff00) >> 8);
    }

    public void addInt16(int value) {
        this.addInt16((short) value);
    }

    public void addInt32(int value) {
        this.addByte(value & 0xff);
        this.addByte((value & 0xff00) >> 8);
        this.addByte((value & 0xff0000) >> 16);
        this.addByte((value & 0xff000000) >> 24);
    }

    /**
     * Writes a Nul-Terminated String to the buffer.
     *
     * @param value The string
     */
    public void addStringNulTerminated(String value) {
        this.addString(value, true);
    }

    /**
     * Writes a String to the buffer.
     *
     * @param value The string.
     */
    public void addString(String value) {
        this.addString(value, false);
    }

    public void addFloat(float value) {
        byte[] data = java.nio.ByteBuffer.allocate(4).putFloat(value).array();
        byte[] reserveData = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            reserveData[i] = data[data.length - i - 1];
        }
        this.addBytes(reserveData);
    }

    public void addHEXString(String hexValues) {
        String s = hexValues.replaceAll(" ", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i + 1), 16));
        }
        this.addBytes(data);
    }

    public void addByte(int value) {
        this.addByte((byte) value);
    }

    public void addByte(byte value) {
        this.addBytes(new byte[]{value});
    }

    public void addBytes(byte[] value) {
        this.extendBufferIfNecessary(value.length);
        for (byte aValue : value) {
            this.buffer[this.currentPos++] = aValue;
        }
        this.updateSizeForPosition(this.currentPos);
    }

    /**
     * Specify an offset inside the buffer where the value should be read from.
     *
     * @param offset The offset.
     * @return Int16 at specified offset.
     */
    public short getInt16(int offset) {
        short value = (short) (this.buffer[offset++] & 0xff);
        value += (short) ((this.buffer[offset] & 0xff) << 8);
        return value;
    }

    public short getInt16() {
        short value = this.getInt16(this.currentPos);
        this.currentPos += 2;
        return value;
    }

    /**
     * Specify an offset inside the buffer where the value should be read from.
     *
     * @param offset The offset.
     * @return Int32 at specified offset.
     */
    public int getInt32(int offset) {
        int value = (buffer[offset++] & 0xff);
        value += ((buffer[offset++] & 0xff) << 8);
        value += ((buffer[offset++] & 0xff) << 16);
        value += ((buffer[offset] & 0xff) << 24);
        return value;
    }

    public int getInt32() {
        int value = this.getInt32(this.currentPos);
        this.currentPos += 4;
        return value;
    }

    public float getFloat() {
        byte[] data = new byte[4];
        for (int i = data.length - 1; i >= 0; i--) {
            data[i] = this.buffer[this.currentPos++];
        }
        float returnFloat = java.nio.ByteBuffer.wrap(data).getFloat();
        return returnFloat;
    }

    public byte[] getBytesNulTerminated() {
        int len = this.getLengthTillNulTermination(this.currentPos);
        return this.getBytes(len);
    }

    public byte[] getBytesNulTerminated(int offset) {
        int len = this.getLengthTillNulTermination(offset);
        return this.getBytes(offset, len);
    }

    public byte[] getBytes(int length) {
        byte[] data = this.getBytes(this.currentPos, length);
        // Advance to skip read bytes
        this.currentPos += length;
        return data;
    }

    public byte[] getBytes(int offset, int length) {

        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = this.getByte(offset++);
        }

        return data;
    }

    public byte getByte() {
        return this.getByte(this.currentPos++);
    }

    public byte getByte(int offset) {
        return this.buffer[offset];
    }

    public String getStringNulTerminated() {
        String str = this.getStringNulTerminated(this.currentPos);
        // Advance to skip read bytes
        this.currentPos += str.length();
        // Advance to skip the nul-byte
        this.currentPos++;
        return str;
    }

    public String getStringNulTerminated(int offset) {
        int len = this.getLengthTillNulTermination(offset);
        return this.getString(offset, len, false);
    }

    public String getString(int length) {
        String str = this.getString(this.currentPos, length, false);
        // Advance to skip read bytes
        this.currentPos += length;
        return str;
    }

    public String getStringFixed(int length) {
        String str = this.getString(this.currentPos, length, true);
        // Advance to skip read bytes
        this.currentPos += length;
        return str;
    }


    public String getString(int offset, int length, boolean skipNull) {
        StringBuilder sb = new StringBuilder();
        if (offset + length <= this.size) {
            byte[] strData = new byte[length];
            int count = 0;
            for (int i = 0; i < length; i++) {
                byte b = this.buffer[offset++];
                if (skipNull) {
                    if (b > 0) {
                        strData[count++] = b;
                    }
                } else {
                    strData[i] = b;
                }
            }
            try {
                sb.append(new String(strData, "ISO-8859-1"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String toHEXString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            sb.append(String.format("%02x ", this.buffer[i]));
        }
        return sb.toString();
    }

    public String toAsciiString(boolean spaced) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            char c = '.';
            if (this.buffer[i] >= 'A' && this.buffer[i] <= 'Z') c = (char) this.buffer[i];
            if (this.buffer[i] >= 'a' && this.buffer[i] <= 'z') c = (char) this.buffer[i];
            if (this.buffer[i] >= '0' && this.buffer[i] <= '9') c = (char) this.buffer[i];
            if (spaced && i != 0) {
                sb.append("  ");
            }
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbString = new StringBuilder();

        for (int i = 0; i < this.size; i++) {
            sb.append(String.format("%02x ", this.buffer[i]));

            char c = '.';
            if (this.buffer[i] >= 'A' && this.buffer[i] <= 'Z') c = (char) this.buffer[i];
            if (this.buffer[i] >= 'a' && this.buffer[i] <= 'z') c = (char) this.buffer[i];
            if (this.buffer[i] >= '0' && this.buffer[i] <= '9') c = (char) this.buffer[i];
            sbString.append(c);
        }

        return this.size + " " + sbString.toString() + '|' + sb.toString();
    }


    private void addString(String value, boolean nulTerminated) {
        try {
            this.addBytes(value.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (nulTerminated) this.addBytes(new byte[]{0});
    }

    private void extendBufferIfNecessary(int length) {
        int bLength = this.buffer.length;
        if (length + this.currentPos >= bLength) {
            int newSize = length + bLength + ByteBuffer.bufferSize;
            this.extendBuffer(newSize);
        }
    }

    private void extendBufferForPositionIfNecessary(int position) {
        int bLength = this.buffer.length;
        if (position >= bLength) {
            int newSize = position + bLength + ByteBuffer.bufferSize;
            this.extendBuffer(newSize);
        }
        this.updateSizeForPosition(position);
    }

    private void extendBuffer(int newSize) {
        byte[] extension = new byte[newSize];
        System.arraycopy(this.buffer, 0, extension, 0, this.size);
        this.buffer = extension;
    }

    private int getLengthTillNulTermination(int offset) {
        int len = 0;
        while ((offset + len) <= this.size && this.buffer[offset + len] != 0) {
            len++;
        }
        return len;
    }

    private void updateSizeForPosition(int position) {
        if (position > this.size) {
            this.size = position;
        }
    }
}
