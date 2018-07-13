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

package net.arrowgene.dance.server.packet.handle;

import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.packet.enums.LoginErrorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;


public class _1000_x3E8_LOGIN_REQUEST_AUTHENTICATION extends HandlerBase {


    private static final Logger logger = LogManager.getLogger(_1000_x3E8_LOGIN_REQUEST_AUTHENTICATION.class);

    private final Object loginRequestsLock = new Object();
    private HashSet<String> loginRequests;

    public _1000_x3E8_LOGIN_REQUEST_AUTHENTICATION(DanceServer server) {
        super(server);
        this.loginRequests = new HashSet<>();
    }

    private void freeRequest(String name) {
        synchronized (this.loginRequestsLock) {
            this.loginRequests.remove(name);
        }
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        boolean disconnectClient = false;
        SendPacket answerPacket = new SendPacket(PacketType.LOGIN_RESPONSE_AUTHENTICATION);
        if (server.isOnline()) {
            String username = packet.getStringNulTerminated();
            String password = packet.getString(32);
            int versionMajorClient = packet.getInt16();
            int versionMinorClient = packet.getInt16();
            boolean inTransit = false;
            synchronized (this.loginRequestsLock) {
                if (this.loginRequests.contains(username)) {
                    inTransit = true;
                } else {
                    this.loginRequests.add(username);
                }
            }
            if (!inTransit) {
                Account account = getDatabase().getAccount(username, password);
                if (account != null) {
                    switch (account.getState()) {
                        case MEMBER:
                        case MODERATOR:
                        case ADMIN:
                            DanceClient possibleActive = super.server.getClientController().getClientByAccountName(account.getUsername());
                            if (possibleActive == null) {
                                client.setAccount(account);
                                super.server.clientAuthenticated(client);
                                Character character = client.getCharacter();
                                if (character != null) {
                                    logger.info(String.format("Logged In (%s)", client));
                                    answerPacket.addInt32(LoginErrorType.MSG_NO_ERROR.getNumValue());
                                    answerPacket.addInt16(versionMajorClient);
                                    answerPacket.addInt16(versionMinorClient);
                                    if (character.isNewCharacter()) {
                                        answerPacket.addByte(0);
                                        answerPacket.addByte(1);
                                        answerPacket.addByte(1);
                                    } else {
                                        // Unknown Byte (00 from packet logs)
                                        answerPacket.addByte(0);
                                        // Possible  "new character byte"
                                        // Setting to 1 will trigger "LOGIN_REQUEST_CREATE_CHARACTER"
                                        // and Settings menu will be opened
                                        answerPacket.addByte(0);
                                        // Unknown Byte (01 from packet logs)
                                        answerPacket.addByte(1);
                                    }
                                    answerPacket.addStringNulTerminated(account.getUsername());
                                    // Unknown Byte (Default 01)
                                    answerPacket.addByte(1);
                                } else {
                                    // Multiple Characters can be created with the web frontend.
                                    // One of the Characters needs to be set as active.

                                    logger.warn(String.format("no character created or no character is set active in 'user_active_character_id' of 'ag_user' table (%s)", client));
                                    // TODO No Character created / active: Find a better error message for dance client
                                    answerPacket.addInt32(LoginErrorType.MSG_LOGIN_ERROR.getNumValue());
                                    disconnectClient = true;
                                }
                            } else {
                                logger.warn(String.format("tried to login while already online, disconnecting both (%s)", client));
                                //TODO Disconnect whoever is online? or not in case the person is in a active game?
                                possibleActive.disconnect();
                                answerPacket.addInt32(LoginErrorType.MSG_ALREADY_ONLINE.getNumValue());
                                disconnectClient = true;
                            }
                            break;
                        case BANNED:
                            answerPacket.addInt32(LoginErrorType.MSG_ACCOUNT_BANNED.getNumValue());
                            disconnectClient = true;
                            break;
                        default:
                            answerPacket.addInt32(LoginErrorType.MSG_LOGIN_ERROR.getNumValue());
                            disconnectClient = true;
                            break;
                    }
                } else {
                    answerPacket.addInt32(LoginErrorType.MSG_WRONG_PASSWORD.getNumValue());
                    logger.warn(String.format("Login with username '%s' failed, because no account was found for the credentials (%s)", username, client));
                    disconnectClient = true;
                }
                freeRequest(username);
            } else {
                logger.warn(String.format("Login with username '%s' failed, because a previous login request has not completed yet (%s)", username, client));
                answerPacket.addInt32(LoginErrorType.MSG_ALREADY_ONLINE.getNumValue());
                disconnectClient = true;
            }
        } else {
            logger.warn(String.format("Login failed, because the server is not ready yet (%s)", client));
            answerPacket.addInt32(LoginErrorType.MSG_OLD_CLIENT.getNumValue());
            disconnectClient = true;
        }
        answerPacket.addByte(0);
        client.sendPacket(answerPacket);
        if (disconnectClient) {
            client.disconnect();
        }
        return null;
    }
}
