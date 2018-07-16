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

import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.group.Group;
import net.arrowgene.dance.library.models.group.GroupQuitMsg;
import net.arrowgene.dance.library.models.item.Inventory;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.group.GroupManager;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

public class _7506_x1D52_GROUP_REQUEST_LEAVE extends HandlerBase {

    public _7506_x1D52_GROUP_REQUEST_LEAVE(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        GroupQuitMsg message = this.leave(client);

        SendPacket answerPacket = new SendPacket(PacketType.GROUP_RESPONSE_LEAVE);
        answerPacket.addInt32(message.getNumValue());
        answerPacket.addByte(0);
        client.sendPacket(answerPacket);

        return null;
    }

    private GroupQuitMsg leave(DanceClient client) {

        Character character = client.getCharacter();
        Group group = client.getGroup();

        if(group == null){
            return GroupQuitMsg.OK;
        }

        if (character.getId() == group.getLeaderId()) {
            Inventory inventory = client.getInventory();
            if (inventory.getItemByShopItemId(GroupManager.ITEM_DISBAND_GROUP_CARD_ID) == null) {
                return GroupQuitMsg.DISMISS_CARD;
            }
            // TODO remove 1* group Disband Card from Inventory.
        }

        super.server.getGroupManager().leaveGroup(character.getId());
        return GroupQuitMsg.OK;
    }

}
