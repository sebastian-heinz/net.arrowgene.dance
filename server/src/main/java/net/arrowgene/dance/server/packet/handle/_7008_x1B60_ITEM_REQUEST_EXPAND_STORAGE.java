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

import net.arrowgene.dance.library.models.item.InventorySlotType;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.shop.Shop;


public class _7008_x1B60_ITEM_REQUEST_EXPAND_STORAGE extends HandlerBase {

    public _7008_x1B60_ITEM_REQUEST_EXPAND_STORAGE(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        InventorySlotType inventorySlotType = InventorySlotType.getType(packet.getInt32());

        boolean canExpand = super.server.getShop().expandStorage(client.getCharacter(), inventorySlotType);

        SendPacket answerPacket = new SendPacket(PacketType.ITEM_RESPONSE_EXPAND_STORAGE);
        answerPacket.addInt32(inventorySlotType.getNumValue());

        if (canExpand) {
            answerPacket.addInt32(0);
        } else {
            answerPacket.addInt32(1);
        }

        answerPacket.addInt32(Shop.EXPAND_STORAGE_COST);

        if (inventorySlotType == InventorySlotType.CLOTHES) {
            answerPacket.addInt32(client.getCharacter().getClothSlotCount());
        } else if (inventorySlotType == InventorySlotType.ITEMS) {
            answerPacket.addInt32(client.getCharacter().getItemSlotCount());
        }

        client.sendPacket(answerPacket);
        return null;
    }
}
