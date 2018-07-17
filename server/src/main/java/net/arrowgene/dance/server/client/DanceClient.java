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

package net.arrowgene.dance.server.client;

import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.account.AccountSettings;
import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.character.SocialEntry;
import net.arrowgene.dance.library.models.group.Group;
import net.arrowgene.dance.library.models.group.GroupMember;
import net.arrowgene.dance.library.models.item.Inventory;
import net.arrowgene.dance.library.models.item.InventoryItem;
import net.arrowgene.dance.library.models.mail.Mailbox;
import net.arrowgene.dance.library.models.song.FavoriteSong;
import net.arrowgene.dance.library.models.wedding.WeddingRecord;
import net.arrowgene.dance.server.channel.Channel;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.builder.CharacterPacket;
import net.arrowgene.dance.server.packet.builder.ItemPacket;
import net.arrowgene.dance.server.room.Room;
import net.arrowgene.dance.server.tcp.TcpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DanceClient {

    private static final Logger logger = LogManager.getLogger(DanceClient.class);

    private TcpClient tcpClient;
    private Account account;
    private Character character;
    private Channel channel;
    private Room room;
    private Inventory inventory;
    private GroupMember groupMember;
    private Group group;
    private Mailbox mailbox;
    private AccountSettings settings;
    private List<SocialEntry> buddyList;
    private List<SocialEntry> blackList;
    private List<FavoriteSong> favoriteSongs;
    private WeddingRecord weddingRecord;
    private long lastAwayPing;
    private long totalSecondsAway;
    private boolean loadingDone = false;

    public DanceClient(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
        inventory = new Inventory();
        groupMember = null;
        mailbox = new Mailbox();
        buddyList = new ArrayList<>();
        blackList = new ArrayList<>();
        favoriteSongs = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("[Character:%s][Channel:%s][Room:%s][Client:%s]", character, channel, room, tcpClient);
    }

    public TcpClient getTcpClient() {
        return tcpClient;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if (room != null) {
            getCharacter().setReady(false);
            loadingDone = false;
        }
        this.room = room;
    }

    public boolean isLoadingDone() {
        return loadingDone;
    }

    public void setLoadingDone(boolean loadingDone) {
        this.loadingDone = loadingDone;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public GroupMember getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(GroupMember groupMember) {
        this.groupMember = groupMember;
    }

    public Mailbox getMailbox() {
        return mailbox;
    }

    public void setMailbox(Mailbox mailbox) {
        this.mailbox = mailbox;
    }

    public List<SocialEntry> getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(List<SocialEntry> buddyList) {
        this.buddyList = buddyList;
    }

    public List<SocialEntry> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<SocialEntry> blackList) {
        this.blackList = blackList;
    }

    public AccountSettings getSettings() {
        return settings;
    }

    public void setSettings(AccountSettings settings) {
        this.settings = settings;
    }

    public List<FavoriteSong> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void setFavoriteSongs(List<FavoriteSong> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
    }

    public void addBuddy(SocialEntry buddy) {
        buddyList.add(buddy);
    }

    public void removeBuddy(SocialEntry buddy) {
        buddyList.remove(buddy);
    }

    public void addFavoriteSong(FavoriteSong favoriteSong) {
        favoriteSongs.add(favoriteSong);
    }

    public void removeFavoriteSong(FavoriteSong favoriteSong) {
        favoriteSongs.remove(favoriteSong);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public WeddingRecord getWeddingRecord() {
        return weddingRecord;
    }

    public void setWeddingRecord(WeddingRecord weddingRecord) {
        this.weddingRecord = weddingRecord;
    }


    public long getLastAwayPing() {
        return lastAwayPing;
    }

    public void setLastAwayPing(long lastAwayPing) {
        this.lastAwayPing = lastAwayPing;
    }

    public long getTotalSecondsAway() {
        return totalSecondsAway;
    }

    public void setTotalSecondsAway(long totalSecondsAway) {
        this.totalSecondsAway = totalSecondsAway;
    }

    /**
     * @return indication weather the client is hosting a room.
     */
    public boolean isHostInRoom() {
        if (getRoom() != null) {
            if (getRoom().getHost() == this) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sends a packet to this client.
     *
     * @param packet Packet to be sent.
     */
    public void sendPacket(Packet packet) {
        if (tcpClient.getAlive()) {
            tcpClient.sendPacket(packet.getAllBytes());
            // TODO log packet
            //logger.writePacketLog(packet, this, LogType.RESPONSE_PACKET);
        }
    }

    /**
     * Reloads the character info seen in the lobby.
     */
    public void refreshCharacter() {
        // This will refresh the numbers (Level, coins, etc..)
        Packet characterInfo = CharacterPacket.getInstance().getCharacter(this);
        sendPacket(characterInfo);

        // This will refresh the small character image
        Packet itemList = ItemPacket.getInstance().getItemList(inventory.getItems());
        sendPacket(itemList);
    }

    /**
     * Disconnects the client on the next opportunity, allowing for a graceful shutdown.
     */
    public void disconnect() {
        tcpClient.setAlive(false);
    }

    /**
     * Equips all items which are marked as equipped in the inventory.
     */
    public void loadEquippedItems() {
        for (InventoryItem item : inventory.getItems()) {
            if (item.isEquipped()) {
                equipItem(item);
            }
        }
    }

    /**
     * Equips a given item to the character.
     *
     * @param inventoryItem Item to equip
     */
    public void equipItem(InventoryItem inventoryItem) {

        int modelId = inventoryItem.getShopItem().getModelId();
        int slotId = inventoryItem.getSlotId();
        inventoryItem.setEquipped(true);

        switch (inventoryItem.getShopItem().getCategory()) {
            case HEAD_N_HAIR_HAIRSTYLE_MALE:
            case HEAD_N_HAIR_HAIRSTYLE_FEMALE:
                character.setHair(modelId);
                inventory.setHairSlot(slotId);
                break;
            case CLOTHING_TOPS_MALE:
            case CLOTHING_TOPS_FEMALE:
                character.setTop(modelId);
                inventory.setTopSlot(slotId);
                break;
            case CLOTHING_BOTTOMS_MALE:
            case CLOTHING_BOTTOMS_FEMALE:
                character.setPants(modelId);
                inventory.setPantsSlot(slotId);
                break;
            case CLOTHING_GLOVES_MALE:
            case CLOTHING_GLOVES_FEMALE:
                character.setGloves(modelId);
                inventory.setGlovesSlot(slotId);
                break;
            case CLOTHING_SHOES_MALE:
            case CLOTHING_SHOES_FEMALE:
                character.setShoes(modelId);
                inventory.setShoesSlot(slotId);
                break;
            case HEAD_N_HAIR_FACES_MALE:
            case HEAD_N_HAIR_FACES_FEMALE:
                character.setFace(modelId);
                inventory.setFaceSlot(slotId);
                break;
            case CLOTHING_GLASSES_MALE:
            case CLOTHING_GLASSES_FEMALE:
                character.setGlasses(modelId);
                inventory.setGlassesSlot(slotId);
                break;
            case CLOTHING_ONE_PIECE_MALE:
            case CLOTHING_ONE_PIECE_FEMALE:
                //TODO Check how we dress these items
                //    top = itemId;
                //   topSlot = slotId;
                //    pants = 0;
                //    pantsSlot = 0;
                break;
            case CLOTHING_OUTFIT_MALE:
            case CLOTHING_OUTFIT_FEMALE:
                break;
            case ITEMS_MAIN_CONSUMABLES:
            case ITEMS_AVATAR_EFFECTS:
                break;
        }
    }

    /**
     * Removes an equipped item from the character.
     *
     * @param inventoryItem Item to unequip
     */
    public void unEquipItem(InventoryItem inventoryItem) {

        inventoryItem.setEquipped(false);

        switch (inventoryItem.getShopItem().getCategory()) {
            case HEAD_N_HAIR_HAIRSTYLE_MALE:
            case HEAD_N_HAIR_HAIRSTYLE_FEMALE:
                character.setHair(Character.DEFAULT_CLOTH_ID);
                inventory.setHairSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_TOPS_MALE:
            case CLOTHING_TOPS_FEMALE:
                character.setTop(Character.DEFAULT_CLOTH_ID);
                inventory.setTopSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_BOTTOMS_MALE:
            case CLOTHING_BOTTOMS_FEMALE:
                character.setPants(Character.DEFAULT_CLOTH_ID);
                inventory.setPantsSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_GLOVES_MALE:
            case CLOTHING_GLOVES_FEMALE:
                character.setGloves(Character.DEFAULT_CLOTH_ID);
                inventory.setGlovesSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_SHOES_MALE:
            case CLOTHING_SHOES_FEMALE:
                character.setShoes(Character.DEFAULT_CLOTH_ID);
                inventory.setShoesSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case HEAD_N_HAIR_FACES_MALE:
            case HEAD_N_HAIR_FACES_FEMALE:
                character.setFace(Character.DEFAULT_CLOTH_ID);
                inventory.setFaceSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_GLASSES_MALE:
            case CLOTHING_GLASSES_FEMALE:
                character.setGlasses(Character.DEFAULT_CLOTH_ID);
                inventory.setGlassesSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_ONE_PIECE_MALE:
            case CLOTHING_ONE_PIECE_FEMALE:
                //TODO Check how we dress these items
                //    top = itemId;
                //   topSlot = slotId;
                //    pants = 0;
                //    pantsSlot = 0;
                break;
            case CLOTHING_OUTFIT_MALE:
            case CLOTHING_OUTFIT_FEMALE:
                break;
            case ITEMS_MAIN_CONSUMABLES:
            case ITEMS_AVATAR_EFFECTS:
                break;
        }
    }

}
