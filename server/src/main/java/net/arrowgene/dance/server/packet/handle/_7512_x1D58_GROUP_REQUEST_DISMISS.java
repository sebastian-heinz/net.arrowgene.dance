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
import net.arrowgene.dance.library.models.group.GroupDismissMemberMsg;
import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

public class _7512_x1D58_GROUP_REQUEST_DISMISS extends HandlerBase {

    public _7512_x1D58_GROUP_REQUEST_DISMISS(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        String characterName = packet.getStringNulTerminated();
        int a = packet.getInt32();

        GroupMember groupMember = super.server.getGroupManager().getGroupMemberByCharacterName(characterName);

        GroupDismissMemberMsg answer = this.dismiss(client, groupMember);

        SendPacket answerPacket = new SendPacket(PacketType.GROUP_RESPONSE_DISMISS);
        answerPacket.addInt32(answer.getNumValue());
        answerPacket.addByte(0);
        client.sendPacket(answerPacket);

        return null;
    }

    private GroupDismissMemberMsg dismiss(DanceClient client, GroupMember groupMember) {

        if (groupMember == null) {
            return GroupDismissMemberMsg.MEMBER_NOT_FOUND;
        }

        Group group = super.server.getGroupManager().getGroupById(groupMember.getGroupId());
        if (client.getCharacter().getId() != group.getLeaderId()) {
            return GroupDismissMemberMsg.NO_RIGHTS;
        }

        super.server.getGroupManager().leaveGroup(groupMember.getCharacterId());

        return GroupDismissMemberMsg.OK;

        //return GroupDismissMemberMsg.ERROR;
    }

}
