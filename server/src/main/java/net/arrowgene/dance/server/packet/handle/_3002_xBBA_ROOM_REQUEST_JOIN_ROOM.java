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

package net.arrowgene.dance.server.packet.handle;

import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.packet.builder.RoomPacket;
import net.arrowgene.dance.server.room.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class _3002_xBBA_ROOM_REQUEST_JOIN_ROOM extends HandlerBase {


    private static final Logger logger = LogManager.getLogger(_3002_xBBA_ROOM_REQUEST_JOIN_ROOM.class);

    public _3002_xBBA_ROOM_REQUEST_JOIN_ROOM(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        int roomNumber = packet.getInt32();

        Room room = client.getChannel().getRoom(roomNumber);
        boolean success = false;
        if (room != null) {
            success = room.join(client);
        }
        client.sendPacket(RoomPacket.getInstance().getJoinRoomPacket(client, room, success));

        if (success) {
            client.getRoom().sendPacket(RoomPacket.getInstance().getJoinRoomAnnouncePacket(client, room));
            logger.info(String.format("joined room '%s' (%s)", room.getName(), client));
        } else {
            if (room != null) {
                logger.warn(String.format("couldn't join room '%s' (%s)", room.getName(), client));
            } else {
                logger.warn(String.format("couldn't join room number '%d' (%s)", roomNumber, client));
            }
        }

        return null;

        //  int roomNumber = packet.getInt32();

        //  for (Room r : rooms) {
        //      if (r.getNumber() == roomNumber) {
        //          // Fehlerbehandlung wenn Raum voll
        //          r.addAccount(client);

        //          Packet answerPacket = new Packet(PacketType.ROOM_RESPONSE_JOIN_ROOM);
        //          answerPacket.addInt32(0); // no error
        //          answerPacket.addInt32(r.getNumber());
        //          answerPacket.addByte(r.getSlot(client)); //?????
        //          answerPacket.addByte(r.getHostSlot()); //?????
        //          answerPacket.addStringNulTerminated(r.getName());
        //          answerPacket.addByte(0);
        //          answerPacket.addInt32(r.getSong());
        //          //answerPacket.addByte(0); //Scene) int 16´? (0 = walk street, 1 = Club St, 2 = BackStreet, 3 = Stage, 4 = Tahiti, 5 = Xmas, 6 = Carnival, 7 = Secret Gdn, 8 = Dance Trip, 9 = Casablanca, 10 = Flourishing Street)
        //          //answerPacket.addByte(1);
        //          answerPacket.addInt32(9);
        //          answerPacket.addInt32(0);
        //          answerPacket.addByte(r.getMode()); //Game Mode
        //          answerPacket.addByte(0);
        //          answerPacket.addByte(0);
        //          answerPacket.addByte(2);
        //          answerPacket.addByte(0);
        //          answerPacket.addByte(0);
        //          answerPacket.addByte(255);
        //          answerPacket.addByte(0);

        //          for (int i = 0; i < r.getMaxPlayer(); i++) {
        //              answerPacket.addByte(i); //0 - 15; Slots

        //              if (r.getSlot(i) != null) {
        //                  Character character = r.getSlot(i).getCharacter();
        //                  //group group = room.Slots[i].DanceClient.group;

        //                  answerPacket.addInt32(1); // Slot Status (0 = Empty, 1 = Player, 2 = Closed)
        //                  answerPacket.addStringNulTerminated(character.getName());
        //                  answerPacket.addInt32(character.getLevel());
        //                  answerPacket.addInt32(1);
        //                  answerPacket.addByte(character.getSex().getNumValue());
        //                  answerPacket.addByte(1);
        //                  answerPacket.addByte(character.getTeam());
        //                  answerPacket.addByte(character.getReady());
        //                  answerPacket.addInt32(character.getWeight());
        //                  answerPacket.addByte(0);
        //                  answerPacket.addByte(0xff); // Avatar Skin - 0xff = Standard
        //                  //Spawn
        //                  answerPacket.addByte(character.getDirection());
        //                  answerPacket.addByte(1);
        //                  answerPacket.addFloat(character.getX());
        //                  answerPacket.addFloat(character.getY());
        //                  answerPacket.addFloat(character.getZ());
        //                  //Guild
        //                  //answerPacket.addStringNulTerminated(group.Name);
        //                  answerPacket.addByte(0);
        //                  //answerPacket.addInt16(group.Icon);
        //                  answerPacket.addInt16(0);
        //                  //Clothes
        //                  answerPacket.addInt32(2147483647);

        //                  ////////
        //                  answerPacket.addInt32(0xdbba2);
        //                  answerPacket.addInt32(0xdbba3);
        //                  answerPacket.addInt32(0xdbba4);
        //                  answerPacket.addInt32(0xdbba6);
        //                  //answerPacket.AddByteString("a2 bb 0d 00"); //Head
        //                  //answerPacket.AddByteString("a3 bb 0d 00"); //Chest
        //                  //answerPacket.AddByteString("a4 bb 0d 00"); //Legs
        //                  //answerPacket.AddByteString("a6 bb 0d 00"); //Shoes
        //                  //////////

        //                  answerPacket.addInt32(2147483647);
        //                  answerPacket.addInt32(2147483647); //Hair
        //                  answerPacket.addInt32(2147483647); //Top
        //                  answerPacket.addInt32(2147483647); //Pants
        //                  answerPacket.addInt32(2147483647); //Gloves
        //                  answerPacket.addInt32(2147483647); //Boots
        //                  answerPacket.addInt32(2147483647); //Face
        //                  answerPacket.addInt32(2147483647); //Glasses
        //                  answerPacket.addInt32(2147483647);
        //                  answerPacket.addInt32(2147483647);
        //                  answerPacket.addInt32(2147483647);
        //              } else {
        //                  answerPacket.addInt32(0);
        //              }
        //          }
        //          answerPacket.addByte(0);
        //          answerPacket.addByte(0);
        //          answerPacket.addByte(0);

        //          System.out.println(answerPacket.toString());

        //          client.write(answerPacket);


        //          Character character = client.getCharacter();

        //          Packet announcePlayer = new Packet(PacketType.ROOM_RESPONSE_JOIN_ANNOUNCE);
        //          announcePlayer.addByte(r.getHostSlot()); //Hostslot, nur eine Vermutung
        //          announcePlayer.addByte(r.getSlot(client));
        //          announcePlayer.addStringNulTerminated(character.getName());
        //          announcePlayer.addInt32(character.getLevel());
        //          announcePlayer.addInt32(7); // packet.AddByteString("01 00 00 00"); --7
        //          announcePlayer.addByte(character.getSex().getNumValue());
        //          announcePlayer.addByte(1);
        //          announcePlayer.addByte(character.getTeam());
        //          announcePlayer.addByte(character.getReady());
        //          announcePlayer.addInt32(character.getWeight());
        //          announcePlayer.addByte(0);
        //          announcePlayer.addByte(0xff); // Avatar Skin
        //          announcePlayer.addByte(character.getDirection());
        //          announcePlayer.addByte(1);
        //          announcePlayer.addFloat(character.getX());
        //          announcePlayer.addFloat(character.getY());
        //          announcePlayer.addFloat(character.getZ());
        //          //announcePlayer.addStringNulTerminated(group.Name);
        //          announcePlayer.addByte(0);
        //          announcePlayer.addInt16(0xff); //group.Icon);
        //          //announcePlayer.addByte(0);
        //          announcePlayer.addInt32(2147483647);
        //          announcePlayer.addInt32(0xdbba2); //Head
        //          announcePlayer.addInt32(0xdbba3); //Chest
        //          announcePlayer.addInt32(0xdbba4); //Legs
        //          announcePlayer.addInt32(0xdbba6); //Shoes
        //          //announcePlayer.addInt32(2147483647);
        //          announcePlayer.addInt32(2147483647); //Hair
        //          announcePlayer.addInt32(2147483647); //Top
        //          announcePlayer.addInt32(2147483647); //Pants
        //          announcePlayer.addInt32(2147483647); //Gloves
        //          announcePlayer.addInt32(2147483647); //Boots
        //          announcePlayer.addInt32(2147483647); //Face
        //          announcePlayer.addInt32(2147483647); //Glasses
        //          announcePlayer.addInt32(2147483647);
        //          announcePlayer.addInt32(2147483647);
        //          announcePlayer.addInt32(2147483647);
        //          announcePlayer.addInt32(2147483647);
        //          //announcePlayer.addInt32(0x1b);
        //          announcePlayer.addByte(0);
        //          announcePlayer.addByte(0);
        //          announcePlayer.addByte(0);

        //          sendToAll(announcePlayer);
        //          break;
        //      }
        //  }


        //  return new SendPacket[0];
    }
}
