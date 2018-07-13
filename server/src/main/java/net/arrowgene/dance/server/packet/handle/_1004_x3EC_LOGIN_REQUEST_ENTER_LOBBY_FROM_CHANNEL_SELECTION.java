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

package net.arrowgene.dance.server.packet.handle;

import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.channel.Channel;
import net.arrowgene.dance.library.models.channel.ChannelType;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class _1004_x3EC_LOGIN_REQUEST_ENTER_LOBBY_FROM_CHANNEL_SELECTION extends HandlerBase {


    private static final Logger logger = LogManager.getLogger(_1004_x3EC_LOGIN_REQUEST_ENTER_LOBBY_FROM_CHANNEL_SELECTION.class);

    public _1004_x3EC_LOGIN_REQUEST_ENTER_LOBBY_FROM_CHANNEL_SELECTION(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        Packet answerPacket = new SendPacket(PacketType.LOGIN_RESPONSE_ENTER_LOBBY_FROM_CHANNEL_SELECTION);
        ChannelType type = ChannelType.getType(packet.getInt16());
        short number = packet.getInt16();

        Channel channel = server.getLobby().getChannel(type, number);

        if (channel == null) {
            logger.warn(String.format("tried to join a channel which is 'null' (%s)", client));
            answerPacket.addInt32(1);
        } else if (channel.isFull()) {
            logger.warn(String.format("couldn't join channel '%s' because it's full (%s)", channel.getDetails().getName(), client));
            answerPacket.addInt32(1);
        } else {
            logger.info(String.format("joined channel '%s' (%s)", channel.getDetails().getName(), client));
            channel.join(client);
        }
        answerPacket.addInt32(0);
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);
        return null;
    }

}
