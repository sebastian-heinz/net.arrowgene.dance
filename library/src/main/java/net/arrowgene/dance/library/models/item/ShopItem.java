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


public class ShopItem {

    public static final int LUCKY_TICKET_MODEL_ID = 100789;

    private int id;
    private String name;
    private int price;

    private InventorySlotType slotType;

    /**
     * Id for texture
     */
    private int modelId;

    /**
     * @see ItemCategoryType
     */
    private ItemCategoryType category;

    /**
     * min level to wear/buy item
     */
    private int minLevel;

    /**
     * @see ItemDurationType
     */
    private ItemDurationType duration;

    /**
     * @see ItemQuantityType
     */
    private ItemQuantityType quantity;

    /**
     * @see ItemPriceCategoryType
     */
    private ItemPriceCategoryType priceCategory;

    /**
     * @see ItemSexType
     */
    private ItemSexType sex;

    /**
     * Indicates if this item is a wedding ring
     */
    private boolean weddingRing;


    public ShopItem() {

    }

    public InventorySlotType getSlotType() {
        switch (this.category) {
            case ITEMS_MAIN_CONSUMABLES:
            case ITEMS_AVATAR_EFFECTS:
                return InventorySlotType.ITEMS;
            default:
                return InventorySlotType.CLOTHES;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public ItemCategoryType getCategory() {
        return category;
    }

    public void setCategory(ItemCategoryType category) {
        this.category = category;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public ItemDurationType getDuration() {
        return duration;
    }

    public void setDuration(ItemDurationType duration) {
        this.duration = duration;
    }

    public ItemQuantityType getQuantity() {
        return quantity;
    }

    public void setQuantity(ItemQuantityType quantity) {
        this.quantity = quantity;
    }

    public ItemPriceCategoryType getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(ItemPriceCategoryType priceCategory) {
        this.priceCategory = priceCategory;
    }

    public ItemSexType getSex() {
        return sex;
    }

    public void setSex(ItemSexType sex) {
        this.sex = sex;
    }

    public boolean isWeddingRing() {
        return weddingRing;
    }

    public void setWeddingRing(boolean weddingRing) {
        this.weddingRing = weddingRing;
    }

    @Override
    public String toString() {
        return "[" + this.id + "] " + this.name + " (" + this.price + ")";
    }
}
