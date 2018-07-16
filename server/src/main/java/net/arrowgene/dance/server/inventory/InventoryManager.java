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

package net.arrowgene.dance.server.inventory;


import net.arrowgene.dance.library.models.item.Inventory;
import net.arrowgene.dance.library.models.item.InventoryItem;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.client.DanceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager extends ServerComponent {


    private static final Logger logger = LogManager.getLogger(InventoryManager.class);

    private final Object inventoriesLock = new Object();

    private List<Inventory> inventories;

    public InventoryManager(DanceServer server) {
        super(server);
        this.inventories = new ArrayList<>();
    }

    @Override
    public void load() {

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
        if (client.getCharacter() != null) {
            Inventory inventory = this.getInventoryByCharacterId(client.getCharacter().getId());
            client.setInventory(inventory);
            client.loadEquippedItems();
        }
    }

    @Override
    public void clientDisconnected(DanceClient client) {
        if (client.getCharacter() != null) {
            super.getDatabase().syncInventory(client.getCharacter().getId(), client.getInventory().getItems());
        }
    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {
        logger.debug(String.format("Inventories: %d", inventories.size()));
    }

    public Inventory getInventoryByCharacterId(int characterId) {
        List<InventoryItem> items = super.getDatabase().getInventoryItems(characterId);

        Inventory inventory = new Inventory();
        inventory.addItems(items);

        return inventory;
    }


}
