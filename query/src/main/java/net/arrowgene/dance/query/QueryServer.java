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

package net.arrowgene.dance.query;

import net.arrowgene.dance.server.DanceServer;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryServer {

    private static final Logger logger = LogManager.getLogger(QueryServer.class);


    private List<QueryEndpoint> queryEndpoints;
    private HttpServer queryServer;
    private DanceServer server;

    public QueryServer(DanceServer server) {
        this.queryEndpoints = new ArrayList<>();
        this.server = server;
    }

    public void start() {
        this.loadEndpoints();
        int port = this.server.getServerConfig().getQueryPort();
        ServerBootstrap bootstrap = ServerBootstrap.bootstrap();
        for (QueryEndpoint endpoint : this.queryEndpoints) {
            bootstrap.registerHandler(endpoint.getRoute(), endpoint);
            bootstrap.registerHandler(endpoint.getRoute() + "/*", endpoint);
        }
        this.queryServer = bootstrap
            .setListenerPort(port)
            .create();
        try {
            queryServer.start();
            logger.info(String.format("Query Server started on port: %d", port));
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void loadEndpoints() {
        this.queryEndpoints.clear();
        this.queryEndpoints.add(new net.arrowgene.dance.query.v1.CatchAllEndpoint(this.server));
        this.queryEndpoints.add(new net.arrowgene.dance.query.v1.HealthEndpoint(this.server));
        this.queryEndpoints.add(new net.arrowgene.dance.query.v1.AuthenticationEndpoint(this.server));
        this.queryEndpoints.add(new net.arrowgene.dance.query.v1.AccountEndpoint(this.server));
        this.queryEndpoints.add(new net.arrowgene.dance.query.v1.CharacterEndpoint(this.server));
    }

    public void stop() {
        this.queryServer.stop();
    }
}
