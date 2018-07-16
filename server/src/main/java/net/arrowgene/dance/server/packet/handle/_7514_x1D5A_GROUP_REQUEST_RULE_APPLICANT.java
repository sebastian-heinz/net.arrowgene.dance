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
import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.library.models.group.GroupRights;
import net.arrowgene.dance.library.models.group.GroupRuleApplicantMsg;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

public class _7514_x1D5A_GROUP_REQUEST_RULE_APPLICANT extends HandlerBase {

    public static final int ACCEPT = 1;
    public static final int REFUSE = 0;

    public _7514_x1D5A_GROUP_REQUEST_RULE_APPLICANT(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        int count = packet.getInt16();
        int op = packet.getByte();

        SendPacket answerPacket = new SendPacket(PacketType.GROUP_RESPONSE_RULE_APPLICANT);
        Group group = client.getGroup();

        if (group.getMembers().size() + count > group.getMaxMembers()) {
            answerPacket.addInt32(GroupRuleApplicantMsg.GROUP_IS_MAXIMUM_SIZE_BUY_EXPANSION_CARD.getNumValue());
        } else if (client.getCharacter().getId() == group.getLeaderId()) {
            for (int i = 0; i < count; i++) {
                String characterName = packet.getStringNulTerminated();
                GroupMember member = super.server.getGroupManager().getGroupMemberByCharacterName(characterName);
                if (member == null) {
                    // The member has left the group, while the leader is still viewing the application list
                    // and can potentially accept or reject the person.
                    answerPacket.addInt32(GroupRuleApplicantMsg.WRONG_DATABASE.getNumValue());
                } else if (op == ACCEPT) {
                    member.setGroupRights(GroupRights.MEMBER);
                } else if (op == REFUSE) {
                    super.server.getGroupManager().leaveGroup(member.getCharacterId());
                }
            }
            answerPacket.addInt32(count);
        } else {
            answerPacket.addInt32(GroupRuleApplicantMsg.NO_RIGHTS.getNumValue());
        }
        answerPacket.addByte(op);
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);

        return null;
    }
}
