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

import net.arrowgene.dance.library.models.group.Group;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.ArrayList;
import java.util.List;


public class _7527_x1D67_GROUP_REQUEST_SEARCH_GROUP extends HandlerBase {

    public _7527_x1D67_GROUP_REQUEST_SEARCH_GROUP(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {


        String searchQuery = packet.getStringNulTerminated();

        List<Group> results = new ArrayList<Group>();
        for (Group group : this.server.getGroupManager().getGroups()) {
            if (group.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                results.add(group);
            }
        }

        SendPacket answerPacket = new SendPacket(PacketType.GROUP_RESPONSE_SEARCH_GROUP);
        answerPacket.addInt32(results.size());
        int i = 1;
        for (Group group : results) {
            answerPacket.addByte(i++); //count / order?
            answerPacket.addStringNulTerminated(group.getName());
            answerPacket.addStringNulTerminated(group.getLeaderName());
            answerPacket.addInt16(group.getMembers().size());
            answerPacket.addStringNulTerminated(group.getCreationDateString());
            answerPacket.addInt32(group.getRanking());
        }
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);

        return null;
    }
}
