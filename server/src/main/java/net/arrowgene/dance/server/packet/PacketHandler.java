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

package net.arrowgene.dance.server.packet;

import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.packet.handle.HandlerBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PacketHandler {


    private static final Logger logger = LogManager.getLogger(PacketHandler.class);
    private Map<PacketType, HandlerBase> packetHandles;

    public PacketHandler() {
        this.packetHandles = new HashMap<PacketType, HandlerBase>();
    }

    public void addHandle(PacketType type, HandlerBase handler) {
        this.packetHandles.put(type, handler);
    }

    public void handle(ReadPacket packet, DanceClient sender) {
        if (packet != null) {
            // TODO log packet
            // this.logger.writePacketLog(packet, sender, LogType.REQUEST_PACKET);
            HandlerBase handler = this.packetHandles.get(packet.getPacketType());
            if (handler != null) {
                SendPacket[] packets = handler.handle(packet, sender);
                if (packets != null) {
                    for (SendPacket sendPacket : packets) {
                        for (DanceClient receiver : sendPacket.getReceivers()) {
                            receiver.sendPacket(sendPacket);
                        }
                    }
                }
            }
        } else {
            logger.info(String.format("Read packet is null (%s)", sender));
        }
    }

}
