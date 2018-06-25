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

package net.arrowgene.dance.server;

import net.arrowgene.dance.library.common.ByteBuffer;
import net.arrowgene.dance.log.Log;
import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.log.Logger;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.tcp.TcpClient;

import java.util.ArrayList;
import java.util.Arrays;


// TODO CLEAN UP LOGGSER AND INTERFACE
public class ServerLogger extends Logger {

    private boolean logPackets;
    private ArrayList<PacketType> ignorePackets;
    private ArrayList<PacketType> allowPackets;


    public ServerLogger(ServerConfig config) {
        super(config.getLogDirectory());
        this.logPackets = config.isLogPackets();
        this.allowPackets = new ArrayList<>();
        this.ignorePackets = new ArrayList<>(Arrays.asList(
            PacketType.LOBBY_REQUEST_PING,
            PacketType.LOBBY_RESPONSE_PING,
            PacketType.AWAY_REQUEST));
    }

    /**
     * Add an packet to the ignored filter, preventing it from showing up in the logs.
     */
    public void addIgnorePacketType(PacketType ignorePacketType) {
        if (!this.ignorePackets.contains(ignorePacketType)) {
            this.ignorePackets.add(ignorePacketType);
        }
    }

    /**
     * Remove an packet to the ignored filter, allowing it to show up in the logs.
     */
    public void removeIgnorePacketType(PacketType ignorePacketType) {
        this.ignorePackets.remove(ignorePacketType);
    }

    /**
     * Clear the ignored filter, allowing all packets to be logged.
     */
    public void clearIgnorePacketTypes() {
        this.ignorePackets.clear();
    }

    /**
     * Add an packet to the allowed filter, causing only packets of these types to show up in logs.
     */
    public void addAllowPacketType(PacketType ignorePacketType) {
        if (!this.ignorePackets.contains(ignorePacketType)) {
            this.ignorePackets.add(ignorePacketType);
        }
    }

    /**
     * Remove an packet from the allowed filter, making it not show up in the logs anymore.
     * Note: If the allowed packet type filter is empty, all packets are shown.
     */
    public void removeAllowPacketType(PacketType ignorePacketType) {
        this.ignorePackets.remove(ignorePacketType);
    }

    /**
     * Removes all allowed packets from the filter, turning this filter off.
     */
    public void clearAllowPacketTypes() {
        this.allowPackets.clear();
    }

    public void writeLog(LogType logType, String className, String methodName, String text, DanceClient client) {
        text = className + "::" + methodName + ":" + text;
        Log log = new Log(text, logType, this.getAccountText(client));
        this.writeLog(log);
    }

    public void writeLog(LogType logType, String className, String methodName, String text, TcpClient tcpClient) {
        text = className + "::" + methodName + ":" + text;
        Log log = new Log(text, logType, tcpClient.getIdentity());
        this.writeLog(log);
    }

    public void writeLog(LogType logType, String text, DanceClient danceClient) {
        this.writeLog(logType, text, this.getAccountText(danceClient));
    }

    public void writeLog(LogType logType, String text, TcpClient tcpClient) {
        this.writeLog(logType, text, tcpClient.getIdentity());
    }

    public void writePacketLog(Packet packet, DanceClient client, LogType logType) {
        if (this.shouldLogPacket(packet)) {
            packet.writeHeader();
            this.writeLog(logType, this.createPacketLogText(packet), client);
        }
    }

    public void writeUnknownPacketLog(short id, short size, byte[] data, TcpClient tcpClient) {
        Packet packet = this.reconstructPacket(id, size, data);
        this.writeLog(LogType.UNKNOWN_PACKET, this.createPacketLogText(packet), tcpClient.getIdentity());
    }

    public void writeDataLog(String name, int size, byte[] data, TcpClient tcpClient) {
        if (size < 0) {
            size = 0;
        }
        ByteBuffer buf = new ByteBuffer(size);
        for (int i = 0; i < size; i++) {
            buf.addByte(data[i]);
        }
        String text = NEW_LINE +
            "----------------------------------------" + NEW_LINE +
            "Name___:" + name + NEW_LINE +
            "Size___:" + buf.getSize() + NEW_LINE +
            "Buffer_:" + buf.toHEXString() + NEW_LINE +
            "Chars__:" + buf.toAsciiString(true) + NEW_LINE +
            "----------------------------------------" + NEW_LINE;
        this.writeLog(LogType.UNKNOWN_PACKET, text, tcpClient.getIdentity());
    }

    // TODO Duplicate
    public void writeLog(Throwable throwable, TcpClient client) {
        String trace = "-no trace-";
        if (throwable != null) {
            trace = throwable.toString() + "\n";
            for (StackTraceElement el : throwable.getStackTrace()) {
                if (el != null) {
                    trace += "\t at " + el.toString() + "\n";
                }
            }
        }
        if (client != null) {
            this.writeLog(LogType.ERROR, trace, client);
        } else {
            this.writeLog(LogType.ERROR, trace);
        }
    }

    private String createPacketLogText(Packet packet) {

        PacketType pType = PacketType.getType(packet.getPacketId());

        String identity = "Unnamed";
        if (pType != null) {
            identity = pType.name();
        }

        return NEW_LINE +
            "----------------------------------------" + NEW_LINE +
            "Name___:" + identity + NEW_LINE +
            "Id_____:" + packet.getPacketId() + NEW_LINE +
            "Size___:" + packet.getSize() + NEW_LINE +
            "Buffer_:" + packet.toHEXString() + NEW_LINE +
            "Chars__:" + packet.toAsciiString(true) + NEW_LINE +
            "----------------------------------------" + NEW_LINE;
    }

    private String getAccountText(DanceClient client) {
        String account = "";
        if (client != null) {
            if (client.getAccount() != null) {
                account += client.getAccount().getUsername();
            }
            if (client.getTcpClient() != null) {
                account += "[" + client.getTcpClient().getIdentity() + "]";
            }
        }
        return account;
    }

    private Packet reconstructPacket(short id, short size, byte[] data) {
        Packet packet = new Packet(id, data);

        // Reconstruct original header
        packet.setCurrentPos(0);
        packet.addInt16(size);
        packet.addInt16(id);

        return packet;
    }

    private boolean shouldLogPacket(Packet packet) {
        if (this.logPackets) {
            if (this.allowPackets.size() > 0) {
                if (this.allowPackets.contains(PacketType.getType(packet.getPacketId()))) {
                    return true;
                }
            } else {
                if (!this.ignorePackets.contains(PacketType.getType(packet.getPacketId()))) {
                    return true;
                }
            }
        }
        return false;
    }

}
