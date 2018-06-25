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
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.ArrayList;


public class _1002_x3EA_LOGIN_REQUEST_CHANNEL_LIST extends HandlerBase {

    public _1002_x3EA_LOGIN_REQUEST_CHANNEL_LIST(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        SendPacket answerPacket = new SendPacket(PacketType.LOGIN_RESPONSE_CHANNEL_LIST, client);

        ArrayList<Channel> channels = server.getLobby().getChannels();
        answerPacket.addInt32(channels.size());
        answerPacket.addByte(0);// TODO: Nur ein packet, oder mehrere? Bei zu vielen channel eventuell bearbeiten

        for (Channel c : channels) {
            answerPacket.addInt16(c.getDetails().getType().getNumValue());
            answerPacket.addInt16(c.getDetails().getPosition());
            answerPacket.addStringNulTerminated(c.getDetails().getName());
            answerPacket.addInt32(c.getDetails().getMaxUser()); // Default 150 perhaps max Users?
            answerPacket.addInt32(c.getClientCount()); //0x0e
            answerPacket.addInt32(1);
            answerPacket.addInt32(1);
        }
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);
        return null;
    }
}
