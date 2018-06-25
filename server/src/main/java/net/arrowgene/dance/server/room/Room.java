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

package net.arrowgene.dance.server.room;

import net.arrowgene.dance.library.models.character.CharacterWalkDirection;
import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.channel.Channel;
import net.arrowgene.dance.library.models.character.CharacterSexTyp;
import net.arrowgene.dance.library.models.character.ControllerType;
import net.arrowgene.dance.library.models.character.GameTeam;
import net.arrowgene.dance.server.game.GameStatsData;
import net.arrowgene.dance.server.game.ScoreUser;
import net.arrowgene.dance.server.game.Scores;
import net.arrowgene.dance.server.game.SongType;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.builder.GamePacket;
import net.arrowgene.dance.server.packet.builder.RoomPacket;
import net.arrowgene.dance.server.song.SongManager;


import java.util.ArrayList;
import java.util.Random;


/**
 * A room where multiple players can join.
 */
public class Room {

    /**
     * The maximum capacity a client can technically hold. (active + viewers)
     */
    private static final int MAX_ROOM_CAPACITY = 16;

    /**
     * The default value for active open slots.
     */
    private static final int DEFAULT_MAX_ACTIVE = 6;

    private final Object roomLock = new Object();

    private int number;
    private Channel channel;
    private DanceClient host;
    private DanceClient[] clients;
    private Song song;
    private byte songDifficult;
    private byte songDifficult2;
    private String name;
    private String password;
    private RoomStateType state;
    private Scores scores = null;
    private ControllerType controllerType;
    private RoomModeType mode;
    private int maxPlayer;
    private int color;
    private boolean locked;
    private SongManager songManager;
    private RoomBackground background;
    private RoomGameMode gameMode;
    private RoomFormation formation;
    private int maxViewers;
    private boolean visible;

    public Room(SongManager songManager, DanceClient host, Channel channel, String name, String password, ControllerType controllerType) {
        this.songManager = songManager;
        this.song = songManager.getSong(SongType.NORMAL.getNumValue());
        this.maxPlayer = DEFAULT_MAX_ACTIVE;
        this.locked = false;
        this.color = 0;
        this.visible = true;
        this.songDifficult = 0;
        this.songDifficult2 = 0;
        this.mode = RoomModeType.NORMAL;
        this.clients = new DanceClient[MAX_ROOM_CAPACITY];
        this.state = RoomStateType.WAITING;
        this.host = host;
        this.channel = channel;
        this.name = name;
        this.password = password;
        this.controllerType = host.getCharacter().getController();
        this.number = channel.getNextFreeRoomNumber();

        this.maxViewers = MAX_ROOM_CAPACITY - 6;

        this.host.getCharacter().setDirection(CharacterWalkDirection.SOUTH);
        this.host.getCharacter().setMoving(1); // 1 = nicht bewegend
        this.host.getCharacter().setX(-100);
        this.host.getCharacter().setY(0);
        this.host.getCharacter().setZ(-100);

        this.background = RoomBackground.CASABLANCA;
        this.gameMode = RoomGameMode.MULTIPLAYER_MODE;

        this.join(this.host);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void sendGameStats(DanceClient client) {
        ScoreUser user = scores.get(client);
        user.setFinished(true);

        if (scores.allFinished()) {
            for (DanceClient c : getClients()) {
                c.sendPacket(GamePacket.getInstance().getGameEndedPacket(c, this));
            }

            DanceClient[] winningOrder = new DanceClient[this.getActiveCount()];
            int i = 0;
            for (DanceClient c : getActiveClients()) {
                setReady(c, false);
                client.setLoadingDone(false);

                winningOrder[i++] = c;
            }
            int n = winningOrder.length;
            do {
                int newn = 1;
                for (i = 0; i < n - 1; ++i) {
                    if (scores.get(winningOrder[i]).getPoints() < scores.get(winningOrder[i + 1]).getPoints()) {
                        DanceClient tmp = winningOrder[i];
                        winningOrder[i] = winningOrder[i + 1];
                        scores.get(winningOrder[i]).setPlace(i);
                        scores.get(winningOrder[i + 1]).setPlace(i + 1);
                        winningOrder[i + 1] = tmp;
                        newn = i + 1;
                    } // ende if
                } // ende for
                n = newn;
            } while (n > 1);

            for (DanceClient c : getActiveClients()) {
                ScoreUser su = scores.get(c);

                c.getCharacter().addExperience(su.getCharacterExperience());
                c.getCharacter().addPoints(su.getCharacterPoints());
                c.getCharacter().addCoins(su.getCharacterCoins());

                c.getCharacter().addGame(1);
                if (getActiveCount() / 2 >= su.getPlace()) {
                    c.getCharacter().addWins(1);
                } else {
                    c.getCharacter().addLosses(1);
                }

                c.getCharacter().addPerfects(su.getPerfects());
                c.getCharacter().addCools(su.getCools());
                c.getCharacter().addBads(su.getBads());
                c.getCharacter().addMisses(su.getMisses());
            }

            this.sendPacket(GamePacket.getInstance().getFinalGameStatsPacket(this, scores, winningOrder));
            this.scores = null;
            this.announceRoomInChannel();
        }
    }

    public void enterRoomAfterGame(DanceClient client) {
        client.getCharacter().setReady(false);

        client.sendPacket(RoomPacket.getInstance().getEnterRoomAfterGamePacket(client, this));
    }

    public void setGameStats(DanceClient client, GameStatsData data) {
        ScoreUser userStats = scores.get(client);

        userStats.setUnknown(data.getUnknown());
        userStats.addPerfects(data.getPerfects());
        userStats.addCools(data.getCools());
        userStats.addBads(data.getBads());
        userStats.addMisses(data.getMisses());
        userStats.setEnergy(data.getEnergy());
        userStats.setMaxCombo(data.getMaxCombo());
        userStats.setPacketId(data.getPacketId());

        userStats.addPoints(data.getPoints());

        announceGameStatsInRoom(client, userStats);
    }

    public void setTeam(DanceClient client, GameTeam team) {
        client.getCharacter().setTeam(team);
        this.sendPacket(GamePacket.getInstance().getChangeTeamPacket(client, this));
    }

    public void setNumberOfViewers(DanceClient client, int numOfViewers) {
        if (host == client) {
            this.maxViewers = numOfViewers;
        }
    }

    public RoomBackground getBackground() {
        return background;
    }

    public void setBackground(DanceClient client, RoomBackground bg, boolean random) {
        if (host == client) {
            this.background = bg;

            client.sendPacket(RoomPacket.getInstance().getUpdateBackgroundPacket(this));
            //this.announceBackgroundInRoom();
        }
    }

    public RoomFormation getFormation() {
        return formation;
    }

    public void setFormation(DanceClient client, RoomFormation formation) {
        if (host == client) {
            this.formation = formation;

            this.announceFormationInRoom();
        }
    }

    public RoomGameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(DanceClient client, RoomGameMode gameMode) {
        if (host == client) {
            this.gameMode = gameMode;

            this.announceGameModeInRoom();
        }
    }

    public void setViewer(DanceClient client) {
        if (isActiveClient(client)) {
            int oldSlotId = getSlot(client);
            int currentHostSlotId = getHostSlot();

            if (oldSlotId >= 0) {
                int newSlotId = this.assignSlotId(client, true);
                if (newSlotId != -1) {
                    synchronized (this.roomLock) {
                        this.clients[oldSlotId] = null;
                    }

                    // is char host? then update host char
                    if (oldSlotId == currentHostSlotId) {
                        this.host = null;
                        int slots = getActiveCount();
                        Random rand = new Random();

                        while (this.host == null) {
                            this.host = getActiveClients().get(rand.nextInt() % slots);
                        }
                    }
                    client.getCharacter().setRoomSlotId(newSlotId);

                    this.sendPacket(RoomPacket.getInstance().getSetVisitorPacket(client, this, oldSlotId));
                }
            }
        } else {
            //get active
            int oldSlotId = getSlot(client);
            if (oldSlotId >= 0) {
                if (getActiveCount() < 6) {
                    int newSlotId = this.assignSlotId(client);
                    if (newSlotId != -1) {
                        synchronized (this.roomLock) {
                            this.clients[oldSlotId] = null;
                        }
                        client.getCharacter().setRoomSlotId(newSlotId);

                        this.sendPacket(RoomPacket.getInstance().getSetVisitorPacket(client, this, oldSlotId));
                    }
                }
            }
        }
    }

    public boolean isActiveClient(DanceClient client) {
        boolean ret = false;
        for (DanceClient c : getActiveClients()) {
            if (c == client) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public boolean areAllActiveClientsReady() {
        boolean ret = true;

        for (DanceClient c : getActiveClients()) {
            if (!c.getCharacter().isReady() && !c.isHostInRoom()) {
                ret = false;
                break;
            }
        }

        return ret;
    }

    public boolean areAllClientsLoadingDone() {
        boolean ret = true;

        for (DanceClient c : getClients()) {
            if (!c.isLoadingDone()) {
                ret = false;
                break;
            }
        }

        return ret;
    }

    public void startSong() {
        if (areAllActiveClientsReady()) {
            scores = new Scores();
            for (DanceClient c : getActiveClients()) {
                scores.addUser(c);
            }

            this.announceRoomInChannel();
        }
    }

    public int getNumber() {
        return number;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getName() {
        return name;
    }

    public void setName(DanceClient client, String newName) {
        if (getHost() == client) {
            if (!newName.equals(name)) {
                this.name = newName;

                this.sendPacket(RoomPacket.getInstance().getChangeNamePacket(client, this));

                this.announceRoomInChannel();
            }
        }
    }

    public RoomStateType getState() {
        return state;
    }

    public ControllerType getControllerType() {
        return controllerType;
    }

    public RoomModeType getMode() {
        return mode;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public int getMaxClients() {
        return maxViewers + 6;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getColor() {
        return color;
    }

    public int getSlot(DanceClient client) {
        int slotId = -1;
        DanceClient[] users = this.getIndexedClients();
        for (int i = 0; i < MAX_ROOM_CAPACITY; i++) {
            if (users[i] != null && users[i].equals(client)) {
                slotId = i;
                break;
            }
        }
        return slotId;
    }

    public DanceClient getSlot(int slotId) {
        DanceClient[] users = this.getIndexedClients();
        return users[slotId];
    }

    public int getHostSlot() {
        int slotId = -1;
        DanceClient[] users = this.getIndexedClients();
        for (int i = 0; i < MAX_ROOM_CAPACITY; i++) {
            if (users[i] != null && users[i].equals(this.host)) {
                slotId = i;
                break;
            }
        }
        return slotId;
    }

    public Song getSong() {
        return song;
    }

    public int getSongDifficult() {
        return songDifficult;
    }

    public int getSongDifficult2() {
        return songDifficult2;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setSongDifficult(byte songDifficult) {
        this.songDifficult = songDifficult;
    }

    public void setSongDifficult2(byte songDifficult2) {
        this.songDifficult2 = songDifficult2;
    }

    public DanceClient getHost() {
        return host;
    }

    public void setHost(DanceClient client) {
        if (getActiveClients().contains(client)) {
            host = client;
        }
    }

    public void setReady(DanceClient client, boolean ready) {
        client.getCharacter().setReady(ready);
        this.sendPacket(RoomPacket.getInstance().getReadyPacket(client));
    }

    public void setReady(DanceClient client) {
        if (client.getCharacter().isReady()) {
            client.getCharacter().setReady(false);
        } else {
            client.getCharacter().setReady(true);
        }
        this.sendPacket(RoomPacket.getInstance().getReadyPacket(client));
    }

    /**
     * Adds a new client to this room.
     *
     * @param client
     */
    public boolean join(DanceClient client) {
        int slotId = this.assignSlotId(client);

        if (slotId >= 0) {
            client.getCharacter().setTeam(GameTeam.SINGLE);

            client.getCharacter().setRoomSlotId(slotId);
            client.setRoom(this);

            client.getChannel().sendPacket(RoomPacket.getInstance().getListUserUpdatePacket(client, this));

            this.announceRoomInChannel();

            return true;
        } else {
            return false;
        }
    }

    public void announceGameStatsInRoom(DanceClient client, ScoreUser stats) {
        Packet packet = GamePacket.getInstance().getGameStatsPacket(client, this, stats);
        if (packet != null)
            this.sendPacket(packet);
    }

    public void announceGameModeInRoom() {
        Packet packet = RoomPacket.getInstance().getUpdateGameModePacket(this);
        if (packet != null)
            this.sendPacket(packet);
    }

    public void announceFormationInRoom() {
        Packet packet = RoomPacket.getInstance().getUpdateFormationPacket(this);
        if (packet != null)
            this.sendPacket(packet);
    }

    public void announceBackgroundInRoom() {
        Packet packet = RoomPacket.getInstance().getUpdateBackgroundPacket(this);
        if (packet != null)
            this.sendPacket(packet);
    }

    public boolean isGameRunning() {
        return scores != null;
    }

    public void announceRoomInChannel() {
        // announce room
        Packet announceRoom = RoomPacket.getInstance().getAnnounceRoomPacket(this);
        host.getChannel().sendPacket(announceRoom, host);
    }

    /**
     * Removes a client from the room.
     *
     * @param client
     */
    public void leave(DanceClient client) {
        int slotId = client.getCharacter().getRoomSlotId();
        if (slotId >= 0) {
            if (scores != null) {
                //Die Runde läuft gerade
                scores.removeUser(client);
            }
            synchronized (this.roomLock) {
                this.clients[slotId] = null;
            }

            if (getClients().size() == 0) {
                this.channel.closeRoom(this);

            } else {
                if (this.host == client) {
                    this.host = null;
                    int slots = getActiveCount();
                    Random rand = new Random();

                    while (this.host == null) {
                        this.host = getActiveClients().get(rand.nextInt() % slots);
                    }
                }
            }
        } else {
            // TODO logger, this character should not be in the room!!!?
            System.out.print("TODO logger, this character should not be in the room!!!?");
        }

        this.sendPacket(RoomPacket.getInstance().getAnnounceLeaveRoomPacket(client, this));
        client.getCharacter().setRoomSlotId(-1);
        client.setRoom(null);
    }

    /**
     * Returns all clients who are in this room.
     * The index does not represent the slot position.
     *
     * @return The collection of clients in this room.
     */
    public ArrayList<DanceClient> getClients() {
        ArrayList<DanceClient> users = new ArrayList<>();
        synchronized (this.roomLock) {
            for (DanceClient client : this.clients) {
                if (client != null) {
                    users.add(client);
                }
            }
        }
        return users;
    }

    public DanceClient[] getIndexedClients() {
        DanceClient[] indexedClients;
        synchronized (this.roomLock) {
            indexedClients = this.clients.clone();
        }
        return indexedClients;
    }

    /**
     * Returns all active clients from this room.
     * The index does not represent the slot position.
     *
     * @return Collections of all active clients.
     */
    public ArrayList<DanceClient> getActiveClients() {
        ArrayList<DanceClient> users = new ArrayList<>();
        synchronized (this.roomLock) {
            for (int i = 0; i < this.maxPlayer; i++) {
                DanceClient client = this.clients[i];
                if (client != null) {
                    users.add(client);
                }
            }
        }
        return users;
    }

    /**
     * @return Number of active clients in this room.
     */
    public int getActiveCount() {
        return this.getActiveClients().size();
    }

    /**
     * @return Number of active male clients in this room.
     */
    public int getActiveMaleCount() {
        int maleCount = 0;
        for (DanceClient client : this.getActiveClients()) {
            if (client.getCharacter().getSex() == CharacterSexTyp.MALE) {
                maleCount++;
            }
        }
        return maleCount;
    }

    /**
     * Sends a package to everyone in this room.
     *
     * @param packet The packet to be send.
     */
    public void sendPacket(Packet packet) {
        ArrayList<DanceClient> users = this.getClients();
        for (DanceClient client : users) {
            client.sendPacket(packet);
        }
    }

    /**
     * Sends a package to everyone in this room with the exception of a single client.
     *
     * @param packet The packet to be send.
     * @param except The exception, who will not receive the packet.
     */
    public void sendPacket(Packet packet, DanceClient except) {
        ArrayList<DanceClient> users = this.getClients();
        for (DanceClient client : users) {
            if (client != except) {
                client.sendPacket(packet);
            }
        }
    }

    /**
     * Assigns a client to the next free slot and returns the number.
     *
     * @param client
     * @return The assigned slot id.
     */
    private int assignSlotId(DanceClient client) {
        return assignSlotId(client, false);
    }

    /**
     * Assigns a client to the next free slot and returns the number.
     *
     * @param client
     * @param onlyVisitorSlot
     * @return The assigned slot id.
     */
    private int assignSlotId(DanceClient client, boolean onlyVisitorSlot) {
        int slotId = -1;
        synchronized (this.roomLock) {
            for (int i = (onlyVisitorSlot) ? 6 : 0; i < this.clients.length; i++) {
                if (this.clients[i] == null) {
                    slotId = i;
                    this.clients[slotId] = client;
                    break;
                }
            }
        }
        return slotId;
    }

}
