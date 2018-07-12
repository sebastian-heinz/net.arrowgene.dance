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
import net.arrowgene.dance.database.sqlite.SQLiteDb;
import net.arrowgene.dance.server.character.CharacterManager;
import net.arrowgene.dance.server.chat.ChatManager;
import net.arrowgene.dance.server.client.ClientController;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.group.GroupManager;
import net.arrowgene.dance.server.inventory.InventoryManager;
import net.arrowgene.dance.server.lobby.Lobby;
import net.arrowgene.dance.log.LogType;
import net.arrowgene.dance.server.packet.PacketHandler;
import net.arrowgene.dance.server.packet.PacketHandlerBuilder;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.post.PostOffice;

import net.arrowgene.dance.server.shop.Shop;
import net.arrowgene.dance.server.song.SongManager;
import net.arrowgene.dance.server.task.TaskManager;
import net.arrowgene.dance.server.tcp.*;
import net.arrowgene.dance.server.tcp.io.TcpServerIO;
import net.arrowgene.dance.server.tcp.nio.TcpServerNIO;
import net.arrowgene.dance.server.util.DeadLockDetector;
import net.arrowgene.dance.server.wedding.LoveMagistrate;

import java.util.*;

public class DanceServer implements ConnectedListener, DisconnectedListener, ReceivedPacketListener, Thread.UncaughtExceptionHandler {

    public static long getUnixTimeNow() {
        return GregorianCalendar.getInstance().getTimeInMillis() / 1000;
    }

    public static String getBuildVersion() {
        return DanceServer.class.getPackage().getImplementationVersion();
    }


    private final Object clientLookupLock = new Object();

    private boolean online;
    private long startTime;
    private ServerLogger logger;
    private TcpServer tcpServer;
    private Database database;
    private Lobby lobby;
    private ChatManager chatManager;
    private GroupManager groupManager;
    private SongManager songManager;
    private Shop shop;
    private ServerConfig serverConfig;
    private ClientController clientController;
    private PostOffice postOffice;
    private LoveMagistrate loveMagistrate;
    private CharacterManager characterManager;
    private InventoryManager inventoryManager;
    private PacketHandler packetHandler;
    private HashMap<TcpClient, DanceClient> clientLookup;
    private List<ServerComponent> serverComponents;
    private Thread deadlockDetectThread;
    private TaskManager taskManager;

    /**
     * Creates a new instance of the DanceServer with a default configuration.
     */
    public DanceServer() {
        this(new ServerConfig());
    }

    /**
     * Creates a new instance of the DanceServer with a given configuration.
     *
     * @param serverConfig The configuration.
     */
    public DanceServer(ServerConfig serverConfig) {
        this.online = false;
        this.serverConfig = serverConfig;
        this.initServer();
    }

    public ServerLogger getLogger() {
        return logger;
    }

    public TcpServer getTcpServer() {
        return tcpServer;
    }

    public Database getDatabase() {
        return database;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public SongManager getSongManager() {
        return songManager;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public Shop getShop() {
        return this.shop;
    }

    public PostOffice getPostOffice() {
        return this.postOffice;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public LoveMagistrate getLoveMagistrate() {
        return this.loveMagistrate;
    }

    public CharacterManager getCharacterManager() {
        return this.characterManager;
    }

    public boolean isOnline() {
        return online;
    }

    /**
     * Starts the server
     */
    public void start() {
        this.startTime = getUnixTimeNow();
        for (String config : this.serverConfig.getCurrentConfiguration()) {
            this.logger.writeLog(LogType.SERVER, "DanceServer", "start", config);
        }
        this.tcpServer.start();
        for (ServerComponent serverComponent : this.serverComponents) {
            serverComponent.start();
        }
        this.online = true;
    }

    /**
     * Stops the server
     * <p>
     * 1) Stop accepting new connections (TODO: boolean to toggle maintenance message for trying to login)
     * 2) Save the server state
     * 3) Save all client states
     * 4) Disconnect all clients
     * 5) Shutdown Server
     */
    public void stop() {
        this.online = false;
        this.save();
        for (ServerComponent serverComponent : this.serverComponents) {
            serverComponent.stop();
        }
        this.tcpServer.stop();
        this.logger.saveLogs();
    }

    public void load() {
        for (ServerComponent serverComponent : this.serverComponents) {
            serverComponent.load();
        }
    }

    /**
     * Updates the database with the current state of the server.
     */
    public void save() {
        for (ServerComponent serverComponent : this.serverComponents) {
            serverComponent.save();
        }
    }

    public void writeDebugInfo() {
        this.logger.writeLog(LogType.DEBUG, "DanceServer", "writeDebugInfo", "Start Time: " + this.startTime);
        this.logger.writeLog(LogType.DEBUG, "DanceServer", "writeDebugInfo", "Uptime: " + (getUnixTimeNow() - this.startTime));
        synchronized (this.clientLookupLock) {
            this.logger.writeLog(LogType.DEBUG, "DanceServer", "writeDebugInfo", "Client Lookups: " + this.clientLookup.size());
        }
        this.logger.writeLog(LogType.DEBUG, "DanceServer", "writeDebugInfo", "Server Components: " + this.serverComponents.size());
        this.tcpServer.writeDebugInfo();
        for (ServerComponent serverComponent : this.serverComponents) {
            serverComponent.writeDebugInfo();
        }
    }

    public void clientAuthenticated(DanceClient client) {
        for (ServerComponent serverComponent : this.serverComponents) {
            serverComponent.clientAuthenticated(client);
        }
    }

    @Override
    public void clientConnected(TcpClient tcpClient) {
        DanceClient client = new DanceClient(tcpClient, this.logger);
        synchronized (this.clientLookupLock) {
            this.clientLookup.put(tcpClient, client);
        }
        for (ServerComponent serverComponent : this.serverComponents) {
            serverComponent.clientConnected(client);
        }
    }

    @Override
    public void receivedPacket(TcpClient tcpClient, ReadPacket readPacket) {
        DanceClient client;
        synchronized (this.clientLookupLock) {
            client = this.clientLookup.get(tcpClient);
        }
        if (client == null) {
            if (tcpClient == null) {
                this.logger.writeLog(LogType.CLIENT, "DanceServer", "receivedPacket", "Couldn't lookup DanceClient because key 'TcpClient' is null.");
            } else {
                this.logger.writeLog(LogType.CLIENT, "DanceServer", "receivedPacket", "Missing value DanceClient for key TcpClient", tcpClient);
            }
        } else if (this.serverConfig.isDebugMode()) {
            try {
                this.packetHandler.handle(readPacket, client);
            } catch (Throwable exception) {
                this.logger.writeLog(LogType.ERROR, "DanceServer", "receivedPacket", "Debug Mode prevent an error");
                this.logger.writeLog(exception, tcpClient);
            }
        } else {
            this.packetHandler.handle(readPacket, client);
        }
    }

    @Override
    public void clientDisconnected(TcpClient tcpClient) {
        DanceClient client;
        synchronized (this.clientLookupLock) {
            client = this.clientLookup.remove(tcpClient);
        }
        if (client != null) {
            for (ServerComponent serverComponent : this.serverComponents) {
                serverComponent.clientDisconnected(client);
            }
        }
    }

    private void initServer() {
        // TODO
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
           //    SQLiteController.this.close();
            }
        });

        this.logger = new ServerLogger(this.serverConfig);
        Thread.setDefaultUncaughtExceptionHandler(this);

        this.clientLookup = new HashMap<>();
        this.logger = new ServerLogger(this.serverConfig);
        this.database = new SQLiteDb(this.logger);

        this.logger.writeLog(LogType.SERVER, "DanceServer", "initServer", "Version:" + DanceServer.getBuildVersion());

        if (this.serverConfig.isDebugMode()) {
            deadlockDetectThread = new DeadLockDetector(this);
            deadlockDetectThread.setDaemon(true);
            deadlockDetectThread.start();
        }

        switch (this.serverConfig.getServerType()) {
            case IO:
                this.tcpServer = new TcpServerIO(this);
                break;
            case NIO:
                this.tcpServer = new TcpServerNIO(this);
                break;
            default:
                this.logger.writeLog(LogType.ERROR,
                    "DanceServer::initServer:Server Type not found, check settings for [ServerType] value");
                break;
        }

        this.tcpServer.addConnectedListener(this);
        this.tcpServer.addDisconnectedListener(this);
        this.tcpServer.addReceivedPacketListener(this);

        this.packetHandler = new PacketHandlerBuilder(this).build();

        // Server Components
        this.serverComponents = new ArrayList<>();

        this.clientController = new ClientController(this);
        this.clientController.setPriority(100);
        this.serverComponents.add(clientController);

        this.characterManager = new CharacterManager(this);
        this.characterManager.setPriority(90);
        this.serverComponents.add(this.characterManager);


        this.lobby = new Lobby(this);
        this.serverComponents.add(this.lobby);

        this.chatManager = new ChatManager(this);
        this.serverComponents.add(this.chatManager);

        this.groupManager = new GroupManager(this);
        this.serverComponents.add(this.groupManager);

        this.songManager = new SongManager(this);
        this.serverComponents.add(this.songManager);

        this.shop = new Shop(this);
        this.serverComponents.add(this.shop);

        this.postOffice = new PostOffice(this);
        this.serverComponents.add(this.postOffice);

        this.loveMagistrate = new LoveMagistrate(this);
        this.serverComponents.add(this.loveMagistrate);

        this.inventoryManager = new InventoryManager(this);
        this.serverComponents.add(this.inventoryManager);

        this.taskManager = new TaskManager(this);
        this.serverComponents.add(this.taskManager);

        Collections.sort(this.serverComponents, new ServerComponentPriority());

        this.load();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.logger.writeLog(LogType.ERROR, "DanceServer", "uncaughtException", "Thread Died: " + t.getName());
        this.logger.writeLog(e);
    }
}
