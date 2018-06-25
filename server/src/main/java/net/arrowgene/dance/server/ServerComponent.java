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

package net.arrowgene.dance.server;

import net.arrowgene.dance.database.Database;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.log.Logger;

/**
 * A ServerComponent has the responsibility of managing a single self containing part of the server.
 * (For example a ServerComponent can manage all Character-objects, or all Mail-objects.)
 * The ServerComponent should abstract the database access for the specific object it manages.
 * The instance will keep frequently used objects in memory, to reduce database read/write calls.
 * It has to ensure that the data is written and loaded from/to the database on the defined hooks (load/save).
 */
public abstract class ServerComponent {

    private int priority;
    protected DanceServer server;

    public ServerComponent(DanceServer server) {
        this.server = server;
        this.priority = 0;
    }

    public abstract void load();

    public abstract void save();

    public abstract void start();

    public abstract void stop();

    /**
     * Occurs after a client has authenticated itself.
     */
    public abstract void clientAuthenticated(DanceClient client);

    /**
     * Occurs when a client was disconnected, either by the server or the client.
     */
    public abstract void clientDisconnected(DanceClient client);

    /**
     * Occurs when the client established a TCP connection.
     * <p>
     * Note: This does not grantee that this is a valid client!
     * Use {@link #clientAuthenticated(DanceClient client)} instead.
     * <p>
     * Note: There is a bug where a clientConnected call happens,
     * but a clientDisconnected is never called!
     * Use {@link #clientAuthenticated(DanceClient client)} if,
     * you require to detect a disconnect.
     */
    public abstract void clientConnected(DanceClient client);

    /**
     * Writes additional debug information to the logs.
     * This will only be called if the server is in debug mode,
     * or manually requested by calling the function.
     */
    public abstract void writeDebugInfo();

    /**
     * Always returns the current database instance of the server.
     */
    protected Database getDatabase() {
        return this.server.getDatabase();
    }

    /**
     * Always returns the current logger instance of the server.
     */
    protected ServerLogger getLogger() {
        return this.server.getLogger();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
