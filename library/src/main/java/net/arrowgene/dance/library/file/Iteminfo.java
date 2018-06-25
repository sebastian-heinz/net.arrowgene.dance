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

package net.arrowgene.dance.library.file;

import net.arrowgene.dance.library.common.ByteBuffer;
import net.arrowgene.dance.library.common.FileOp;
import net.arrowgene.dance.library.models.item.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Read/Write Iteminfo.dat
 */
public class Iteminfo {

    public static final int ITEM_LENGTH = 152;

    private int headA;
    private int headB;
    private int headItemCount;
    private List<ShopItem> items;

    public Iteminfo() {
        items = new ArrayList<>();
    }


    public Iteminfo(String iteminfoPath) throws Exception {
        this(Paths.get(iteminfoPath));
    }

    public Iteminfo(Path iteminfoPath) throws Exception {
        this();
        if (!Files.isRegularFile(iteminfoPath)) {
            throw new Exception("Invalid iteminfoPath");
        }
        byte[] file = FileOp.readFile(iteminfoPath);
        ByteBuffer buffer = new ByteBuffer(file);
        this.headA = buffer.getInt32();
        this.headB = buffer.getInt32();
        this.headItemCount = buffer.getInt32();
        if (this.headA != 2) {
            throw new Exception("Unexpected file header");
        }
        if (this.headB != 7008) {
            throw new Exception("Unexpected file header");
        }
        while (buffer.getCurrentPos() < buffer.getSize() - ITEM_LENGTH) {
            // TODO check if we miss the last item
            byte[] block = buffer.getBytes(ITEM_LENGTH);
            byte[] decryptedBlock = this.decrypt(block);
            ShopItem item = this.createItem(new ByteBuffer(decryptedBlock));
            this.items.add(item);
        }
    }

    public int getHeadA() {
        return headA;
    }

    public int getHeadB() {
        return headB;
    }

    public int getHeadItemCount() {
        return headItemCount;
    }

    public List<ShopItem> getItems() {
        return items;
    }

    public void add(ShopItem item) {
        items.add(item);
    }

    public void remove(ShopItem item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
    }

    public void save() {

    }

    private ShopItem createItem(ByteBuffer buffer) {

        int id = buffer.getInt32();
        int modelId = buffer.getInt32();
        int category = buffer.getInt32();
        int priceCategory = buffer.getByte();
        int a = buffer.getByte();
        int b = buffer.getInt16();
        int price = buffer.getInt32();

        // Item name is a fixed length of 44bytes
        String name = buffer.getStringNulTerminated();
        buffer.setCurrentPos(64);

        int d11 = buffer.getByte();
        int d111 = buffer.getByte();

        int c11 = buffer.getByte();
        int c111 = buffer.getByte();


        int d = buffer.getInt16();
        int d1 = buffer.getInt16();

        int e = buffer.getInt16();
        int e1 = buffer.getInt16();

        int f = buffer.getInt16();
        int f1 = buffer.getInt16();

        int g = buffer.getInt32();
        int h = buffer.getInt32();
        int i = buffer.getInt32();
        int j = buffer.getInt32();

        int k = buffer.getInt32();
        int l = buffer.getInt32();
        int m = buffer.getInt32();
        int n = buffer.getInt32();

        int o = buffer.getInt32();
        int p = buffer.getInt32();
        int minLevel = buffer.getInt32();
        int duration = buffer.getInt32();
        int quantity = buffer.getInt32();
        int q = buffer.getInt32();
        int weddingRing = buffer.getInt16();
        int test = buffer.getByte();
        int sex = buffer.getByte();
        int r = buffer.getInt32();
        int s = buffer.getInt32();
        int t = buffer.getInt32();

        if (weddingRing > 0) {
            int hh = 1;
        }

        ShopItem item = new ShopItem();
        item.setId(id);
        item.setModelId(modelId);
        item.setCategory(ItemCategoryType.getType(category));
        item.setPriceCategory(ItemPriceCategoryType.getType(priceCategory));
        item.setPrice(price);
        item.setName(name);
        item.setMinLevel(minLevel);
        item.setDuration(ItemDurationType.getType(duration));
        item.setQuantity(ItemQuantityType.getType(quantity));
        item.setSex(ItemSexType.getType(sex));
        item.setWeddingRing(weddingRing > 0);

        return item;
    }

    private byte[] decrypt(byte[] block) {
        for (int i = 0; i < block.length; i++) {
            int tmp = block[i];
            tmp = (0x1f9 - tmp) & 0x800000ff;
            if (tmp < 0) {
                tmp--;
                tmp |= 0x0ffffff00;
                tmp++;
            }
            block[i] = (byte) (tmp & 0xff);
        }
        return block;
    }
}
