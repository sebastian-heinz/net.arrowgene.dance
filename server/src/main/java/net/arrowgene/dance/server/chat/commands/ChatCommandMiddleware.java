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

package net.arrowgene.dance.server.chat.commands;

import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.library.models.account.AccountStateType;
import net.arrowgene.dance.server.chat.ChatMessage;
import net.arrowgene.dance.server.chat.ChatMiddleware;
import net.arrowgene.dance.server.chat.ChatType;
import net.arrowgene.dance.server.chat.commands.commandos.*;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.HashMap;

/**
 * Handles chat commands
 */
public class ChatCommandMiddleware implements ChatMiddleware {

    private DanceServer server;
    private HashMap<String, ChatCommand> chatCommands;

    public ChatCommandMiddleware(DanceServer server) {
        this.server = server;
        this.chatCommands = new HashMap<String, ChatCommand>();
        this.loadChatCommands();
    }

    private void loadChatCommands() {
        this.chatCommands.clear();
        this.chatCommands.put("atm", new ATM());
        this.chatCommands.put("bm", new BannerMessage());
        this.chatCommands.put("wm", new WorldMessage());
        this.chatCommands.put("r", new RightsManagement());
        this.chatCommands.put("p", new PacketSender());
        this.chatCommands.put("cm", new CharacterMod());
        this.chatCommands.put("sc", new ServerCommand());
        this.chatCommands.put("ch", new ChannelManagement());
    }

    @Override
    public void handleMessage(ChatMessage chatMessage) {
        boolean deliver = this.parseMessage(chatMessage.getMessage(), chatMessage.getSender(), chatMessage.getChatType());
        if (!deliver) {
            chatMessage.StopDelivery();
        }
    }

    private boolean parseMessage(String message, DanceClient danceClient, ChatType type) {
        boolean forward = true;

        if (danceClient.getAccount().getState() == AccountStateType.ADMIN) {
            if (message.substring(0, 1).equals("/")) {
                forward = false;
                this.doChatCommand(danceClient, message.substring(1), type);
            }
        }

        ///Player Commands For Testing Phase, giving Players a way to modify the character
        else if (message.length() > 8 && message.substring(0, 8).equals("//gender")) {
            String[] gender = message.split(" ");
            if (gender.length > 1) {
                this.doChatCommand(danceClient, "cm gender " + gender[1], type);
                this.server.getDatabase().insertCharacter(danceClient.getCharacter());
                forward = false;
            }
        } else if (message.length() > 8 && message.substring(0, 8).equals("//weight")) {
            String[] weight = message.split(" ");
            if (weight.length > 1) {
                this.doChatCommand(danceClient, "cm weight " + weight[1], type);
                this.server.getDatabase().insertCharacter(danceClient.getCharacter());
                forward = false;
            }
        } else if (message.length() > 7 && message.substring(0, 7).equals("//coins")) {
            String[] coins = message.split(" ");
            if (coins.length > 1) {
                this.doChatCommand(danceClient, "cm coins " + coins[1], type);
                this.server.getDatabase().insertCharacter(danceClient.getCharacter());
                forward = false;
            }
        } else if (message.length() > 7 && message.substring(0, 7).equals("//bonus")) {
            String[] bonus = message.split(" ");
            if (bonus.length > 1) {
                this.doChatCommand(danceClient, "cm bonus " + bonus[1], type);
                this.server.getDatabase().insertCharacter(danceClient.getCharacter());
                forward = false;
            }
        } else if (message.length() > 7 && message.substring(0, 7).equals("//level")) {
            String[] bonus = message.split(" ");
            if (bonus.length > 1) {
                this.doChatCommand(danceClient, "cm level " + bonus[1], type);
                this.server.getDatabase().insertCharacter(danceClient.getCharacter());
                forward = false;
            }
        }
        ///End Player Commands

        return forward;
    }

    private void doChatCommand(DanceClient danceClient, String text, ChatType type) {

        String[] command = text.split(" ");

        if (this.chatCommands.containsKey(command[0])) {
            String[] response = this.chatCommands.get(command[0]).exec(command, danceClient, this.server);

            if (response != null) {
                for (String r : response) {
                    PacketType pType = PacketType.LOBBY_RESPONSE_MESSAGE_CHANNEL;
                    if (type == ChatType.CHANNEL) {
                        pType = PacketType.LOBBY_RESPONSE_MESSAGE_CHANNEL;
                    } else if (type == ChatType.ROOM) {
                        pType = PacketType.ROOM_RESPONSE_MESSAGE_ROOM;
                    }
                    Packet answerPacket = new SendPacket(pType);
                    answerPacket.addStringNulTerminated(command[0]);
                    answerPacket.addStringNulTerminated(r);
                    answerPacket.addByte(0);
                    danceClient.sendPacket(answerPacket);
                }
            }
        } else {
            PacketType pType = PacketType.LOBBY_RESPONSE_MESSAGE_CHANNEL;
            if (type == ChatType.CHANNEL) {
                pType = PacketType.LOBBY_RESPONSE_MESSAGE_CHANNEL;
            } else if (type == ChatType.ROOM) {
                pType = PacketType.ROOM_RESPONSE_MESSAGE_ROOM;
            }
            Packet answerPacket = new SendPacket(pType);
            answerPacket.addStringNulTerminated("SYSTEM");
            answerPacket.addStringNulTerminated("Command not found.");
            answerPacket.addByte(0);
            danceClient.sendPacket(answerPacket);
        }

    }
}

