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


import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.account.AccountStateType;
import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.character.CharacterSexTyp;
import net.arrowgene.dance.query.QueryEndpoint;
import net.arrowgene.dance.query.QueryRequest;
import net.arrowgene.dance.query.models.*;
import net.arrowgene.dance.server.DanceServer;

import java.util.List;

/**
 * This is a privileged endpoint, and is not allowed to be used by normal users.
 * This endpoint allows to create, modify and delete Accounts and related entities.
 */
public class AccountEndpoint extends QueryEndpoint {

    public AccountEndpoint(DanceServer server) {
        super(server);
    }

    @Override
    public String getRoute() {
        return "/api/v1/account";
    }

    @Override
    public AccountStateType getAuthorization() {
        return AccountStateType.QUERY;
    }

    @Override
    public boolean isAuthenticatedRoute() {
        return true;
    }

    /**
     * /account/{name}/ - Get all Account info (Account, Characters)
     */
    @Override
    protected void handleGet(QueryRequest queryRequest) {
        if (queryRequest.getUrlParameter().length == 1) {
            String accountName = queryRequest.getUrlParameter()[0];
            Account account = super.server.getDatabase().getAccount(accountName);
            AccountResponse response = new AccountResponse();
            if (account != null) {
                List<Character> characters = super.server.getDatabase().getCharactersByUserId(account.getId());
                response.setCharacters(CharacterResponse.asList(characters, false));
                response.setName(account.getUsername());
                response.setState(account.getState().getNumValue());
                response.setActiveCharacterId(account.getActiveCharacterId());
                response.setStatusCode(200);
            } else {
                response.setStatusCode(404);
                response.setMessage("Account not found");
            }
            queryRequest.respondJsonModel(response);
        } else {
            queryRequest.respondNotFound();
        }
    }

    /**
     * /account/ - Create Account
     * /account/{name}/character/ - Create Character
     */
    @Override
    protected void handlePost(QueryRequest queryRequest) {
        if (queryRequest.getUrlParameter().length == 2) {
            String accountName = queryRequest.getUrlParameter()[0];
            String section = queryRequest.getUrlParameter()[1];
            if (section.equals("character")) {
                this.createCharacter(queryRequest, accountName);
            } else {
                queryRequest.respondNotFound();
            }
        } else if (queryRequest.getUrlParameter().length == 0) {
            this.createAccount(queryRequest);
        } else {
            queryRequest.respondNotFound();
        }
    }

    /**
     * /account/{name}/ - Update Account
     */
    @Override
    protected void handlePut(QueryRequest queryRequest) {
        if (queryRequest.getUrlParameter().length == 1) {
            AccountUpdateRequest request = queryRequest.getJsonModel(AccountUpdateRequest.class);
            String accountName = queryRequest.getUrlParameter()[0];
            Account account = super.server.getDatabase().getAccount(accountName);
            QueryResponse queryResponse = new QueryResponse();
            if (account != null) {
                boolean error = false;
                if (request.getActiveCharacterId() != null && request.getActiveCharacterId() > -1) {
                    List<Character> characters = super.server.getDatabase().getCharactersByUserId(account.getId());
                    Character ownsCharacter = null;
                    for (Character character : characters) {
                        if (character.getCharacterId() == request.getActiveCharacterId()) {
                            ownsCharacter = character;
                            break;
                        }
                    }
                    if (ownsCharacter != null) {
                        account.setActiveCharacterId(request.getActiveCharacterId());
                    } else {
                        error = true;
                        queryResponse.setStatusCode(403);
                        queryResponse.setMessage("No Character with specified activeCharacterId found.");
                    }
                }
                if (!error && request.getHash() != null && request.getHash().length() > 0) {
                    account.setPasswordHash(request.getHash());
                }
                if (!error) {
                    this.server.getDatabase().insertAccount(account);
                    queryResponse.setStatusCode(200);
                    queryResponse.setMessage("Account updated");
                }
            } else {
                queryResponse.setStatusCode(404);
                queryResponse.setMessage("Account not found");
            }
            queryRequest.respondJsonModel(queryResponse);
        } else {
            queryRequest.respondNotFound();
        }
    }

    /**
     * /account/{name}/character/{name}/ - Delete Character
     */
    @Override
    protected void handleDelete(QueryRequest queryRequest) {
        boolean handled = false;
        if (queryRequest.getUrlParameter().length == 3) {
            String accountName = queryRequest.getUrlParameter()[0];
            String section = queryRequest.getUrlParameter()[1];
            String characterName = queryRequest.getUrlParameter()[2];
            if (section.equals("character")) {
                handled = true;
                this.deleteCharacter(queryRequest, accountName, characterName);
            }
        }
        if (!handled) {
            queryRequest.respondNotFound();
        }
    }

    private void createAccount(QueryRequest queryRequest) {
        AccountCreateRequest request = queryRequest.getJsonModel(AccountCreateRequest.class);
        Account account = super.server.getDatabase().getAccount(request.getName());
        QueryResponse queryResponse = new QueryResponse();
        if (account == null) {
            Account newAccount = new Account();
            newAccount.setState(AccountStateType.MEMBER);
            newAccount.setPasswordHash(request.getHash());
            newAccount.setUsername(request.getName());
            this.server.getDatabase().insertAccount(newAccount);
            queryResponse.setStatusCode(200);
            queryResponse.setMessage("Account created");
        } else {
            queryResponse.setStatusCode(403);
            queryResponse.setMessage("Account already created");
        }
        queryRequest.respondJsonModel(queryResponse);
    }

    private void createCharacter(QueryRequest queryRequest, String accountName) {
        CharacterCreateRequest request = queryRequest.getJsonModel(CharacterCreateRequest.class);
        Character character = super.server.getCharacterManager().getCharacterByName(request.getName());
        QueryResponse response = new QueryResponse();
        if (character == null) {
            Account account = this.server.getDatabase().getAccount(accountName);
            if (account != null) {
                CharacterSexTyp sex = CharacterSexTyp.getType(request.getSex());
                if (sex == null) {
                    response.setStatusCode(500);
                    response.setMessage("Bad Property Sex (0=Female;1=Male)");
                } else {
                    Character newCharacter = super.server.getCharacterManager().createNewCharacter(account.getId(), request.getName(), sex);
                    response = new CharacterResponse(newCharacter, false);
                    response.setStatusCode(200);
                    response.setMessage("Character created");
                }
            } else {
                response.setStatusCode(404);
                response.setMessage("Account not found");
            }
        } else {
            response.setStatusCode(403);
            response.setMessage("Character name already exists");
        }
        queryRequest.respondJsonModel(response);
    }

    private void deleteCharacter(QueryRequest queryRequest, String accountName, String characterName) {
        QueryResponse response = new QueryResponse();
        response.setStatusCode(404);
        response.setMessage("Not Implemented");
        queryRequest.respondJsonModel(response);
    }

}
