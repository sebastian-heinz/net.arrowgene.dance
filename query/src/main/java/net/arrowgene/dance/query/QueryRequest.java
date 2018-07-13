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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.arrowgene.dance.library.models.account.Account;
import net.arrowgene.dance.query.models.QueryResponse;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryRequest {

    private static final Logger logger = LogManager.getLogger(QueryRequest.class);

    private String method;
    private Map<String, String> queryParameter;
    private Map<String, String> requestHeaders;
    private Map<String, String> responseHeaders;
    private String[] urlPaths;
    private String[] routePaths;
    private String route;
    private String[] urlParameter;
    private int responseHTTPCode;
    private String responseBody;
    private String requestRawBody;
    private JsonElement requestJson;
    private Account account;
    private String host;
    private HttpResponse httpResponse;

    public QueryRequest(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext, String route) {
        this.httpResponse = httpResponse;
        this.route = route;
        this.responseHeaders = new HashMap<>();
        this.requestHeaders = new HashMap<>();
        for (Header requestHeader : httpRequest.getAllHeaders()) {
            this.requestHeaders.put(requestHeader.getName(), requestHeader.getValue());
        }
        this.routePaths = this.pathToArray(this.route);
        this.method = httpRequest.getRequestLine().getMethod();
        this.queryParameter = this.queryToMap(httpRequest.getRequestLine().getUri());
        this.urlPaths = this.pathToArray(httpRequest.getRequestLine().getUri());
        this.urlParameter = this.getUrlParameter(this.urlPaths, this.routePaths);
        this.requestRawBody = this.getRequestBody(httpRequest);
        this.requestJson = this.getJson(this.requestRawBody);
        this.responseHTTPCode = 0;
        HttpInetConnection connection = (HttpInetConnection) httpContext.getAttribute(HttpCoreContext.HTTP_CONNECTION);
        this.host = connection.getRemoteAddress().getHostName();
    }

    public String getHost() {
        return host;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getResponseHTTPCode() {
        return this.responseHTTPCode;
    }

    public void setResponseHTTPCode(int responseHTTPCode) {
        this.responseHTTPCode = responseHTTPCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void addResponseHeader(String key, String value) {
        this.responseHeaders.put(key, value);
    }

    public String getRequestRawBody() {
        return requestRawBody;
    }

    public JsonElement getRequestJson() {
        return requestJson;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getQueryParameter() {
        return queryParameter;
    }

    public String[] getUrlPaths() {
        return urlPaths;
    }

    public String[] getRoutePaths() {
        return routePaths;
    }

    public String getRoute() {
        return route;
    }

    public String[] getUrlParameter() {
        return urlParameter;
    }

    public byte[] getResponseBytes() {
        return this.responseBody.getBytes(Charset.forName("UTF-8"));
    }

    public <T> T getJsonModel(Class clazz) {
        Gson gson = new Gson();
        T model = null;
        try {
            Object obj = gson.fromJson(this.getRequestRawBody(), clazz);
            model = (T) obj;
        } catch (Exception e) {
            logger.error(e);
        }
        return model;
    }

    public void respondNotAuthorized() {
        this.respondNotAuthorized(null);
    }

    public void respondNotAuthorized(String reason) {
        Gson gson = new Gson();

        String message = "Not Authorized";
        if (reason != null) {
            message += " " + reason;
        }

        String responseJson = gson.toJson(new QueryResponse(401, message));

        this.addResponseHeader("Content-Type", "application/json");
        this.setResponseHTTPCode(HttpStatus.SC_UNAUTHORIZED);
        this.setResponseBody(responseJson);

        this.respond();
    }

    public void respondNotFound() {
        Gson gson = new Gson();
        String responseJson = gson.toJson(new QueryResponse(404, "Not Found"));

        this.addResponseHeader("Content-Type", "application/json");
        this.setResponseHTTPCode(HttpStatus.SC_NOT_FOUND);
        this.setResponseBody(responseJson);

        this.respond();
    }

    public void respondError() {
        Gson gson = new Gson();
        String responseJson = gson.toJson(new QueryResponse(500, "Internal Error"));

        this.addResponseHeader("Content-Type", "application/json");
        this.setResponseHTTPCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        this.setResponseBody(responseJson);

        this.respond();
    }

    public void respondJsonModel(Object model) {
        if (model != null) {
            Gson gson = new Gson();
            String responseJson = gson.toJson(model);
            this.respondJson(responseJson);
        } else {
            logger.warn("Sending null object");
            respondError();
        }
    }

    public void respondJson(String responseJson) {
        // Not null-instance, Not "null" from gson convert and minimum json length of 2 ({})
        if (responseJson != null && !responseJson.equals("null") && responseJson.length() > 1) {
            this.addResponseHeader("Content-Type", "application/json");
            this.setResponseHTTPCode(HttpStatus.SC_OK);
            this.setResponseBody(responseJson);
            this.respond();
        } else {
            logger.warn("Sending null or empty object");
            respondError();
        }
    }

    public void respond() {
        if (this.responseHTTPCode == 0) {
            logger.warn("No HTTP Code set");
            respondError();
        } else {
            for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
                this.httpResponse.addHeader(entry.getKey(), entry.getValue());
            }
            this.httpResponse.setStatusCode(this.getResponseHTTPCode());
            try {
                this.httpResponse.setEntity(new StringEntity(this.responseBody));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");

                String key = null;
                String value = null;
                try {
                    key = java.net.URLDecoder.decode(pair[0], "UTF-8");
                    if (pair.length > 1) {
                        value = java.net.URLDecoder.decode(pair[1], "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error(e);
                }

                if (key != null && value != null) {
                    result.put(key, value);
                } else if (key != null) {
                    result.put(key, "");
                }
            }
        }
        return result;
    }

    private String[] pathToArray(String path) {
        String[] result = new String[0];
        if (path != null) {
            if (path.charAt(0) == '/') {
                path = path.substring(1);
            }
            String[] pathParts = path.split("/");
            result = new String[pathParts.length];
            for (int i = 0; i < pathParts.length; i++) {
                String pathPart = pathParts[i];
                String part = null;
                try {
                    part = java.net.URLDecoder.decode(pathPart, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    logger.error(e);
                }
                if (part != null) {
                    result[i] = part;
                }
            }
        }
        return result;
    }

    private String[] getUrlParameter(String[] urlPaths, String[] routePaths) {
        List<String> results = new ArrayList<>();
        boolean scan = true;
        for (int i = 0; i < urlPaths.length; i++) {
            String urlPart = urlPaths[i];
            boolean found = false;
            if (scan) {
                for (int j = 0; j < routePaths.length; j++) {
                    String routePart = routePaths[j];
                    if (urlPart.equals(routePart)) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                scan = false;
                results.add(urlPart);
            }
        }

        return results.toArray(new String[0]);
    }

    private String getRequestBody(InputStream bodyStream) {
        String result = "";
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // TODO increase buffer if necessary?
            byte buf[] = new byte[4096];
            for (int n = bodyStream.read(buf); n > 0; n = bodyStream.read(buf)) {
                out.write(buf, 0, n);
            }
            result = new String(out.toByteArray(), "UTF-8");
        } catch (IOException e) {
            logger.error(e);
        } finally {
            try {
                bodyStream.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }

        return result;
    }

    private String getRequestBody(HttpRequest httpRequest) {
        HttpEntity entity = null;
        if (httpRequest instanceof HttpEntityEnclosingRequest)
            entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
        // For some reason, just putting the incoming entity into
        // the response will not work. We have to buffer the message.
        byte[] data;
        if (entity == null) {
            data = new byte[0];
        } else {
            try {
                data = EntityUtils.toByteArray(entity);
            } catch (IOException e) {
                data = new byte[0];
                e.printStackTrace();
            }
        }
        return new String(data);
    }


    private JsonElement getJson(String string) {
        // TODO check content type, only parse if json
        JsonElement json = null;
        try {
            json = new JsonParser().parse(string);
        } catch (Exception e) {
            logger.error(e);
        }
        return json;
    }

}
