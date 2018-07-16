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

package net.arrowgene.dance.server.shop;


import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.item.*;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.client.DanceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Shop extends ServerComponent {


    private static final Logger logger = LogManager.getLogger(Shop.class);

    public static final int EXPAND_STORAGE_COST = 1000;

    private List<ShopItem> items;

    public Shop(DanceServer server) {
        super(server);
        this.items = new ArrayList<>();
    }

    @Override
    public void load() {
        this.items = super.getDatabase().getShopItems();
    }

    @Override
    public void save() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void clientAuthenticated(DanceClient client) {

    }

    @Override
    public void clientDisconnected(DanceClient client) {

    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {
        logger.debug(String.format("Items: %d", items.size()));
    }

    public ArrayList<ShopItem> getShopItems() {
        return new ArrayList<ShopItem>(this.items);
    }

    public ShopItem getShopItem(int itemId) {
        ShopItem foundItem = null;
        for (ShopItem item : this.items) {
            if (item.getId() == itemId) {
                foundItem = item;
                break;
            }
        }
        return foundItem;
    }

    public ShopMessages buyItem(int itemId, DanceClient client) {
        ShopItem shopItem = this.getShopItem(itemId);
        if (shopItem == null) {
            logger.error(String.format("ag_items missing item id: %d", itemId));
            return ShopMessages.MSG_ERROR;
        } else {
            return this.buyItem(shopItem, client);
        }
    }

    public ShopMessages buyItem(ShopItem shopItem, DanceClient client) {

        if (shopItem == null) {
            logger.error("Shop Item is null");
            return ShopMessages.MSG_ERROR;
        }

        Character character = client.getCharacter();
        Inventory inventory = client.getInventory();

        if (!this.hasSpace(character, inventory, shopItem.getSlotType())) {
            return ShopMessages.MSG_YOU_HAVE_NO_MORE_ROOM;
        }
        if (!this.canAfford(character, shopItem)) {
            return ShopMessages.MSG_YOU_DO_NOT_HAVE_ENOUGH_COINS_POINTS_OR_BONUS_FOR_THIS_ITEM;
        }
        if (!this.spendMoney(character, shopItem)) {
            logger.error("Payment Error!!!!! Investigate and make more robust");
            return ShopMessages.MSG_ERROR_PLEASE_CHECK;
        }

        InventoryItem invItem = this.craftItem(shopItem, client.getCharacter().getId());
        client.getInventory().addItem(invItem);


        return ShopMessages.MSG_NO_ERROR;
    }

    public InventoryItem craftItem(ShopItem shopItem, int characterId) {

        long expireDate = 0;
        switch (shopItem.getDuration()) {
            case PERMANENT: { //Forever
                expireDate = -1;
                break;
            }
            case ZERO: { //unknown
                expireDate = 1;
                break;
            }
            case SEVEN: {
                expireDate = generateExpireDate(7);
                break;
            }
            case THIRTY: {
                expireDate = generateExpireDate(30);
                break;
            }
        }

        InventoryItem inventoryItem = new InventoryItem(shopItem, characterId, shopItem.getQuantity().getNumValue(), expireDate);

        return inventoryItem;
    }

    public boolean expandStorage(Character character, InventorySlotType slotType) {

        if (!this.canAfford(character, ItemPriceCategoryType.POINTS, Shop.EXPAND_STORAGE_COST)) {
            return false;
        }

        if (!this.spendMoney(character, ItemPriceCategoryType.POINTS, Shop.EXPAND_STORAGE_COST)) {
            return false;
        }

        if (slotType == InventorySlotType.CLOTHES) {
            character.expandClothSlots();
        } else if (slotType == InventorySlotType.ITEMS) {
            character.expandItemSlots();
        }

        return true;
    }

    public boolean spendMoney(Character character, ShopItem shopItem) {
        return this.spendMoney(character, shopItem.getPriceCategory(), shopItem.getPrice());
    }

    public boolean spendMoney(Character character, ItemPriceCategoryType currency, int amount) {
        if (currency == ItemPriceCategoryType.POINTS) {
            character.setPoints(character.getPoints() - amount);
        } else if (currency == ItemPriceCategoryType.COINS) {
            character.setCoins(character.getCoins() - amount);
        } else if (currency == ItemPriceCategoryType.BONUS) {
            character.setBonus(character.getBonus() - amount);
        }
        return super.getDatabase().insertCharacter(character);
    }

    public boolean canAfford(Character character, ItemPriceCategoryType currency, int amount) {
        if (currency == ItemPriceCategoryType.POINTS) {
            return character.getPoints() >= amount;
        } else if (currency == ItemPriceCategoryType.COINS) {
            return character.getCoins() >= amount;
        } else if (currency == ItemPriceCategoryType.BONUS) {
            return character.getBonus() >= amount;
        }
        return false;
    }

    public boolean canAfford(Character character, ShopItem shopItem) {
        return this.canAfford(character, shopItem.getPriceCategory(), shopItem.getPrice());
    }

    public boolean hasSpace(Character character, Inventory inventory, InventorySlotType slotType) {
        boolean space = false;
        if (slotType == InventorySlotType.CLOTHES) {
            space = character.getClothSlotCount() > inventory.clothCount();
        } else if (slotType == InventorySlotType.ITEMS) {
            space = character.getItemSlotCount() > inventory.itemsCount();
        }
        return space;
    }

    private long generateExpireDate(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return c.getTimeInMillis() / 1000;
    }

}
