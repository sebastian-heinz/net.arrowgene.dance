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

import net.arrowgene.dance.library.common.SlotFinder;

import java.util.ArrayList;
import java.util.List;


public class Inventory {

    public static final int CLOTHES_PER_PAGE = 8;
    public static final int ITEMS_PER_PAGE = 8;

    /**
     * Cloth slots can be used from 200 - 400
     */
    public static final int MAXIMUM_CLOTHES = 200;

    /**
     * Item slots can be used from 400 - ???
     */
    public static final int MAXIMUM_ITEMS = 200;

    public static final int MAX_CLOTH_PAGES = MAXIMUM_CLOTHES / CLOTHES_PER_PAGE;
    public static final int MAX_ITEM_PAGES = MAXIMUM_ITEMS / ITEMS_PER_PAGE;


    private SlotFinder clothSlots;
    private SlotFinder itemSlots;
    private ArrayList<InventoryItem> items;
    private int hairSlot;
    private int glassesSlot;
    private int topSlot;
    private int shoesSlot;
    private int faceSlot;
    private int glovesSlot;
    private int pantsSlot;


    public Inventory() {
        this.itemSlots = new SlotFinder(MAXIMUM_ITEMS);
        this.clothSlots = new SlotFinder(MAXIMUM_CLOTHES);
        this.items = new ArrayList<InventoryItem>();
    }

    public int getHairSlot() {
        return hairSlot;
    }

    public void setHairSlot(int hairSlot) {
        this.hairSlot = hairSlot;
    }

    public int getGlassesSlot() {
        return glassesSlot;
    }

    public void setGlassesSlot(int glassesSlot) {
        this.glassesSlot = glassesSlot;
    }

    public int getTopSlot() {
        return topSlot;
    }

    public void setTopSlot(int topSlot) {
        this.topSlot = topSlot;
    }

    public int getShoesSlot() {
        return shoesSlot;
    }

    public void setShoesSlot(int shoesSlot) {
        this.shoesSlot = shoesSlot;
    }

    public int getFaceSlot() {
        return faceSlot;
    }

    public void setFaceSlot(int faceSlot) {
        this.faceSlot = faceSlot;
    }

    public int getGlovesSlot() {
        return glovesSlot;
    }

    public void setGlovesSlot(int glovesSlot) {
        this.glovesSlot = glovesSlot;
    }

    public int getPantsSlot() {
        return pantsSlot;
    }

    public void setPantsSlot(int pantsSlot) {
        this.pantsSlot = pantsSlot;
    }

    public ArrayList<InventoryItem> getItems() {
        return new ArrayList<InventoryItem>(this.items);
    }

    public InventoryItem getItem(int inventoryId) {
        InventoryItem inventoryItem = null;
        for (InventoryItem item : this.items) {
            if (item.getId() == inventoryId) {
                inventoryItem = item;
                break;
            }
        }
        return inventoryItem;
    }

    public InventoryItem getItemByShopItemId(int shopItemId) {
        InventoryItem inventoryItem = null;
        for (InventoryItem item : this.items) {
            if (item.getShopItem().getId() == shopItemId) {
                inventoryItem = item;
                break;
            }
        }
        return inventoryItem;
    }

    public InventoryItem getItemByModelId(int modelId) {
        InventoryItem inventoryItem = null;
        for (InventoryItem item : this.items) {
            if (item.getShopItem().getModelId() == modelId) {
                inventoryItem = item;
                break;
            }
        }
        return inventoryItem;
    }

    public InventoryItem getItemBySlotNumber(int slotId) {
        InventoryItem inventoryItem = null;
        for (InventoryItem item : this.items) {
            if (item.getSlotId() == slotId) {
                inventoryItem = item;
                break;
            }
        }
        return inventoryItem;
    }

    public void addItems(List<InventoryItem> items) {
        for (InventoryItem item : items) {
            this.addItem(item);
        }
    }

    public void addItem(InventoryItem inventoryItem) {

        InventoryItem existingQuantityItem = null;
        if (inventoryItem.getShopItem().getQuantity() != ItemQuantityType.NOT_CONSUMABLE) {
            // Consumable item, check if an item already exists.
            existingQuantityItem = this.getItemByModelId(inventoryItem.getShopItem().getModelId());
        }

        if (existingQuantityItem != null) {
            // A Consumable item exists, increase the amount.
            existingQuantityItem.increaseQuantity(inventoryItem.getShopItem().getQuantity().getNumValue());
        } else {
            InventorySlotType slotType = inventoryItem.getShopItem().getSlotType();
            int slot = SlotFinder.INVALID_SLOT;

            if (slotType == InventorySlotType.ITEMS) {
                slot = this.itemSlots.takeSlot();
            } else if (slotType == InventorySlotType.CLOTHES) {
                slot = this.clothSlots.takeSlot();
            }

            if (slot != SlotFinder.INVALID_SLOT) {
                inventoryItem.setSlotNumber(slot);
                this.items.add(inventoryItem);
            }
        }
    }

    public void removeItem(InventoryItem inventoryItem) {

        InventorySlotType slotType = inventoryItem.getShopItem().getSlotType();
        int slot = inventoryItem.getSlotNumber();

        if (slotType == InventorySlotType.ITEMS) {
            this.itemSlots.freeSlot(slot);
        } else if (slotType == InventorySlotType.CLOTHES) {
            this.clothSlots.freeSlot(slot);
        }

        this.items.remove(inventoryItem);
    }

    public void removeItemBySlotId(int slotId) {
        InventoryItem item = this.getItemBySlotNumber(slotId);
        if (item != null) {
            this.removeItem(item);
        }
    }

    /**
     * @return Count of clothes
     */
    public int clothCount() {
        int clothCount = 0;
        for (InventoryItem item : this.items) {
            if (item.getShopItem().getSlotType() == InventorySlotType.CLOTHES) {
                clothCount++;
            }
        }
        return clothCount;
    }

    /**
     * @return Count of equipable Items (World Speaker, etc...)
     */
    public int itemsCount() {
        int itemCount = 0;
        for (InventoryItem item : this.items) {
            if (item.getShopItem().getSlotType() == InventorySlotType.ITEMS) {
                itemCount++;
            }
        }
        return itemCount;
    }


}
