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

import net.arrowgene.dance.server.tcp.ServerType;
import net.arrowgene.dance.server.tcp.io.ClientManagerType;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class ServerConfig {

    public static final String EOL = System.lineSeparator();
    public static final int HOUR_MS = 3600 * 1000;
    public static final int MIN_MS = 60 * 1000;
    public static final int MIN_SEC = 60;

    /**
     * Directory to store database, logs etc.
     */
    private String logDirectory;

    /**
     * Port the listens for game connections.
     */
    private int port;

    /**
     * Host address of the server.
     */
    private InetAddress hostAddress;

    /**
     * Interval at which the server should save the logs to files.
     */
    private int logPeriodMin;

    /**
     * Interval at which the server should save the state to the database.
     */
    private int worldSavePeriodMin;

    /**
     * Defines the underlying tcp server to be used.
     */
    private ServerType serverType;

    /**
     * Defines the manager type.
     * Only relevant if server is run in {@code ServerType.IO} or this setting has no effect.
     */
    private ClientManagerType clientManagerType;

    /**
     * The port where the query server should listen on.
     */
    private int queryPort;

    /**
     * A key which the query server allows all requests from.
     */
    private String queryMasterKey;

    /**
     * Only accepts the query master key from this host.
     */
    private String queryMasterHost;

    /**
     * Defines how long a jwt is valid before the client needs to authenticate again.
     */
    private long queryJWTValidMS;

    /**
     * If known Packets should be logged.
     */
    private boolean logPackets;

    /**
     * Time after an inactive (AFK) client should be kicked.
     */
    private int maxAwaySeconds;

    /**
     * Time after a client will be disconnected if there has been no network activity.
     */
    private int maxNetworkInactivitySeconds;

    /**
     * Enables the debug mode.
     * <p>
     * - Additional logging.
     * - Additional server hardening to prevent it from crashing.
     * - Monitors for deadlocks.
     * <p>
     * This will probably slow the server down.
     */
    private boolean debugMode;

    private int debugWriteInfoMS;

    private long debugDetectDeadlockMS;

    private int debugGarbageCollectionMS;

    private int debugProcessInfoMS;

    /**
     * Number of threads working on processing client packet requests.
     */
    private int tpConsumerCount;

    /**
     * Number of threads polling if client have data to process.
     */
    private int tpProducerCount;

    /**
     * After a producer completed a request, it rests for the specified duration.
     * This allows to free CPU time.
     * A value of 0 or negative means it will keep polling as fast as it can.
     */
    private int tpProducerSleepMS;

    public ServerConfig() {
        File file = new File("./logs");
        file.mkdirs();

        this.logDirectory = file.getAbsolutePath();
        this.logPeriodMin = 30;

        // Game Server
        this.hostAddress = null;
        this.port = 2345;
        this.worldSavePeriodMin = 60;
        this.logPackets = true;
        this.maxAwaySeconds = 30 * MIN_SEC;
        this.serverType = ServerType.IO;
        this.clientManagerType = ClientManagerType.ThreadPool;

        // Thread Pool Specific
        this.maxNetworkInactivitySeconds = 2 * MIN_SEC;
        this.tpConsumerCount = 5;
        this.tpProducerCount = 2;
        this.tpProducerSleepMS = 100;

        // Query Server
        this.queryPort = 2346;
        this.queryMasterHost = "localhost";
        this.queryMasterKey = "TOPSECRET";
        this.queryJWTValidMS = 2 * HOUR_MS;

        // Debug
        this.debugMode = true;
        this.debugWriteInfoMS = 10 * MIN_MS;
        this.debugDetectDeadlockMS = 60 * MIN_MS;
        this.debugGarbageCollectionMS = 30 * MIN_MS;
        this.debugProcessInfoMS = 10 * MIN_MS;
    }

    public String getQueryMasterKey() {
        return queryMasterKey;
    }

    public void setQueryMasterKey(String queryMasterKey) {
        this.queryMasterKey = queryMasterKey;
    }

    public String getQueryMasterHost() {
        return queryMasterHost;
    }

    public void setQueryMasterHost(String queryMasterHost) {
        this.queryMasterHost = queryMasterHost;
    }

    public long getQueryJWTValidMS() {
        return queryJWTValidMS;
    }

    public void setQueryJWTValidMS(long queryJWTValidMS) {
        this.queryJWTValidMS = queryJWTValidMS;
    }

    public String getLogDirectory() {
        return logDirectory;
    }

    public void setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getQueryPort() {
        return queryPort;
    }

    public void setQueryPort(int queryPort) {
        this.queryPort = queryPort;
    }

    public int getLogPeriodMin() {
        return logPeriodMin;
    }

    public void setLogPeriodMin(int logPeriodMin) {
        this.logPeriodMin = logPeriodMin;
    }

    public int getWorldSavePeriodMin() {
        return worldSavePeriodMin;
    }

    public void setWorldSavePeriodMin(int worldSavePeriodMin) {
        this.worldSavePeriodMin = worldSavePeriodMin;
    }

    public InetAddress getHostAddress() {
        return this.hostAddress;
    }

    public void setHostAddress(InetAddress hostAddress) {
        this.hostAddress = hostAddress;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public ClientManagerType getClientManagerType() {
        return clientManagerType;
    }

    public void setClientManagerType(ClientManagerType clientManagerType) {
        this.clientManagerType = clientManagerType;
    }

    public int getDebugWriteInfoMS() {
        return debugWriteInfoMS;
    }

    public void setDebugWriteInfoMS(int debugWriteInfoMS) {
        this.debugWriteInfoMS = debugWriteInfoMS;
    }

    public long getDebugDetectDeadlockMS() {
        return debugDetectDeadlockMS;
    }

    public void setDebugDetectDeadlockMS(long debugDetectDeadlockMS) {
        this.debugDetectDeadlockMS = debugDetectDeadlockMS;
    }

    public int getDebugGarbageCollectionMS() {
        return debugGarbageCollectionMS;
    }

    public void setDebugGarbageCollectionMS(int debugGarbageCollectionMS) {
        this.debugGarbageCollectionMS = debugGarbageCollectionMS;
    }

    public int getDebugProcessInfoMS() {
        return debugProcessInfoMS;
    }

    public void setDebugProcessInfoMS(int debugProcessInfoMS) {
        this.debugProcessInfoMS = debugProcessInfoMS;
    }

    public boolean isLogPackets() {
        return logPackets;
    }

    public void setLogPackets(boolean logPackets) {
        this.logPackets = logPackets;
    }

    public int getMaxAwaySeconds() {
        return maxAwaySeconds;
    }

    public void setMaxAwaySeconds(int maxAwaySeconds) {
        this.maxAwaySeconds = maxAwaySeconds;
    }

    public int getMaxNetworkInactivitySeconds() {
        return maxNetworkInactivitySeconds;
    }

    public void setMaxNetworkInactivitySeconds(int maxNetworkInactivitySeconds) {
        this.maxNetworkInactivitySeconds = maxNetworkInactivitySeconds;
    }

    public int getTpConsumerCount() {
        return tpConsumerCount;
    }

    public void setTpConsumerCount(int tpConsumerCount) {
        this.tpConsumerCount = tpConsumerCount;
    }

    public int getTpProducerCount() {
        return tpProducerCount;
    }

    public void setTpProducerCount(int tpProducerCount) {
        this.tpProducerCount = tpProducerCount;
    }

    public int getTpProducerSleepMS() {
        return tpProducerSleepMS;
    }

    public void setTpProducerSleepMS(int tpProducerSleepMS) {
        this.tpProducerSleepMS = tpProducerSleepMS;
    }

    /**
     * Returns every current setting
     */
    public List<String> getCurrentConfiguration() {
        List<String> currentConfig = new ArrayList<>();
        currentConfig.add(String.format("logDirectory: %s", this.logDirectory));
        currentConfig.add(String.format("port: %s", this.port));
        currentConfig.add(String.format("hostAddress: %s", this.hostAddress));
        currentConfig.add(String.format("logPeriodMin: %s", this.logPeriodMin));
        currentConfig.add(String.format("worldSavePeriodMin: %s", this.worldSavePeriodMin));
        currentConfig.add(String.format("serverType: %s", this.serverType));
        currentConfig.add(String.format("clientManagerType: %s", this.clientManagerType));
        currentConfig.add(String.format("queryPort: %s", this.queryPort));
        // currentConfig.add(String.format("queryMasterKey %s", this.queryMasterKey));
        currentConfig.add(String.format("queryMasterHost: %s", this.queryMasterHost));
        currentConfig.add(String.format("queryJWTValidMS: %s", this.queryJWTValidMS));
        currentConfig.add(String.format("logPackets: %s", this.logPackets));
        currentConfig.add(String.format("maxAwaySeconds: %s", this.maxAwaySeconds));
        currentConfig.add(String.format("maxNetworkInactivitySeconds: %s", this.maxNetworkInactivitySeconds));
        currentConfig.add(String.format("debugMode: %s", this.debugMode));
        currentConfig.add(String.format("debugWriteInfoMS: %s", this.debugWriteInfoMS));
        currentConfig.add(String.format("debugDetectDeadlockMS: %s", this.debugDetectDeadlockMS));
        currentConfig.add(String.format("debugGarbageCollectionMS: %s", this.debugGarbageCollectionMS));
        currentConfig.add(String.format("debugProcessInfoMS: %s", this.debugProcessInfoMS));
        currentConfig.add(String.format("tpProducerSleepMS: %s", this.tpProducerSleepMS));
        currentConfig.add(String.format("tpProducerCount: %s", this.tpProducerCount));
        currentConfig.add(String.format("tpConsumerCount: %s", this.tpConsumerCount));
        return currentConfig;
    }
}
