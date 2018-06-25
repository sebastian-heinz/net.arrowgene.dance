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

import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.character.CharacterSexTyp;
import net.arrowgene.dance.library.models.wedding.WeddingState;
import net.arrowgene.dance.library.models.group.Group;
import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.library.models.group.GroupRights;
import net.arrowgene.dance.library.models.item.Inventory;
import net.arrowgene.dance.library.models.item.InventoryItem;
import net.arrowgene.dance.library.models.item.ShopItem;
import net.arrowgene.dance.library.models.wedding.RingType;
import net.arrowgene.dance.library.models.wedding.WeddingRecord;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;

public class CharacterPacket {

    private static CharacterPacket instance = new CharacterPacket();

    public static final int DEFAULT_CLOTH_ID = 0x7FFFFFFF;

    public static CharacterPacket getInstance() {
        return instance;
    }

    public Packet getCharacter(DanceClient client) {
        Packet packet = new SendPacket(PacketType.LOBBY_RESPONSE_CHARACTER_INFO);
        this.writeCharacter(packet, client.getCharacter(), client.getInventory(), client.getGroupMember(), client.getGroup(), client.getWeddingRecord());
        return packet;
    }

    public void writeCharacter(Packet packet, Character character, Inventory inventory, GroupMember member, Group group, WeddingRecord weddingRecord) {
        packet.addInt32(0);
        packet.addStringNulTerminated(character.getName());
        packet.addByte(character.getSex().getNumValue());

        if (weddingRecord != null) {
            WeddingState myState = weddingRecord.getWeddingState(character.getCharacterId());
            packet.addByte(myState.getNumValue());
            if (myState == WeddingState.MARRIED || myState == WeddingState.DIVORCE) {
                String partnerName = weddingRecord.getPartnerCharacterName(character.getCharacterId());
                packet.addStringNulTerminated(partnerName);
            } else {
                packet.addStringNulTerminated("");
            }
            packet.addByte(weddingRecord.getRingType().getNumValue());
        } else {
            packet.addByte(WeddingState.NOT_MARRIED.getNumValue());
            packet.addStringNulTerminated("");
            packet.addByte(RingType.NONE.getNumValue());
        }

        packet.addByte(character.getController().getNumValue());
        packet.addInt32(character.getLevel());
        packet.addInt32(character.getExperience());
        packet.addInt32(character.getGames());
        packet.addInt32(character.getWins());
        packet.addInt32(character.getDraws());
        packet.addInt32(character.getLosses());
        packet.addInt32(character.getHearts());
        packet.addInt32(character.getMvp());
        packet.addInt32(character.getPerfects());
        packet.addInt32(character.getCools());
        packet.addInt32(character.getBads());
        packet.addInt32(character.getMisses());
        packet.addInt32(character.getPoints());
        packet.addInt32(character.getCoins());
        packet.addByte(character.getClothSlotCount());
        packet.addByte(character.getItemSlotCount());
        packet.addInt32(character.getBonus());
        packet.addInt32(character.getWeight());

        packet.addByte(0);

        //Item Slots
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);
        packet.addInt16(0);

        packet.addInt32(character.getRanking());
        packet.addByte(0);

        this.writeModel(packet, character.getSex());

        //Cloth Slots
        packet.addInt16(0xff);
        packet.addInt16(inventory.getHairSlot());
        packet.addInt16(inventory.getTopSlot());
        packet.addInt16(inventory.getPantsSlot());
        packet.addInt16(inventory.getGlovesSlot());
        packet.addInt16(inventory.getShoesSlot());
        packet.addInt16(inventory.getFaceSlot());
        packet.addInt16(inventory.getGlassesSlot());
        packet.addInt16(0xff);
        packet.addInt16(0xff);
        packet.addInt16(0xff);

        this.writeGroupMember(packet, member, group);

        packet.addInt32(character.getPointsWon());
        packet.addInt32(character.getStatusAchieved());
        packet.addInt32(character.getBestScore());
        packet.addInt32(character.getCompetitionWon());
        packet.addInt32(character.getCompetitionLost());

        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);

        InventoryItem luckyTicket = inventory.getItemByModelId(ShopItem.LUCKY_TICKET_MODEL_ID);
        if (luckyTicket != null) {
            packet.addInt32(luckyTicket.getQuantity());
        } else {
            packet.addInt32(0);
        }


        packet.addInt32(0);
        packet.addInt32(0);

        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);

        packet.addInt16(0);
        packet.addInt32(-1);
        packet.addByte(0);
    }

    public void writeAboutMe(Packet packet, Character character, GroupMember member, Group group, WeddingRecord weddingRecord) {
        packet.addInt32(0);
        packet.addStringNulTerminated(character.getName());
        packet.addByte(character.getSex().getNumValue());

        if (weddingRecord != null) {
            WeddingState myState = weddingRecord.getWeddingState(character.getCharacterId());
            if (myState == WeddingState.MARRIED || myState == WeddingState.DIVORCE) {
                String partnerName = weddingRecord.getPartnerCharacterName(character.getCharacterId());
                packet.addStringNulTerminated(partnerName);
            } else {
                packet.addStringNulTerminated("");
            }
            packet.addByte(weddingRecord.getRingType().getNumValue());
        } else {
            packet.addStringNulTerminated("");
            packet.addByte(RingType.NONE.getNumValue());
        }

        packet.addInt32(character.getLevel());
        packet.addInt32(character.getExperience());
        packet.addInt32(character.getPoints());
        packet.addInt32(character.getCoins());
        packet.addInt32(character.getHearts());
        packet.addInt32(character.getMvp());
        packet.addInt32(0);
        packet.addByte(character.getController().getNumValue());
        packet.addInt32(character.getGames());
        packet.addInt32(character.getWins());
        packet.addInt32(character.getDraws());
        packet.addInt32(character.getLosses());
        packet.addInt32(character.getPerfects());
        packet.addInt32(character.getCools());
        packet.addInt32(character.getBads());
        packet.addInt32(character.getMisses());
        packet.addInt32(character.getWeight());
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addInt32(character.getPointsWon());
        packet.addInt32(character.getStatusAchieved());
        packet.addInt32(character.getBestScore());
        packet.addInt32(character.getCompetitionWon());
        packet.addInt32(character.getCompetitionLost());
        packet.addInt32(character.getAllTimeBestRanking());
        packet.addInt16(character.getMedal());

        this.writeModel(packet, character.getSex());

        packet.addInt32(DEFAULT_CLOTH_ID);
        packet.addInt32(character.getHair());
        packet.addInt32(character.getTop());
        packet.addInt32(character.getPants());
        packet.addInt32(character.getGloves());
        packet.addInt32(character.getShoes());
        packet.addInt32(character.getFace());
        packet.addInt32(character.getGlasses());
        packet.addInt32(DEFAULT_CLOTH_ID);
        packet.addInt32(DEFAULT_CLOTH_ID);
        packet.addInt32(DEFAULT_CLOTH_ID);

        this.writeGroupMember(packet, member, group);

        packet.addInt32(0);

        packet.addInt32(character.getCalorinsLostWeek());
        // TODO Reject Bills, etc speichern
        packet.addByte(1); // Reject Bills     1 = unchecked
        packet.addByte(1); // Invitations      1 = unchecked
        packet.addByte(1); // Post my Profile  1 = checked
        packet.addByte(0);
        packet.addByte(character.getAge());
        packet.addStringNulTerminated(character.getCity());
        packet.addByte(0);
        packet.addStringNulTerminated(character.getZodiac());
        packet.addStringNulTerminated(character.getInfo());

        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);

        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);

        packet.addInt32(0);
        packet.addInt32(0);
        packet.addInt32(0);

        packet.addByte(0);
    }

    private void writeGroupMember(Packet packet, GroupMember member, Group group) {
        if (member != null && group != null) {
            packet.addStringNulTerminated(group.getName());
            packet.addInt16(group.getIcon());
            packet.addByte(member.getGroupRights().getNumValue());
            packet.addInt32(member.getScore());

            // Setting to 0 treats the person like he is not in the group. Maybe for applicants?
            if (member.getGroupRights() == GroupRights.APPLICANT) {
                packet.addByte(0);
            } else {
                packet.addByte(1);
            }

            packet.addStringNulTerminated(group.getCreationDateString());
        } else {
            packet.addBytes(new byte[10]);
        }
    }

    /**
     * The base model / 3d shape? of the character (head, upper body, lower body, feet)
     */
    private void writeModel(Packet packet, CharacterSexTyp sex) {

        if (sex == CharacterSexTyp.MALE) {
            packet.addInt32(0xdbba2);
            packet.addInt32(0xdbba3);
            packet.addInt32(0xdbba4);
            packet.addInt32(0xdbba6);
        } else {
            packet.addInt32(0xdbba8);
            packet.addInt32(0xdbba9);
            packet.addInt32(0xdbbaa);
            packet.addInt32(0xdbbac);
        }
    }
}
