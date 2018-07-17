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

import net.arrowgene.dance.database.DatabaseType;
import net.arrowgene.dance.server.tcp.ServerType;
import net.arrowgene.dance.server.tcp.io.ClientManagerType;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class ServerConfig {

    public static final String EOL = System.lineSeparator();
    public static final int HOUR_MS = 3600 * 1000;
    public static final int MIN_MS = 60 * 1000;
    public static final int MIN_SEC = 60;

    /**
     * Port the listens for game connections.
     */
    private int port;

    /**
     * Host address of the server.
     */
    private InetAddress hostAddress;

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

    private DatabaseType databaseType;

    private String mariaDbHost;
    private short mariaDbPort;
    private String mariaDbDatabase;
    private String mariaDbUser;
    private String mariaDbPassword;
    private boolean mariaDbPool;
    private int mariaDbTimeout;

    public ServerConfig() {

        // Game Server
        hostAddress = null;
        port = 2345;
        worldSavePeriodMin = 60;
        logPackets = true;
        maxAwaySeconds = 30 * MIN_SEC;
        serverType = ServerType.IO;
        clientManagerType = ClientManagerType.ThreadPool;

        // Thread Pool Specific
        maxNetworkInactivitySeconds = 2 * MIN_SEC;
        tpConsumerCount = 5;
        tpProducerCount = 2;
        tpProducerSleepMS = 100;

        // Database
        databaseType = DatabaseType.SQLite;
        mariaDbHost = "localhost";
        mariaDbPort = 3306;
        mariaDbDatabase = "arrowgene";
        mariaDbUser = "root";
        mariaDbPassword = "";
        mariaDbTimeout = 0;
        mariaDbPool = false;

        // Debug
        debugMode = true;
        debugWriteInfoMS = 10 * MIN_MS;
        debugDetectDeadlockMS = 60 * MIN_MS;
        debugGarbageCollectionMS = 30 * MIN_MS;
        debugProcessInfoMS = 10 * MIN_MS;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWorldSavePeriodMin() {
        return worldSavePeriodMin;
    }

    public void setWorldSavePeriodMin(int worldSavePeriodMin) {
        this.worldSavePeriodMin = worldSavePeriodMin;
    }

    public InetAddress getHostAddress() {
        return hostAddress;
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

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getMariaDbHost() {
        return mariaDbHost;
    }

    public void setMariaDbHost(String mariaDbHost) {
        this.mariaDbHost = mariaDbHost;
    }

    public String getMariaDbUser() {
        return mariaDbUser;
    }

    public void setMariaDbUser(String mariaDbUser) {
        this.mariaDbUser = mariaDbUser;
    }

    public String getMariaDbPassword() {
        return mariaDbPassword;
    }

    public void setMariaDbPassword(String mariaDbPassword) {
        this.mariaDbPassword = mariaDbPassword;
    }

    public boolean isMariaDbPool() {
        return mariaDbPool;
    }

    public void setMariaDbPool(boolean mariaDbPool) {
        this.mariaDbPool = mariaDbPool;
    }

    public int getMariaDbTimeout() {
        return mariaDbTimeout;
    }

    public void setMariaDbTimeout(int mariaDbTimeout) {
        this.mariaDbTimeout = mariaDbTimeout;
    }

    public short getMariaDbPort() {
        return mariaDbPort;
    }

    public void setMariaDbPort(short mariaDbPort) {
        this.mariaDbPort = mariaDbPort;
    }

    public String getMariaDbDatabase() {
        return mariaDbDatabase;
    }

    public void setMariaDbDatabase(String mariaDbDatabase) {
        this.mariaDbDatabase = mariaDbDatabase;
    }

    /**
     * Returns every current setting
     */
    public List<String> getCurrentConfiguration() {
        List<String> currentConfig = new ArrayList<>();
        currentConfig.add(String.format("port: %s", port));
        currentConfig.add(String.format("hostAddress: %s", hostAddress));
        currentConfig.add(String.format("worldSavePeriodMin: %s", worldSavePeriodMin));
        currentConfig.add(String.format("serverType: %s", serverType));
        currentConfig.add(String.format("clientManagerType: %s", clientManagerType));
        currentConfig.add(String.format("logPackets: %s", logPackets));
        currentConfig.add(String.format("maxAwaySeconds: %s", maxAwaySeconds));
        currentConfig.add(String.format("maxNetworkInactivitySeconds: %s", maxNetworkInactivitySeconds));
        currentConfig.add(String.format("debugMode: %s", debugMode));
        currentConfig.add(String.format("debugWriteInfoMS: %s", debugWriteInfoMS));
        currentConfig.add(String.format("debugDetectDeadlockMS: %s", debugDetectDeadlockMS));
        currentConfig.add(String.format("debugGarbageCollectionMS: %s", debugGarbageCollectionMS));
        currentConfig.add(String.format("debugProcessInfoMS: %s", debugProcessInfoMS));
        currentConfig.add(String.format("tpProducerSleepMS: %s", tpProducerSleepMS));
        currentConfig.add(String.format("tpProducerCount: %s", tpProducerCount));
        currentConfig.add(String.format("tpConsumerCount: %s", tpConsumerCount));
        currentConfig.add(String.format("databaseType: %s", databaseType));
        return currentConfig;
    }
}
