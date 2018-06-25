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

import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.List;


public class _7508_x1D54_GROUP_REQUEST_MEMBER_LIST extends HandlerBase {

    public _7508_x1D54_GROUP_REQUEST_MEMBER_LIST(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {


        SendPacket memberPacket = new SendPacket(PacketType.GROUP_RESPONSE_MEMBER_LIST);
        SendPacket applicationPacket = new SendPacket(PacketType.GROUP_RESPONSE_MEMBER_LIST);

        if (client.getGroup() != null) {
            //Members
            List<GroupMember> members = client.getGroup().getMembers();
            memberPacket.addByte(1); //Current Members = 1 | Applicants = 0
            memberPacket.addInt32(members.size()); //Count
            memberPacket.addByte(1); //Indicates how many packets to read?
            for (GroupMember member : members) {
                memberPacket.addStringNulTerminated(member.getCharacterName()); //Nickname
                memberPacket.addInt32(member.getCharacterLevel()); //Level
                memberPacket.addStringNulTerminated(member.getDateString()); //Registered
                memberPacket.addInt32(member.getScore()); //Scores
            }
            memberPacket.addByte(0);

            //Applications
            List<GroupMember> applications = client.getGroup().getApplicants();
            applicationPacket.addByte(0); //Current Members = 1 | Applicants = 0
            applicationPacket.addInt32(applications.size()); //Count
            applicationPacket.addByte(1); //Indicates how many packets to read? 16777216 = last packet, (1, 0, 16777216)
            for (GroupMember applicant : applications) {
                applicationPacket.addStringNulTerminated(applicant.getCharacterName()); //Nickname
                applicationPacket.addInt32(applicant.getCharacterLevel()); //Level
                applicationPacket.addStringNulTerminated(applicant.getDateString()); //Registered
            }
            applicationPacket.addByte(0);
        } else {
            // TODO User got kicked but is in group screen and asked about group member lists,
            // Since he is not in the group anymore we send him empty lists.
            // is there a way to show an error?

            applicationPacket.addByte(0);
            applicationPacket.addInt32(0);
            applicationPacket.addByte(1);
            applicationPacket.addByte(0);

            memberPacket.addByte(1);
            memberPacket.addInt32(0);
            memberPacket.addByte(1);
            memberPacket.addByte(0);
        }

        client.sendPacket(memberPacket);
        client.sendPacket(applicationPacket);

        return null;
    }
}
