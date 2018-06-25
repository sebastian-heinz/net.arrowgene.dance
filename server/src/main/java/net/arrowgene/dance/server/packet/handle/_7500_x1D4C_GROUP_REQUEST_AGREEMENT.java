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

import net.arrowgene.dance.library.models.group.GroupAgreementType;
import net.arrowgene.dance.library.models.item.InventoryItem;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

import static net.arrowgene.dance.server.group.GroupManager.ITEM_CREATE_GROUP_CARD_ID;
import static net.arrowgene.dance.server.group.GroupManager.MIN_GROUP_LEVEL;


public class _7500_x1D4C_GROUP_REQUEST_AGREEMENT extends HandlerBase {

    public _7500_x1D4C_GROUP_REQUEST_AGREEMENT(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        GroupAgreementType agree = this.agree(client);

        SendPacket answerPacket = new SendPacket(PacketType.GROUP_RESPONSE_AGREEMENT);
        answerPacket.addInt32(agree.getNumValue());
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);

        return null;
    }

    private GroupAgreementType agree(DanceClient client) {

        if (!this.server.getGroupManager().isActivated()) {
            return GroupAgreementType.MSG_GROUPS_NOT_AVAILABLE;
        }

        if (client.getGroupMember() != null) {
            return GroupAgreementType.MSG_ALREADY_APPLIED_OR_JOINED;
        }

        if (client.getCharacter().getLevel() < MIN_GROUP_LEVEL) {
            return GroupAgreementType.MSG_LEVEL_TO_LOW;
        }

        InventoryItem createGroupCard = client.getInventory().getItemByShopItemId(ITEM_CREATE_GROUP_CARD_ID);
        if (createGroupCard == null) {
            return GroupAgreementType.MSG_MISSING_GROUP_CARD;
        }

        return GroupAgreementType.OK;
    }

}
