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

package net.arrowgene.dance.server.packet.builder;

import net.arrowgene.dance.library.models.item.InventoryItem;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.List;

public class ItemPacket {

    private static ItemPacket instance = new ItemPacket();

    public static ItemPacket getInstance() {
        return instance;
    }

    public Packet getItemList(List<InventoryItem> inventoryItems) {
        Packet packet = new SendPacket(PacketType.ITEM_RESPONSE_ITEM_LIST);
        this.writeItemList(packet, inventoryItems);
        return packet;
    }

    public void writeItemList(Packet packet, List<InventoryItem> inventoryItems) {
        packet.addInt32(inventoryItems.size());
        for (InventoryItem inventoryItem : inventoryItems) {
            packet.addInt16(inventoryItem.getSlotId());
            packet.addInt32(inventoryItem.getShopItem().getModelId());
            packet.addInt32(inventoryItem.getQuantity());
            packet.addByte(0); //expired?
            packet.addStringNulTerminated(inventoryItem.getExpireDateString());
            packet.addByte(1);
            packet.addStringNulTerminated(inventoryItem.getShopItem().getName());
            packet.addInt32(inventoryItem.getShopItem().getMinLevel());
        }
        packet.addByte(0);
    }


}
