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

package net.arrowgene.dance.query.v1;


import net.arrowgene.dance.library.models.account.AccountStateType;
import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.query.QueryEndpoint;
import net.arrowgene.dance.query.QueryRequest;
import net.arrowgene.dance.query.models.CharacterResponse;
import net.arrowgene.dance.query.models.CharactersOnlineResponse;
import net.arrowgene.dance.query.models.QueryResponse;
import net.arrowgene.dance.server.DanceServer;

import java.util.List;

/**
 * This is a public endpoint, and provides reduced, safe to public information about characters.
 */
public class CharacterEndpoint extends QueryEndpoint {

    public CharacterEndpoint(DanceServer server) {
        super(server);
    }

    @Override
    public String getRoute() {
        return "/api/v1/character";
    }

    @Override
    public AccountStateType getAuthorization() {
        return AccountStateType.MEMBER;
    }

    @Override
    public boolean isAuthenticatedRoute() {
        return true;
    }


    /**
     * /character/name/{name} - Get Character with name
     * /character/online - Get Online Character
     */
    @Override
    protected void handleGet(QueryRequest queryRequest) {
        if (queryRequest.getUrlParameter().length > 0) {
            String section = queryRequest.getUrlParameter()[0];
            if (section.equals("name")) {
                if (queryRequest.getUrlParameter().length > 1) {
                    String characterName = queryRequest.getUrlParameter()[1];
                    this.getCharacter(characterName, queryRequest);
                } else {
                    queryRequest.respondNotFound();
                }
            } else if (section.equals("online")) {
                this.getOnlineCharacter(queryRequest);
            } else {
                queryRequest.respondNotFound();
            }
        } else {
            queryRequest.respondNotFound();
        }
    }

    private void getCharacter(String characterName, QueryRequest queryRequest) {
        Character character = super.server.getCharacterManager().getCharacterByName(characterName);
        QueryResponse response = new QueryResponse();
        if (character != null) {
            response = new CharacterResponse(character);
            response.setStatusCode(200);
        } else {
            response.setStatusCode(404);
            response.setMessage("Character not found");
        }
        queryRequest.respondJsonModel(response);
    }

    private void getOnlineCharacter(QueryRequest queryRequest) {
        List<Character> characters = super.server.getCharacterManager().getOnlineCharacters();
        CharactersOnlineResponse response = new CharactersOnlineResponse();
        response.setCharacters(CharacterResponse.asList(characters));
        response.setStatusCode(200);
        queryRequest.respondJsonModel(response);
    }

}
