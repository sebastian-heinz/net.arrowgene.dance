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
import net.arrowgene.dance.library.models.character.SocialEntry;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.log.LogType;

import java.util.ArrayList;
import java.util.List;


public class ClientController extends ServerComponent {

    private final Object clientsLock = new Object();
    private ArrayList<DanceClient> clients;


    public ClientController(DanceServer server) {
        super(server);
        this.clients = new ArrayList<>();
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
        synchronized (this.clientsLock) {
            for (DanceClient client : this.clients) {
                client.disconnect();
            }
        }
    }

    @Override
    public void clientAuthenticated(DanceClient client) {
        AccountSettings settings = super.getDatabase().getSettings(client.getAccount().getId());
        client.setSettings(settings);
        synchronized (this.clientsLock) {
            this.clients.add(client);
        }
        super.getLogger().writeLog(LogType.CLIENT, "Authenticated", client);
    }

    @Override
    public void clientDisconnected(DanceClient client) {
        synchronized (this.clientsLock) {
            this.clients.remove(client);
        }
        super.getLogger().writeLog(LogType.CLIENT, "Disconnected", client);

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
        super.getLogger().writeLog(LogType.CLIENT, "Connected", client);
    }

    @Override
    public void writeDebugInfo() {
        synchronized (this.clientsLock) {
            getLogger().writeLog(LogType.DEBUG, "ClientController", "writeDebugInfo", "Clients: " + this.clients.size());
        }
    }

    /**
     * @return The collection of currently connected clients.
     */
    public ArrayList<DanceClient> getClients() {
        ArrayList<DanceClient> clients;
        synchronized (this.clientsLock) {
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
        ArrayList<DanceClient> clients = this.getClients();
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
        ArrayList<DanceClient> clients = this.getClients();
        for (DanceClient client : clients) {
            Character character = client.getCharacter();
            if (character != null && character.getCharacterId() == characterId) {
                result = client;
                break;
            }
        }
        return result;
    }

    public DanceClient getClientByAccountId(int accountId) {
        DanceClient result = null;
        ArrayList<DanceClient> clients = this.getClients();
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
        ArrayList<DanceClient> clients = this.getClients();
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
