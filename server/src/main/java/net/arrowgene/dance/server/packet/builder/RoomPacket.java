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

package net.arrowgene.dance.server.packet.builder;


import net.arrowgene.dance.library.models.character.CharacterWalkDirection;
import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.room.Room;
import net.arrowgene.dance.library.models.character.Character;

public class RoomPacket {

    private static RoomPacket instance = new RoomPacket();

    public static RoomPacket getInstance() {
        return instance;
    }

    public Packet getAnnounceRoomPacket(Room room) {
        Packet packet = new SendPacket(PacketType.LOBBY_RESPONSE_ANNOUNCE_ROOM);
        packet.addInt32(room.getNumber());
        packet.addByte(room.isVisible()?1:0); // 1 = visible, 0 = invisible
        packet.addStringNulTerminated(room.getName());
        packet.addByte(0);// Icon hinter Namen
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte((byte) (room.isLocked() ? 1 : 0)); // Locked?
        packet.addByte(room.getControllerType().getNumValue()); // Keyboard / Dancepad
        packet.addInt16(room.getMode().getNumValue()); // 0 = Normal, 1 = Boys vs Girls, 2 = Couples Mode, 3 = 3D Notes, 4 = Superstar Mode, 5 = Battel Mode (kein Schreibfehler), 6 = Couple Lean, 7 = walk street, 8 = Club st, 9 = Back Street, 10 = Stage, 11 = Tahiti, 12 = Xmas, ....
        packet.addByte(room.getActiveCount()); //player
        packet.addByte(room.getActiveMaleCount()); //males
        packet.addByte(room.getMaxPlayer()); //max user
        packet.addInt32(room.getSong().getSongId() | 0x01000000);
        packet.addByte(room.getColor()); //color of room number 0 = blue 1 = yellow 2 = red
        packet.addByte(3);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);

        return packet;
    }

    public Packet getListUserUpdatePacket(DanceClient client, Room room) {
        Packet packet = new SendPacket(PacketType.LOBBY_RESPONSE_LIST_USER_UPDATE);
        packet.addStringNulTerminated(client.getCharacter().getName());
        packet.addInt32(room.getNumber());
        packet.addByte(0);

        return packet;
    }

    public Packet getStartGameErrorPacket(int errorCode) {
        SendPacket packet = new SendPacket(PacketType.GAME_RESPONSE_START_GAME);

        packet.addInt32(errorCode);
        packet.addByte(0);

        return packet;
    }

    public Packet getUpdateFormationPacket(Room room)
    {
        SendPacket answerPacket = null;

        if(room != null && room.getGameMode() != null)
        {
            answerPacket = new SendPacket(PacketType.ROOM_RESPONSE_FORMATION);
            answerPacket.addInt32(room.getFormation().getNumValue());
            answerPacket.addByte(0);
        }

        return answerPacket;
    }

    public Packet getUpdateGameModePacket(Room room)
    {
        SendPacket answerPacket = null;

        if(room != null && room.getGameMode() != null)
        {
            answerPacket = new SendPacket(PacketType.GAME_RESPONSE_GAMEMODE);
            answerPacket.addInt32(room.getGameMode().getNumValue());
            answerPacket.addByte(0);
        }

        return answerPacket;
    }

    public Packet getUpdateBackgroundPacket(Room room)
    {
        SendPacket answerPacket = new SendPacket(PacketType.GAME_RESPONSE_BACKGROUND);
        answerPacket.addInt32(room.getBackground().getNumValue());
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getCreateRoomPacket(DanceClient client, Room room)
    {
        SendPacket answerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_CREATE_ROOM);

        answerPacket.addInt32(0); // no error
        answerPacket.addInt32(room.getNumber());
        answerPacket.addInt16(0); //Usernumber 0 = Host
        answerPacket.addStringNulTerminated(room.getName());
        answerPacket.addByte(0);
        answerPacket.addInt32(room.getSong().getSongId() | 0x01000000);
        answerPacket.addByte(8); //dunno
        answerPacket.addByte(1); //dunno
        answerPacket.addByte(0); //dunno
        answerPacket.addByte(0); //dunno
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0); //Gamemode
        answerPacket.addByte(0); //Gamemode
        answerPacket.addByte(0); //Difficult 0 = Easy 1 = Normal 2 = Hard
        answerPacket.addByte(2);
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(255);


        //Spawn Position
        answerPacket.addByte(client.getCharacter().getDirection().getNumValue()); //Direction (North,...)
        answerPacket.addByte(client.getCharacter().getMoving());

        answerPacket.addFloat(client.getCharacter().getX()); //X
        answerPacket.addFloat(client.getCharacter().getY()); //Y
        answerPacket.addFloat(client.getCharacter().getZ()); //Z

        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getStartGamePacket(Song song) {
        SendPacket packet = new SendPacket(PacketType.GAME_RESPONSE_START_GAME);
        packet.addInt32(0);

        packet.addBytes(song.getDancepadData());
        packet.addBytes(song.getKeyboardData());
        packet.addHEXString(song.getKey());

        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);

        return packet;
    }

    public Packet getChangeNamePacket(DanceClient client, Room room)
    {
        SendPacket answerPacket = new SendPacket(PacketType.ROOM_RESPONSE_CHANGE_ROOMNAME);

        answerPacket.addStringNulTerminated(room.getName());
        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getSetVisitorPacket(DanceClient client, Room room, int oldSlotId)
    {
        SendPacket answerPacket = new SendPacket(PacketType.ROOM_RESPONSE_SET_VIEWER);
        answerPacket.addByte(oldSlotId);
        answerPacket.addByte(room.getSlot(client));
        answerPacket.addByte(room.getHostSlot());
        answerPacket.addByte(0);

        if(room.getSlot(client) > 5)
        {
            answerPacket.addByte(2);
            answerPacket.addByte(1);
            answerPacket.addFloat(0);
            answerPacket.addFloat(0);
            answerPacket.addFloat(0);
            answerPacket.addByte(0);
            answerPacket.addByte(0);
            answerPacket.addByte(0);
            answerPacket.addByte(0);
        }else {
            client.getCharacter().setDirection(CharacterWalkDirection.SOUTH);
            client.getCharacter().setMoving(1);

            answerPacket.addByte(client.getCharacter().getDirection().getNumValue());//client.getCharacter().getDirection());
            answerPacket.addByte(client.getCharacter().getMoving());
            answerPacket.addFloat(client.getCharacter().getX());
            answerPacket.addFloat(client.getCharacter().getY());
            answerPacket.addFloat(client.getCharacter().getZ());
            answerPacket.addByte(0);
            answerPacket.addByte(0);
            answerPacket.addByte(0);
            answerPacket.addByte(0);
        }

        return answerPacket;
    }

    public Packet getAnnounceLeaveRoomPacket(DanceClient client, Room room)
    {
        SendPacket answerPacket = new SendPacket(PacketType.ROOM_RESPONSE_LEAVE_ANNOUNCE);
        answerPacket.addByte(client.getCharacter().getRoomSlotId());
        answerPacket.addByte(room.getHostSlot());
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getJoinRoomAnnouncePacket(DanceClient client, Room room) {
        SendPacket answerPacket = new SendPacket(PacketType.ROOM_RESPONSE_JOIN_ANNOUNCE);

        answerPacket.addByte(room.getHostSlot()); //Hostslot, nur eine Vermutung
        answerPacket.addByte(room.getSlot(client));

        addCharacterData(client, answerPacket);

        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getReadyPacket(DanceClient client)
    {
        SendPacket answerPacket = new SendPacket(PacketType.GAME_RESPONSE_READY);

        int slotId = client.getRoom().getSlot(client);
        answerPacket.addByte(slotId);
        answerPacket.addInt16(client.getCharacter().isReady()?1:0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        return answerPacket;
    }

    public void addCharacterData(DanceClient client, SendPacket packet)
    {
        Character character = client.getCharacter();
        packet.addStringNulTerminated(character.getName());
        packet.addInt32(character.getLevel());
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(0);
        packet.addByte(character.getSex().getNumValue());
        packet.addByte(character.getController().getNumValue());
        packet.addByte(character.getTeam().getNumValue());
        packet.addByte((client.isHostInRoom())?1:(character.isReady()?1:0)); //Host ist immer ready
        packet.addInt32(character.getWeight());
        packet.addByte(0);
        packet.addByte(0xff); // Avatar Skin - 0xff = Standard
        //Spawn
        packet.addByte(character.getDirection().getNumValue());
        packet.addByte(character.getMoving());
        packet.addFloat(character.getX());
        packet.addFloat(character.getY());
        packet.addFloat(character.getZ());
        //Guild
        //packet.addString(group.Name);
        packet.addByte(0);
        //packet.addInt16(group.Icon);
        packet.addInt16(0);
        //Clothes
        packet.addInt32(2147483647);

        ////////
        packet.addInt32(0xdbba2);
        packet.addInt32(0xdbba3);
        packet.addInt32(0xdbba4);
        packet.addInt32(0xdbba6);
        //packet.AddByteString("a2 bb 0d 00"); //Head
        //packet.AddByteString("a3 bb 0d 00"); //Chest
        //packet.AddByteString("a4 bb 0d 00"); //Legs
        //packet.AddByteString("a6 bb 0d 00"); //Shoes
        //////////

        packet.addInt32(2147483647);
        packet.addInt32(2147483647); //Hair
        packet.addInt32(2147483647); //Top
        packet.addInt32(2147483647); //Pants
        packet.addInt32(2147483647); //Gloves
        packet.addInt32(2147483647); //Boots
        packet.addInt32(2147483647); //Face
        packet.addInt32(2147483647); //Glasses
        packet.addInt32(2147483647);
        packet.addInt32(2147483647);
        packet.addInt32(2147483647);
    }

    public Packet getEnterRoomAfterGamePacket(DanceClient client, Room room) {
        SendPacket answerPacket = new SendPacket(PacketType.ROOM_RESPONSE_JOIN_ROOM_AFTER_GAME);
        int playerCount = room.getMaxPlayer();

        answerPacket.addInt32(room.getNumber()); //room number
        answerPacket.addByte(room.getSlot(client)); //?????
        answerPacket.addByte(0);//?????
        answerPacket.addStringNulTerminated(room.getName());
        answerPacket.addByte(0);
        answerPacket.addInt32(room.getSong().getSongId() | 0x01000000);
        //answerPacket.addByte(0); //Scene) int 16´? (0 = walk street, 1 = Club St, 2 = BackStreet, 3 = Stage, 4 = Tahiti, 5 = Xmas, 6 = Carnival, 7 = Secret Gdn, 8 = Dance Trip, 9 = Casablanca, 10 = Flourishing Street)
        //answerPacket.addByte(1);
        answerPacket.addInt32(room.getBackground().getNumValue());
        answerPacket.addInt32(0);
        answerPacket.addInt16(room.getMode().getNumValue()); //Game Mode

        answerPacket.addByte(0);
        answerPacket.addByte(1);
        answerPacket.addByte(0);
        answerPacket.addByte(room.getHostSlot()); //<- host
        answerPacket.addByte(255);
        answerPacket.addByte(0);

        for (int i = 0; i < room.getMaxClients(); i++) {
            answerPacket.addByte(i); //0 - 15; Slots

            if (room.getSlot(i) != null) {
                answerPacket.addInt32(1); // Slot Status (0 = Empty, 1 = Player, 2 = Closed)

                this.addCharacterData(room.getSlot(i), answerPacket);
            } else {
                answerPacket.addInt32(0); // 0 is frei, 2 is Closed
            }
        }

        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getJoinRoomPacket(DanceClient client, Room room, boolean success) {
        SendPacket answerPacket = new SendPacket(PacketType.ROOM_RESPONSE_JOIN_ROOM);
        if (success) {
            answerPacket.addInt32(0); // no error
        } else {
            answerPacket.addInt32(1);
        }
        answerPacket.addInt32(room.getNumber());
        answerPacket.addByte(room.getSlot(client)); //?????
        answerPacket.addByte(0);//?????
        answerPacket.addStringNulTerminated(room.getName());
        answerPacket.addByte(0);
        answerPacket.addInt32(room.getSong().getSongId() | 0x01000000);
        //answerPacket.addByte(0); //Scene) int 16´? (0 = walk street, 1 = Club St, 2 = BackStreet, 3 = Stage, 4 = Tahiti, 5 = Xmas, 6 = Carnival, 7 = Secret Gdn, 8 = Dance Trip, 9 = Casablanca, 10 = Flourishing Street)
        //answerPacket.addByte(1);
        answerPacket.addInt32(room.getBackground().getNumValue());
        answerPacket.addInt32(0);
        answerPacket.addInt16(room.getMode().getNumValue()); //Game Mode

        answerPacket.addByte(0);
        answerPacket.addByte(2);
        answerPacket.addByte(0);
        answerPacket.addByte(room.getHostSlot()); //<- host
        answerPacket.addByte(255);
        answerPacket.addByte(0);

        for (int i = 0; i < room.getMaxClients(); i++) {
            answerPacket.addByte(i); //0 - 15; Slots

            if (room.getSlot(i) != null) {
                answerPacket.addInt32(1); // Slot Status (0 = Empty, 1 = Player, 2 = Closed)

                this.addCharacterData(room.getSlot(i), answerPacket);
            } else {
                answerPacket.addInt32(0); // 0 is frei, 2 is Closed
            }
        }
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);


        return answerPacket;
    }

    public Packet getChangeSongPacket(Room room, byte other) {
        Packet answerPacket = new SendPacket(PacketType.GAME_RESPONSE_CHANGE_SONG);

        answerPacket.addInt32(room.getSong().getSongId() | 0x01000000);
        answerPacket.addByte(room.getSongDifficult());
        answerPacket.addByte(room.getSongDifficult2());
        answerPacket.addByte(other);

        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getRemoveRoomPacket(Room room) {
        Packet answerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_REMOVE_ROOM);

        answerPacket.addInt32(room.getNumber());

        answerPacket.addInt32(0);
        answerPacket.addInt32(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        return answerPacket;
    }

}
