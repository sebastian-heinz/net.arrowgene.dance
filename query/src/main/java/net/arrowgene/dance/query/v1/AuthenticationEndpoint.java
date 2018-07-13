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


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.library.models.account.AccountStateType;
import net.arrowgene.dance.query.QueryEndpoint;
import net.arrowgene.dance.query.QueryRequest;
import net.arrowgene.dance.query.models.AuthenticationRequest;
import net.arrowgene.dance.query.models.AuthenticationResponse;
import net.arrowgene.dance.server.DanceServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * JWT implementation: https://github.com/jwtk/jjwt
 */
public class AuthenticationEndpoint extends QueryEndpoint {

    private static final Logger logger = LogManager.getLogger(AuthenticationEndpoint.class);

    public AuthenticationEndpoint(DanceServer server) {
        super(server);
    }

    @Override
    public String getRoute() {
        return "/api/v1/authentication";
    }

    @Override
    public AccountStateType getAuthorization() {
        return null;
    }

    @Override
    public boolean isAuthenticatedRoute() {
        return false;
    }

    @Override
    protected void handlePost(QueryRequest queryRequest) {

        AuthenticationRequest authRequest = queryRequest.getJsonModel(AuthenticationRequest.class);
        AuthenticationResponse response = new AuthenticationResponse();

        if (authRequest.getUser() != null && authRequest.getHash() != null) {
            Account account = super.server.getDatabase().getAccount(authRequest.getUser(), authRequest.getHash());
            if (account != null) {

                Date now = new Date();
                Date expiration = new Date(now.getTime() + super.server.getServerConfig().getQueryJWTValidMS());

                try {
                    String jwt = Jwts.builder()
                        .setIssuedAt(now)
                        .setExpiration(expiration)
                        .setSubject(String.valueOf(account.getId()))
                        .signWith(SignatureAlgorithm.HS512, QueryEndpoint.QUERY_KEY)
                        .compact();

                    response = new AuthenticationResponse();
                    response.setJwt(jwt);
                    response.setStatusCode(200);
                } catch (Exception e) {
                    logger.error(e);
                }

            } else {
                response.setMessage("Wrong Username or Password");
            }
        } else {
            response.setMessage("Missing request parameter 'user' or 'hash' in json body");
        }

        queryRequest.respondJsonModel(response);
    }

}
