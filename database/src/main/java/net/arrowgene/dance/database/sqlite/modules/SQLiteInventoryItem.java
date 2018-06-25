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

package net.arrowgene.dance.database.sqlite.modules;

import net.arrowgene.dance.database.sqlite.SQLiteController;
import net.arrowgene.dance.database.sqlite.SQLiteFactory;
import net.arrowgene.dance.library.models.item.InventoryItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteInventoryItem {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteInventoryItem(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public InventoryItem getInventoryItem(int inventoryId) throws SQLException {
        InventoryItem item = null;
        PreparedStatement select = this.controller.createPreparedStatement(
            "SELECT * FROM [ag_inventory] JOIN [ag_item] USING (item_id) WHERE [inventory_id]=?");
        select.setInt(1, inventoryId);
        ResultSet res = select.executeQuery();
        if (res.next()) {
            item = this.factory.createInventoryItem(res);
        }
        res.close();
        select.close();
        return item;
    }

    public void insertInventoryItem(InventoryItem item) throws SQLException {

        if (item.getId() > -1) {
            PreparedStatement update = this.controller.createPreparedStatement("UPDATE [ag_inventory] SET "
                + "[character_id]=?," + "[item_id]=?," + "[inventory_slot_number]=?," + "[inventory_quantity]=?,"
                + "[inventory_expire_date]=?," + "[inventory_is_equipped]=? " + "WHERE [inventory_id]=?;");
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
            PreparedStatement insert = this.controller
                .createPreparedStatement("INSERT INTO [ag_inventory] VALUES (?,?,?,?,?,?,?);");
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
        PreparedStatement delete = this.controller
            .createPreparedStatement("DELETE FROM [ag_inventory] WHERE [inventory_id]=?");
        delete.setInt(1, inventoryId);
        delete.execute();
        delete.close();
    }

    public void deleteInventoryItems(List<InventoryItem> items) throws SQLException {
        PreparedStatement delete = this.controller
            .createPreparedStatement("DELETE FROM [ag_inventory] WHERE [inventory_id]=?");

        for (InventoryItem item : items) {
            delete.clearParameters();
            delete.setInt(1, item.getId());
            delete.execute();
        }

        delete.close();
    }

    public List<InventoryItem> getInventoryItems(int characterId) throws SQLException {
        List<InventoryItem> items = new ArrayList<InventoryItem>();

        PreparedStatement select = this.controller.createPreparedStatement(
            "SELECT * FROM [ag_inventory] JOIN [ag_item] USING (item_id) WHERE [character_id]=?");
        select.setInt(1, characterId);
        ResultSet res = select.executeQuery();
        while (res.next()) {
            InventoryItem item = this.factory.createInventoryItem(res);
            items.add(item);
        }

        res.close();
        select.close();

        return items;
    }

    public void insertInventoryItems(List<InventoryItem> items) throws SQLException {

        PreparedStatement insert = this.controller
            .createPreparedStatement("INSERT INTO [ag_inventory] VALUES (?,?,?,?,?,?,?);");
        PreparedStatement update = this.controller.createPreparedStatement("UPDATE [ag_inventory] SET "
            + "[character_id]=?," + "[item_id]=?," + "[inventory_slot_number]=?," + "[inventory_quantity]=?,"
            + "[inventory_expire_date]=?," + "[inventory_is_equipped]=? " + "WHERE [inventory_id]=?;");

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
