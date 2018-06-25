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
import net.arrowgene.dance.server.ServerLogger;
import net.arrowgene.dance.server.channel.Channel;
import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.log.Logger;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.builder.CharacterPacket;
import net.arrowgene.dance.server.packet.builder.ItemPacket;
import net.arrowgene.dance.server.room.Room;
import net.arrowgene.dance.server.tcp.TcpClient;

import java.util.ArrayList;
import java.util.List;

public class DanceClient {

    private ServerLogger logger;
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

    public DanceClient(TcpClient tcpClient, ServerLogger logger) {
        this.tcpClient = tcpClient;
        this.logger = logger;
        this.inventory = new Inventory();
        this.groupMember = null;
        this.mailbox = new Mailbox();
        this.buddyList = new ArrayList<>();
        this.blackList = new ArrayList<>();
        this.favoriteSongs = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Client: ");
        if (getCharacter() != null) {
            sb.append(getCharacter() + " ");
        }
        if (getRoom() != null) {
            sb.append("Room: " + getRoom().getNumber() + "-" + getRoom().getName() + " ");
        }

        return sb.toString();
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
        this.buddyList.add(buddy);
    }

    public void removeBuddy(SocialEntry buddy) {
        this.buddyList.remove(buddy);
    }

    public void addFavoriteSong(FavoriteSong favoriteSong) {
        this.favoriteSongs.add(favoriteSong);
    }

    public void removeFavoriteSong(FavoriteSong favoriteSong) {
        this.favoriteSongs.remove(favoriteSong);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public WeddingRecord getWeddingRecord() {
        return this.weddingRecord;
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
        if (this.tcpClient.getAlive()) {
            this.tcpClient.sendPacket(packet.getAllBytes());
            this.logger.writePacketLog(packet, this, LogType.RESPONSE_PACKET);
        }
    }

    /**
     * Reloads the character info seen in the lobby.
     */
    public void refreshCharacter() {
        // This will refresh the numbers (Level, coins, etc..)
        Packet characterInfo = CharacterPacket.getInstance().getCharacter(this);
        this.sendPacket(characterInfo);

        // This will refresh the small character image
        Packet itemList = ItemPacket.getInstance().getItemList(this.inventory.getItems());
        this.sendPacket(itemList);
    }

    /**
     * Disconnects the client on the next opportunity, allowing for a graceful shutdown.
     */
    public void disconnect() {
        this.tcpClient.setAlive(false);
    }

    /**
     * Equips all items which are marked as equipped in the inventory.
     */
    public void loadEquippedItems() {
        for (InventoryItem item : this.inventory.getItems()) {
            if (item.isEquipped()) {
                this.equipItem(item);
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
                this.character.setHair(modelId);
                this.inventory.setHairSlot(slotId);
                break;
            case CLOTHING_TOPS_MALE:
            case CLOTHING_TOPS_FEMALE:
                this.character.setTop(modelId);
                this.inventory.setTopSlot(slotId);
                break;
            case CLOTHING_BOTTOMS_MALE:
            case CLOTHING_BOTTOMS_FEMALE:
                this.character.setPants(modelId);
                this.inventory.setPantsSlot(slotId);
                break;
            case CLOTHING_GLOVES_MALE:
            case CLOTHING_GLOVES_FEMALE:
                this.character.setGloves(modelId);
                this.inventory.setGlovesSlot(slotId);
                break;
            case CLOTHING_SHOES_MALE:
            case CLOTHING_SHOES_FEMALE:
                this.character.setShoes(modelId);
                this.inventory.setShoesSlot(slotId);
                break;
            case HEAD_N_HAIR_FACES_MALE:
            case HEAD_N_HAIR_FACES_FEMALE:
                this.character.setFace(modelId);
                this.inventory.setFaceSlot(slotId);
                break;
            case CLOTHING_GLASSES_MALE:
            case CLOTHING_GLASSES_FEMALE:
                this.character.setGlasses(modelId);
                this.inventory.setGlassesSlot(slotId);
                break;
            case CLOTHING_ONE_PIECE_MALE:
            case CLOTHING_ONE_PIECE_FEMALE:
                //TODO Check how we dress these items
                //    this.top = itemId;
                //   this.topSlot = slotId;
                //    this.pants = 0;
                //    this.pantsSlot = 0;
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
                this.character.setHair(Character.DEFAULT_CLOTH_ID);
                this.inventory.setHairSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_TOPS_MALE:
            case CLOTHING_TOPS_FEMALE:
                this.character.setTop(Character.DEFAULT_CLOTH_ID);
                this.inventory.setTopSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_BOTTOMS_MALE:
            case CLOTHING_BOTTOMS_FEMALE:
                this.character.setPants(Character.DEFAULT_CLOTH_ID);
                this.inventory.setPantsSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_GLOVES_MALE:
            case CLOTHING_GLOVES_FEMALE:
                this.character.setGloves(Character.DEFAULT_CLOTH_ID);
                this.inventory.setGlovesSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_SHOES_MALE:
            case CLOTHING_SHOES_FEMALE:
                this.character.setShoes(Character.DEFAULT_CLOTH_ID);
                this.inventory.setShoesSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case HEAD_N_HAIR_FACES_MALE:
            case HEAD_N_HAIR_FACES_FEMALE:
                this.character.setFace(Character.DEFAULT_CLOTH_ID);
                this.inventory.setFaceSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_GLASSES_MALE:
            case CLOTHING_GLASSES_FEMALE:
                this.character.setGlasses(Character.DEFAULT_CLOTH_ID);
                this.inventory.setGlassesSlot(InventoryItem.NOT_EQUIPPED_SLOT_ID);
                break;
            case CLOTHING_ONE_PIECE_MALE:
            case CLOTHING_ONE_PIECE_FEMALE:
                //TODO Check how we dress these items
                //    this.top = itemId;
                //   this.topSlot = slotId;
                //    this.pants = 0;
                //    this.pantsSlot = 0;
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
