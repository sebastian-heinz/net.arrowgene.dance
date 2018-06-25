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
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.query.QueryEndpoint;
import net.arrowgene.dance.query.QueryRequest;
import net.arrowgene.dance.query.models.HealthResponse;

import java.util.Date;


public class HealthEndpoint extends QueryEndpoint {

    public HealthEndpoint(DanceServer server) {
        super(server);
    }

    @Override
    public String getRoute() {
        return "/api/v1/health";
    }

    @Override
    public AccountStateType getAuthorization() {
        return AccountStateType.QUERY;
    }

    @Override
    public boolean isAuthenticatedRoute() {
        return true;
    }

    @Override
    protected void handleGet(QueryRequest queryRequest) {
        int clientsOnline = this.server.getClientController().getClients().size();
        HealthResponse serverHealth = new HealthResponse();
        serverHealth.setClientsOnline(clientsOnline);
        serverHealth.setServerOnline(this.server.isOnline());
        serverHealth.setTime(new Date());
        serverHealth.setStatusCode(200);
        queryRequest.respondJsonModel(serverHealth);
    }


}
