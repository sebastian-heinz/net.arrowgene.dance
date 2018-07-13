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

package net.arrowgene.dance.server.chat;

import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.chat.commands.ChatCommandMiddleware;
import net.arrowgene.dance.server.chat.filter.ChatFilterMiddleware;
import net.arrowgene.dance.server.chat.logger.ChatLoggerMiddleware;
import net.arrowgene.dance.server.client.DanceClient;

import java.util.ArrayList;

public class ChatManager extends ServerComponent {

    private ArrayList<ChatMiddleware> middleware;


    public ChatManager(DanceServer server) {
        super(server);
        this.middleware = new ArrayList<>();
    }

    /**
     * Loads middleware to be applied on chat messages.
     * Be mindful of the order:
     * Logger should be first to be able to log the raw message.
     * Filter should be last, as it changes the message.
     */
    @Override
    public void load() {
        this.middleware.clear();
        this.middleware.add(new ChatLoggerMiddleware());
        this.middleware.add(new ChatCommandMiddleware(super.server));
        this.middleware.add(new ChatFilterMiddleware());
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

    }

    @Override
    public void clientDisconnected(DanceClient client) {

    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {

    }

    public void handleMessage(ChatMessage chatMessage) {
        for (ChatMiddleware middleware : this.middleware) {
            middleware.handleMessage(chatMessage);
        }
    }

}
