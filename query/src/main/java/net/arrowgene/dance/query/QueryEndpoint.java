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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.account.AccountStateType;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.log.LogType;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import javax.crypto.SecretKey;
import java.io.IOException;


public abstract class QueryEndpoint implements HttpRequestHandler {

    private static final int BEARER_LENGTH = 6;
    protected static final SecretKey QUERY_KEY = MacProvider.generateKey();

    protected DanceServer server;

    public QueryEndpoint(DanceServer server) {
        this.server = server;
    }

    public abstract String getRoute();

    /**
     * Specify the minimum access level for this route.
     * Returning null allows anyone who authenticated to access this route.
     */
    public abstract AccountStateType getAuthorization();

    /**
     * Specify if the route requires authentication.
     * Authenticated
     */
    public abstract boolean isAuthenticatedRoute();


    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        QueryRequest queryRequest = new QueryRequest(httpRequest, httpResponse, httpContext, this.getRoute(), this.server.getLogger());
        if (this.isAuthenticatedRoute()) {
            String authorizationValue = queryRequest.getRequestHeaders().get("Authorization");
            if (authorizationValue != null) {
                authorizationValue = authorizationValue.substring(BEARER_LENGTH);
                String jwt = authorizationValue.trim();
                if (this.isServer(jwt, queryRequest)) {
                    this.handleRequest(queryRequest);
                } else {
                    boolean authenticated = this.authenticate(jwt, queryRequest);
                    if (authenticated) {
                        boolean authorized = this.authorize(queryRequest);
                        if (authorized) {
                            this.handleRequest(queryRequest);
                        } else {
                            queryRequest.respondNotAuthorized("Insufficient Rights");
                        }
                    } else {
                        queryRequest.respondNotAuthorized("Invalid JWT");
                    }
                }
            } else {
                queryRequest.respondNotAuthorized("Empty Authorization header");
            }
        } else {
            this.handleRequest(queryRequest);
        }
    }

    protected void handleGet(QueryRequest queryRequest) {
        queryRequest.respondNotFound();
    }

    protected void handlePost(QueryRequest queryRequest) {
        queryRequest.respondNotFound();
    }

    protected void handleDelete(QueryRequest queryRequest) {
        queryRequest.respondNotFound();
    }

    protected void handlePut(QueryRequest queryRequest) {
        queryRequest.respondNotFound();
    }

    private void handleRequest(QueryRequest queryRequest) {
        switch (queryRequest.getMethod()) {
            case "POST":
                handlePost(queryRequest);
                break;
            case "GET":
                handleGet(queryRequest);
                break;
            case "PUT":
                handlePut(queryRequest);
                break;
            case "DELETE":
                handleDelete(queryRequest);
                break;
            default:
                queryRequest.respondError();
                break;
        }
    }

    private boolean isServer(String key, QueryRequest queryRequest) {
        if (key.equals(this.server.getServerConfig().getQueryMasterKey())) {
            if (queryRequest.getHost().equals(this.server.getServerConfig().getQueryMasterHost())) {
                return true;
            }
            this.server.getLogger().writeLog(LogType.HACK,
                "QueryEndpoint",
                "authenticate",
                "Got query master key from an untrusted source (" + queryRequest.getHost() + "), consider changing the key."
            );
        }
        return false;
    }


    private boolean authenticate(String jwt, QueryRequest queryRequest) {
        try {
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(QueryEndpoint.QUERY_KEY)
                .parseClaimsJws(jwt);

            int accountId = Integer.parseInt(claims.getBody().getSubject());
            Account account = this.server.getDatabase().getAccount(accountId);
            if (account != null) {
                queryRequest.setAccount(account);
                return true;
            }
        } catch (SignatureException e) {
            //don't trust the JWT!
        } catch (Exception e) {
            this.server.getLogger().writeLog(e);
        }
        return false;
    }

    private boolean authorize(QueryRequest queryRequest) {
        AccountStateType authorization = this.getAuthorization();
        if (authorization != null) {
            Account account = queryRequest.getAccount();
            if (account != null && account.getState() != null) {
                if (account.getState().getNumValue() >= authorization.getNumValue()) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

}
