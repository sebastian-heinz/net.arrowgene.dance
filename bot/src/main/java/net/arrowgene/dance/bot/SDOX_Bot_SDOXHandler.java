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

package net.arrowgene.dance.bot;


import net.arrowgene.dance.server.packet.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Daniel on 02.05.2015.
 */
public class SDOX_Bot_SDOXHandler implements Runnable {
    private Socket socket;
    private SDOX_Bot bot;
    private Thread readingThread;
    private boolean bRunning = true;

    public SDOX_Bot_SDOXHandler(Socket socket, SDOX_Bot bot) {
        this.bot = bot;
        this.socket = socket;
        readingThread = new Thread(this);
        readingThread.start();
    }

    @Override
    public void run() {
        System.out.println("Reading from Server");
        while (bRunning) {
            Packet packet = read();
            System.out.println("New packet from Server");
            if (packet != null) {
                System.out.println(packet.toString());
            } else {
                bRunning = false;
            }
        }
        System.out.println("Connection closed to Server");
    }

    public void sendPacket(Packet packet) {
        try {
            socket.getOutputStream().write(packet.getAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] data, int length) {
        try {
            socket.getOutputStream().write(data, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Packet read() {
        Packet pData = null;
        int length = -1;
        byte[] packetData = new byte[0xffff];
        byte[] data = new byte[0xffff];
        int packetLength = 0;

        try {
            length = socket.getInputStream().read(data, 0, 2);
            bot.write(data, length);
            if (length == 2) {
                packetLength = (int) (((data[1] & 0xff) << 8) | (data[0] & 0xff)) & 0xffff;

                InputStream inStream = socket.getInputStream();
                int loadedLength = 2;
                while (loadedLength < packetLength) {
                    length = inStream.read(packetData, 0, (packetLength - loadedLength > 1024) ? 1024 : packetLength - loadedLength);
                    bot.write(packetData, length);
                    for (int i = 0; i < length; i++) {
                        data[i + loadedLength - 2] = packetData[i];
                    }
                    loadedLength += length;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (packetLength <= 0) {
            // TODO Fehler -> Verbindung abbrechen
            System.out.println("packetLength to small");
        } else {
            short packetId = (short) (((data[3] & 0xff) << 8) | (data[2] & 0xff));
            pData = new Packet(packetId, data);
        }

        return pData;
    }
}
