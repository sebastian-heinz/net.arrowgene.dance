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

import net.arrowgene.dance.library.models.character.CharacterSexTyp;
import net.arrowgene.dance.library.models.item.InventoryItem;
import net.arrowgene.dance.library.models.item.ItemSexType;
import net.arrowgene.dance.library.models.wedding.RingType;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.shop.DressItemMsg;


public class _7002_x1B5A_ITEM_REQUEST_DRESS_ITEM extends HandlerBase {

    /**
     * This request is fired whenever an item is double clicked.
     * <p>
     * If the item is equipped it should be unequipped,
     * If the item is not equipped, it should be equipped.
     */
    public _7002_x1B5A_ITEM_REQUEST_DRESS_ITEM(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        short itemSlotId = packet.getInt16();
        InventoryItem inventoryItem = client.getInventory().getItemBySlotNumber(itemSlotId);

        DressItemMsg message;

        if (inventoryItem != null) {
            if (inventoryItem.isEquipped()) {
                client.unEquipItem(inventoryItem);
                message = DressItemMsg.MSG_NO_ERROR;
            } else {
                message = this.check(client, inventoryItem);
                if (message == DressItemMsg.MSG_NO_ERROR) {
                    client.equipItem(inventoryItem);
                }
            }
        } else {
            message = DressItemMsg.NO_ITEM_INFORMATION;
        }

        SendPacket answerPacket = new SendPacket(PacketType.ITEM_RESPONSE_DRESS_ITEM);
        answerPacket.addInt32(message.getNumValue());
        answerPacket.addInt32(0);
        client.sendPacket(answerPacket);

        return null;
    }

    private DressItemMsg check(DanceClient client, InventoryItem inventoryItem) {

        if (inventoryItem.getShopItem().isWeddingRing()) {
            if (client.getWeddingRecord() == null) {
                return DressItemMsg.FAILED_TO_WEAR_THE_RING_CHECK_CONDITIONS;
            }

            RingType allowedRingType = client.getWeddingRecord().getRingType();
            int ringId = -1;
            if (client.getWeddingRecord().getGroomId() == client.getCharacter().getId()) {
                ringId = this.server.getLoveMagistrate().getMaleRingId(allowedRingType);
            } else if (client.getWeddingRecord().getBrideId() == client.getCharacter().getId()) {
                ringId = this.server.getLoveMagistrate().getFemaleRingId(allowedRingType);
            }
            if (ringId != inventoryItem.getShopItem().getId()) {
                return DressItemMsg.FAILED_TO_WEAR_THE_RING_CHECK_CONDITIONS;
            }
        }

        if (inventoryItem.getShopItem().getMinLevel() > client.getCharacter().getLevel()) {
            return DressItemMsg.SORRY_YOU_MUST_MEET_THE_MINIMUM_LEVEL;
        }


        if (inventoryItem.getShopItem().getSex() == ItemSexType.FEMALE && client.getCharacter().getSex() != CharacterSexTyp.FEMALE) {
            return DressItemMsg.CANT_EQUIP_ITEM_WITH_CURRENT_GENDER;
        }

        if (inventoryItem.getShopItem().getSex() == ItemSexType.MALE && client.getCharacter().getSex() != CharacterSexTyp.MALE) {
            return DressItemMsg.CANT_EQUIP_ITEM_WITH_CURRENT_GENDER;
        }

        return DressItemMsg.MSG_NO_ERROR;
    }

}
