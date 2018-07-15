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

package net.arrowgene.dance.database.maria.modules;

import net.arrowgene.dance.database.maria.MariaDbController;
import net.arrowgene.dance.database.maria.MariaDbFactory;
import net.arrowgene.dance.library.models.item.ShopItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaDbShopItem {

    private MariaDbController controller;
    private MariaDbFactory factory;

    public MariaDbShopItem(MariaDbController controller, MariaDbFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertShopItem(ShopItem item) throws SQLException {
        PreparedStatement insert = controller.createPreparedStatement("INSERT OR REPLACE INTO `dance_item` VALUES (?,?,?,?,?,?,?,?,?,?);");
        insert.setInt(1, item.getId());
        insert.setString(2, item.getName());
        insert.setInt(3, item.getModelId());
        insert.setInt(4, item.getCategory().getNumValue());
        insert.setLong(5, item.getMinLevel());
        insert.setInt(6, item.getDuration().getNumValue());
        insert.setInt(7, item.getPrice());
        insert.setInt(8, item.getQuantity().getNumValue());
        insert.setInt(9, item.getSex().getNumValue());
        insert.setInt(10, item.getPriceCategory().getNumValue());
        insert.execute();
        insert.close();
    }

    public void insertShopItems(List<ShopItem> items) throws SQLException {
        PreparedStatement insert = controller.createPreparedStatement("INSERT OR REPLACE INTO `dance_item` VALUES (?,?,?,?,?,?,?,?,?,?,?);");
        for (ShopItem item : items) {
            insert.clearParameters();
            insert.setInt(1, item.getId());
            insert.setString(2, item.getName());
            insert.setInt(3, item.getModelId());
            insert.setInt(4, item.getCategory().getNumValue());
            insert.setLong(5, item.getMinLevel());
            insert.setInt(6, item.getDuration().getNumValue());
            insert.setInt(7, item.getPrice());
            insert.setInt(8, item.getQuantity().getNumValue());
            insert.setInt(9, item.getSex().getNumValue());
            insert.setInt(10, item.getPriceCategory().getNumValue());
            insert.setBoolean(11, item.isWeddingRing());
            insert.execute();
        }
        insert.close();
    }

    public ShopItem getShopItem(int itemId) throws SQLException {
        ShopItem item = null;
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_item` WHERE `id`=?;");
        select.setInt(1, itemId);
        ResultSet res = select.executeQuery();
        if (res.next()) {
            item = factory.createShopItem(res);
        }
        res.close();
        select.close();
        return item;
    }

    public List<ShopItem> getShopItems() throws SQLException {
        List<ShopItem> items = new ArrayList<>();
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_item`;");
        ResultSet res = select.executeQuery();
        while (res.next()) {
            ShopItem item = factory.createShopItem(res);
            items.add(item);
        }
        res.close();
        select.close();
        return items;
    }

    public void deleteShopItem(int itemId) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM `ag_item` WHERE `item_id`=?");
        delete.setInt(1, itemId);
        delete.execute();
        delete.close();
    }
}
