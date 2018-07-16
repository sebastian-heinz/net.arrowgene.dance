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

package net.arrowgene.dance.server.character;


import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.character.SocialEntry;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.client.DanceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CharacterManager extends ServerComponent {

    private static final Logger logger = LogManager.getLogger(CharacterManager.class);

    private final Object charactersLock = new Object();

    private List<Character> characters;

    public CharacterManager(DanceServer server) {
        super(server);
        this.characters = new ArrayList<>();
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
        Character character = this.getCharacterById(client.getAccount().getId());
        if (character != null) {
            client.setCharacter(character);
            List<SocialEntry> buddies = super.getDatabase().getBuddies(client.getCharacter().getId());
            client.setBuddyList(buddies);
        }
    }

    @Override
    public void clientDisconnected(DanceClient client) {
        if (client.getCharacter() != null) {
            super.getDatabase().insertCharacter(client.getCharacter());
            super.getDatabase().syncBuddies(client.getCharacter().getId(), client.getBuddyList());
        }
    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {
        logger.debug(String.format("Characters: %d", characters.size()));
    }

    /**
     * Tries to get the Character-Object from a connected client.
     * On failure a second attempt will try to fetch the character from the database.
     *
     */
    public Character getCharacterByName(String characterName) {

        Character character = null;

        DanceClient client = super.server.getClientController().getClientByCharacterName(characterName);
        if (client != null && client.getCharacter() != null) {
            character = client.getCharacter();
        } else {
            character = super.getDatabase().getCharacter(characterName);
        }

        return character;
    }

    public Character getCharacterById(int characterId) {
        Character character = null;
        DanceClient client = super.server.getClientController().getClientByCharacterId(characterId);
        if (client != null && client.getCharacter() != null) {
            character = client.getCharacter();
        } else {
            character = super.getDatabase().getCharacter(characterId);
        }
        return character;
    }

}
