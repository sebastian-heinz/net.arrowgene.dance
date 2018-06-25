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

package net.arrowgene.dance.library.models.item;

import java.text.Format;
import java.text.SimpleDateFormat;

public class InventoryItem {

    public static final int NOT_EQUIPPED_SLOT_ID = 0;

    private int id;
    private int characterId;
    private int slotNumber;
    private int quantity;
    private long expireDate;
    private boolean equipped;
    private ShopItem shopItem;


    public InventoryItem(ShopItem shopItem) {
        this.shopItem = shopItem;
    }

    public InventoryItem(ShopItem shopItem, int characterId, int quantity, long expireDate) {
        this(shopItem);
        this.id = -1;
        this.characterId = characterId;
        this.slotNumber = -1; //slotNumber will be assigned when added to Inventory.
        this.quantity = quantity;
        this.expireDate = expireDate;
        this.equipped = false;
    }

    /**
     * This is the id used in packets, to identify an Item Slot.
     *
     * @return itemSlotId
     */
    public int getSlotId() {
        if (this.slotNumber >= 0) {
            return this.shopItem.getSlotType().getNumValue() + this.slotNumber;
        } else {
            return -1;
        }
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public ShopItem getShopItem() {
        return shopItem;
    }

    public String getExpireDateString() {
        if (this.expireDate != -1) {
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(this.expireDate * 1000);
        } else {
            return "Permanent";
        }
    }

}
