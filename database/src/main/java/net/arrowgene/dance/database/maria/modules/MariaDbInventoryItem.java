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
import net.arrowgene.dance.library.models.item.InventoryItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaDbInventoryItem {

    private MariaDbController controller;
    private MariaDbFactory factory;

    public MariaDbInventoryItem(MariaDbController controller, MariaDbFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public InventoryItem getInventoryItem(int inventoryId) throws SQLException {
        InventoryItem item = null;
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_inventory` INNER JOIN `dance_item` ON dance_item.id = dance_inventory.item_id WHERE `id`=?");
        select.setInt(1, inventoryId);
        ResultSet res = select.executeQuery();
        if (res.next()) {
            item = factory.createInventoryItem(res);
        }
        res.close();
        select.close();
        return item;
    }

    public void insertInventoryItem(InventoryItem item) throws SQLException {
        if (item.getId() > -1) {
            PreparedStatement update = controller.createPreparedStatement("UPDATE `dance_inventory` SET "
                + "`character_id`=?," + "`item_id`=?," + "`slot_number`=?," + "`quantity`=?,"
                + "`expire_date`=?," + "`is_equipped`=? " + "WHERE `id`=?;");
            update.clearParameters();
            update.setInt(1, item.getCharacterId());
            update.setInt(2, item.getShopItem().getId());
            update.setInt(3, item.getSlotNumber());
            update.setInt(4, item.getQuantity());
            update.setLong(5, item.getExpireDate());
            update.setInt(6, item.isEquipped() ? 1 : 0);
            update.setInt(7, item.getId());
            update.execute();
            update.close();
        } else {
            PreparedStatement insert = controller.createPreparedStatement("INSERT INTO `dance_inventory` VALUES (?,?,?,?,?,?,?);");
            insert.clearParameters();
            insert.setInt(2, item.getCharacterId());
            insert.setInt(3, item.getShopItem().getId());
            insert.setInt(4, item.getSlotNumber());
            insert.setInt(5, item.getQuantity());
            insert.setLong(6, item.getExpireDate());
            insert.setInt(7, item.isEquipped() ? 1 : 0);
            insert.execute();
            insert.close();
        }
    }

    public void deleteInventoryItem(int inventoryId) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM `dance_inventory` WHERE `id`=?");
        delete.setInt(1, inventoryId);
        delete.execute();
        delete.close();
    }

    public void deleteInventoryItems(List<InventoryItem> items) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM `dance_inventory` WHERE `id`=?");
        for (InventoryItem item : items) {
            delete.clearParameters();
            delete.setInt(1, item.getId());
            delete.execute();
        }
        delete.close();
    }

    public List<InventoryItem> getInventoryItems(int characterId) throws SQLException {
        List<InventoryItem> items = new ArrayList<>();
        PreparedStatement select = controller.createPreparedStatement("SELECT * FROM `dance_inventory` INNER JOIN `dance_item` ON dance_item.id = dance_inventory.item_id WHERE `character_id`=?");
        select.setInt(1, characterId);
        ResultSet res = select.executeQuery();
        while (res.next()) {
            InventoryItem item = factory.createInventoryItem(res);
            items.add(item);
        }
        res.close();
        select.close();
        return items;
    }

    public void insertInventoryItems(List<InventoryItem> items) throws SQLException {
        PreparedStatement insert = controller.createPreparedStatement("INSERT INTO `dance_inventory` VALUES (?,?,?,?,?,?,?);");
        PreparedStatement update = controller.createPreparedStatement("UPDATE `dance_inventory` SET "
            + "`character_id`=?," + "`item_id`=?," + "`slot_number`=?," + "`quantity`=?,"
            + "`expire_date`=?," + "`is_equipped`=? " + "WHERE `id`=?;");
        for (InventoryItem item : items) {
            if (item.getId() > -1) {
                update.clearParameters();
                update.setInt(1, item.getCharacterId());
                update.setInt(2, item.getShopItem().getId());
                update.setInt(3, item.getSlotNumber());
                update.setInt(4, item.getQuantity());
                update.setLong(5, item.getExpireDate());
                update.setInt(6, item.isEquipped() ? 1 : 0);
                update.setInt(7, item.getId());
                update.execute();
            } else {
                insert.clearParameters();
                insert.setInt(2, item.getCharacterId());
                insert.setInt(3, item.getShopItem().getId());
                insert.setInt(4, item.getSlotNumber());
                insert.setInt(5, item.getQuantity());
                insert.setLong(6, item.getExpireDate());
                insert.setInt(7, item.isEquipped() ? 1 : 0);
                insert.execute();
            }
        }
        update.close();
        insert.close();
    }
}
