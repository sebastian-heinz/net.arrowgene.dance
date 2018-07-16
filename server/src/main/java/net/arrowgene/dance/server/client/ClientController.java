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
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


public class ClientController extends ServerComponent {

    private static final Logger logger = LogManager.getLogger(ClientController.class);

    private final Object clientsLock = new Object();
    private ArrayList<DanceClient> clients;

    public ClientController(DanceServer server) {
        super(server);
        clients = new ArrayList<>();
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

    /**
     * Forces all clients to disconnect.
     * This should trigger the "clientDisconnected"-method and save the clients state.
     */
    @Override
    public void stop() {
        synchronized (clientsLock) {
            for (DanceClient client : clients) {
                client.disconnect();
            }
        }
    }

    @Override
    public void clientAuthenticated(DanceClient client) {
        AccountSettings settings = super.getDatabase().getSettings(client.getAccount().getId());
        client.setSettings(settings);
        synchronized (clientsLock) {
            clients.add(client);
        }
        logger.info(String.format("Authenticated (%s)", client));
    }

    @Override
    public void clientDisconnected(DanceClient client) {
        synchronized (clientsLock) {
            clients.remove(client);
        }
        logger.info(String.format("Disconnected (%s)", client));
        if (client.getRoom() != null) {
            client.getRoom().leave(client);
        }
        if (client.getChannel() != null) {
            client.getChannel().leave(client);
        }
        if (client.getSettings() != null) {
            super.getDatabase().insertSettings(client.getSettings());
        }
        super.getDatabase().insertFavoriteSongs(client.getFavoriteSongs());
    }

    @Override
    public void clientConnected(DanceClient client) {
        logger.info(String.format("Connected (%s)", client));
    }

    @Override
    public void writeDebugInfo() {
        synchronized (clientsLock) {
            logger.debug(String.format("Clients: %d", clients.size()));
        }
    }

    /**
     * @return The collection of currently connected clients.
     */
    public ArrayList<DanceClient> getClients() {
        ArrayList<DanceClient> clients;
        synchronized (clientsLock) {
            clients = new ArrayList<DanceClient>(this.clients);
        }
        return clients;
    }

    /**
     * Searches for a client with the given character name inside the
     * currently connected clients.
     *
     * @param characterName The name of the character.
     * @return The client object which is associated with the character or null on failure.
     */
    public DanceClient getClientByCharacterName(String characterName) {
        DanceClient result = null;
        ArrayList<DanceClient> clients = getClients();
        for (DanceClient client : clients) {
            Character character = client.getCharacter();
            if (character != null && character.getName().equals(characterName)) {
                result = client;
                break;
            }
        }
        return result;
    }

    /**
     * Searches for a client with the given character name inside the
     * currently connected clients.
     *
     * @param characterId The id of the character.
     * @return The client object which is associated with the character or null on failure.
     */
    public DanceClient getClientByCharacterId(int characterId) {
        DanceClient result = null;
        ArrayList<DanceClient> clients = getClients();
        for (DanceClient client : clients) {
            Character character = client.getCharacter();
            if (character != null && character.getId() == characterId) {
                result = client;
                break;
            }
        }
        return result;
    }

    public DanceClient getClientByAccountId(int accountId) {
        DanceClient result = null;
        ArrayList<DanceClient> clients = getClients();
        for (DanceClient client : clients) {
            if (client.getAccount().getId() == accountId) {
                Character character = client.getCharacter();
                if (character != null) {
                    result = client;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Searches for a client with the given account name inside the
     * currently connected clients.
     *
     * @param accountName The name of the account.
     * @return The client object which is associated with the account name or null on failure.
     */
    public DanceClient getClientByAccountName(String accountName) {
        DanceClient result = null;
        ArrayList<DanceClient> clients = getClients();
        for (DanceClient client : clients) {
            Account account = client.getAccount();
            if (account != null && account.getUsername().equals(accountName)) {
                result = client;
                break;
            }
        }
        return result;
    }

}
