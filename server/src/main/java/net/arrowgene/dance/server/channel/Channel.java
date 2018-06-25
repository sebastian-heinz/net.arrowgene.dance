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

package net.arrowgene.dance.server.channel;


import net.arrowgene.dance.library.models.channel.ChannelDetails;
import net.arrowgene.dance.library.models.character.ControllerType;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.builder.ChannelPacket;
import net.arrowgene.dance.server.packet.builder.RoomPacket;
import net.arrowgene.dance.server.room.Room;
import net.arrowgene.dance.server.song.SongManager;

import java.util.ArrayList;


public class Channel {

    private final Object channelRoomsLock = new Object();
    private final Object channelUsersLock = new Object();
    private ArrayList<Room> rooms;
    private ArrayList<DanceClient> users;
    private ChannelDetails details;

    /**
     * Creates a new instance of a channel which can hold multiple clients and rooms.
     */
    public Channel(ChannelDetails details) {
        this.details = details;
        this.rooms = new ArrayList<Room>();
        this.users = new ArrayList<DanceClient>();
    }

    public ChannelDetails getDetails() {
        return details;
    }

    /**
     * Determines the next free room number which can be assigned to a new room.
     *
     * @return A available room number.
     */
    public int getNextFreeRoomNumber() {
        int availableNumber = -1;
        synchronized (this.channelRoomsLock) {
            boolean found = false;
            do {
                availableNumber++;
                found = false;
                for (Room room : rooms) {
                    if (room.getNumber() == availableNumber) {
                        found = true;
                    }
                }
            } while (found);
        }
        return availableNumber;
    }

    /**
     * @return The collection of currently created rooms.
     */
    public ArrayList<Room> getRooms() {
        ArrayList<Room> rooms;
        synchronized (this.channelRoomsLock) {
            rooms = new ArrayList<>(this.rooms);
        }
        return rooms;
    }

    /**
     * @return The collection of currently created rooms.
     */
    public Room getRoom(int roomNumber) {
        Room ret = null;

        for (Room r : this.rooms) {
            if (r.getNumber() == roomNumber) {
                ret = r;
                break;
            }
        }
        return ret;
    }

    /**
     * @return The collection of currently connected clients in this channel.
     */
    public ArrayList<DanceClient> getClients() {
        ArrayList<DanceClient> users;
        synchronized (this.channelUsersLock) {
            users = new ArrayList<DanceClient>(this.users);
        }
        return users;
    }

    /**
     * Returns the client with the specified characterName
     * or null if the client can not be found in this channel.
     *
     * @param characterName
     * @return matching DanceClient
     */
    public DanceClient getClientByCharacterName(String characterName) {
        DanceClient danceClient = null;
        for (DanceClient client : this.getClients()) {
            if (client.getCharacter().getName().equals(characterName)) {
                danceClient = client;
                break;
            }
        }
        return danceClient;
    }

    /**
     * Closes a room and removes all clients from it.
     *
     * @param room The room to be closed.
     */
    public void closeRoom(Room room) {
        // TODO closing the room.
        synchronized (this.channelRoomsLock) {
            sendPacket(RoomPacket.getInstance().getRemoveRoomPacket(room));
            this.rooms.remove(room);
        }
    }

    /**
     * Creates a new room in this channel.
     *
     * @param host The creator of the room.
     * @return The created room.
     */
    public Room createRoom(SongManager songManager, DanceClient host, String name, String password, ControllerType controllerType) {
        Room room = new Room(songManager, host, this, name, password, controllerType);

        this.rooms.add(room);

        //room.announceRoomInChannel(); // wird announced wenn host in Room "join"t

        return room;
    }

    /**
     * Determines the count of currently connected clients in this channel.
     *
     * @return The count of clients currently inside this channel.
     */
    public int getClientCount() {
        int count;
        synchronized (this.channelUsersLock) {
            count = this.users.size();
        }
        return count;
    }

    /**
     * A client joins this channel.
     *
     * @param client The joining client.
     */
    public void join(DanceClient client) {
        synchronized (this.channelUsersLock) {
            this.users.add(client);
        }
        client.setChannel(this);
        Packet announceJoin = ChannelPacket.getInstance().getAnnounceJoin(client.getCharacter());
        this.sendPacket(announceJoin);
    }

    /**
     * A client leaves this channel.
     *
     * @param client The leaving client.
     */
    public void leave(DanceClient client) {
        synchronized (this.channelUsersLock) {
            this.users.remove(client);
        }
        client.setChannel(null);
        Packet announceLeave = ChannelPacket.getInstance().getAnnounceLeave(client.getCharacter());
        this.sendPacket(announceLeave, client);
    }

    /**
     * Checks if this channel has reached the maximum amount of clients it can hold.
     *
     * @return {@code true} if it reached the maximum or {@code false} if not.
     */
    public boolean isFull() {
        boolean isFull = false;
        synchronized (this.channelUsersLock) {
            if (this.details.getMaxUser() <= this.users.size()) {
                isFull = true;
            }
        }
        return isFull;
    }

    /**
     * Sends a package to everyone in this channel.
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
     * Sends a package to everyone in this channel with the exception of a single client.
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
}
