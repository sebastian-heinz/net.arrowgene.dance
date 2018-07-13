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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class DanceServer implements ConnectedListener, DisconnectedListener, ReceivedPacketListener, Thread.UncaughtExceptionHandler {


    private static final Logger logger = LogManager.getLogger(DanceServer.class);

    public static long getUnixTimeNow() {
        return GregorianCalendar.getInstance().getTimeInMillis() / 1000;
    }

    public static String getBuildVersion() {
        return DanceServer.class.getPackage().getImplementationVersion();
    }

    private final Object clientLookupLock = new Object();

    private boolean online;
    private long startTime;
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
        online = false;
        this.serverConfig = serverConfig;
        initialize();
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
        return shop;
    }

    public PostOffice getPostOffice() {
        return postOffice;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public LoveMagistrate getLoveMagistrate() {
        return loveMagistrate;
    }

    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    public boolean isOnline() {
        return online;
    }

    /**
     * Starts the server
     */
    public void start() {
        startTime = getUnixTimeNow();
        logger.info("Current Server Configuration:");
        for (String config : serverConfig.getCurrentConfiguration()) {
            logger.info(config);
        }
        tcpServer.start();
        for (ServerComponent serverComponent : serverComponents) {
            serverComponent.start();
        }
        online = true;
    }

    /**
     * Stops the server
     * <p>
     * TODO ensure clean shutdown
     * 1) Stop accepting new connections
     * 2) Save the server state
     * 3) Save all client states
     * 4) Disconnect all clients
     * 5) Shutdown Server
     */
    public void stop() {
        online = false;
        save();
        for (ServerComponent serverComponent : serverComponents) {
            serverComponent.stop();
        }
        tcpServer.stop();
    }

    /**
     * Updates the database with the current state of the server.
     */
    public void save() {
        for (ServerComponent serverComponent : serverComponents) {
            serverComponent.save();
        }
    }

    public void writeDebugInfo() {
        logger.debug(String.format("Start Time: %d", startTime));
        logger.debug(String.format("Uptime: %d", getUnixTimeNow() - startTime));
        synchronized (clientLookupLock) {
            logger.debug(String.format("Client Lookups: %d", clientLookup.size()));
        }
        logger.debug(String.format("Server Components: %d", serverComponents.size()));
        tcpServer.writeDebugInfo();
        for (ServerComponent serverComponent : serverComponents) {
            serverComponent.writeDebugInfo();
        }
    }

    public void clientAuthenticated(DanceClient client) {
        for (ServerComponent serverComponent : serverComponents) {
            serverComponent.clientAuthenticated(client);
        }
    }

    @Override
    public void clientConnected(TcpClient tcpClient) {
        DanceClient client = new DanceClient(tcpClient);
        synchronized (clientLookupLock) {
            clientLookup.put(tcpClient, client);
        }
        for (ServerComponent serverComponent : serverComponents) {
            serverComponent.clientConnected(client);
        }
    }

    @Override
    public void receivedPacket(TcpClient tcpClient, ReadPacket readPacket) {
        DanceClient client;
        synchronized (clientLookupLock) {
            client = clientLookup.get(tcpClient);
        }
        if (client == null) {
            if (tcpClient == null) {
                logger.error("Couldn't lookup DanceClient because key 'TcpClient' is null.");
            } else {
                logger.error(String.format("Missing value DanceClient for key TcpClient (%s)", tcpClient));
            }
        } else {
            try {
                packetHandler.handle(readPacket, client);
            } catch (Throwable ex) {
                logger.error(String.format("%s (%s)", ex.getMessage(), tcpClient));
                logger.error(ex);
            }
        }
    }

    @Override
    public void clientDisconnected(TcpClient tcpClient) {
        DanceClient client;
        synchronized (clientLookupLock) {
            client = clientLookup.remove(tcpClient);
        }
        if (client != null) {
            for (ServerComponent serverComponent : serverComponents) {
                serverComponent.clientDisconnected(client);
            }
        }
    }

    private void initialize() {
        logger.info("Initializing Server");
        logger.info(String.format("Version: %s", DanceServer.getBuildVersion()));

        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        Thread.setDefaultUncaughtExceptionHandler(this);

        clientLookup = new HashMap<>();
        database = new SQLiteDb();

        deadlockDetectThread = new DeadLockDetector(this);
        deadlockDetectThread.setDaemon(true);
        deadlockDetectThread.start();

        switch (serverConfig.getServerType()) {
            case IO:
                tcpServer = new TcpServerIO(this);
                break;
            case NIO:
                tcpServer = new TcpServerNIO(this);
                break;
            default:
                logger.fatal("Server Type not found, check settings for [ServerType] value");
                break;
        }

        tcpServer.addConnectedListener(this);
        tcpServer.addDisconnectedListener(this);
        tcpServer.addReceivedPacketListener(this);

        packetHandler = new PacketHandlerBuilder(this).build();

        // Server Components
        this.serverComponents = new ArrayList<>();

        this.clientController = new ClientController(this);
        this.clientController.setPriority(100);
        this.serverComponents.add(clientController);

        this.characterManager = new CharacterManager(this);
        this.characterManager.setPriority(90);
        this.serverComponents.add(this.characterManager);


        this.lobby = new Lobby(this);
        this.serverComponents.add(lobby);

        chatManager = new ChatManager(this);
        serverComponents.add(chatManager);

        groupManager = new GroupManager(this);
        serverComponents.add(groupManager);

        songManager = new SongManager(this);
        serverComponents.add(songManager);

        shop = new Shop(this);
        serverComponents.add(shop);

        postOffice = new PostOffice(this);
        serverComponents.add(postOffice);

        loveMagistrate = new LoveMagistrate(this);
        serverComponents.add(loveMagistrate);

        inventoryManager = new InventoryManager(this);
        serverComponents.add(inventoryManager);

        taskManager = new TaskManager(this);
        serverComponents.add(taskManager);

        Collections.sort(serverComponents, new ServerComponentPriority());

        for (ServerComponent serverComponent : serverComponents) {
            serverComponent.load();
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error(String.format("%s (Thread Name: %s)", e.getMessage(), t.getName()));
        logger.error(e);
    }

    private class ShutdownHook extends Thread {
        @Override
        public void run() {
            logger.info("ShutdownHook triggered");
        }
    }
}
