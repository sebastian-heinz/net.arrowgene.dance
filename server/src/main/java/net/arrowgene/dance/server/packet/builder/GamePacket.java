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


import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.song.Song;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.game.ScoreUser;
import net.arrowgene.dance.server.game.Scores;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.room.Room;

import java.util.Calendar;

public class GamePacket {

    private static GamePacket instance = new GamePacket();

    public static GamePacket getInstance() {
        return instance;
    }

    public Packet getGameEndedPacket(DanceClient client, Room room) {
        SendPacket answerPacket = new SendPacket(PacketType.GAME_RESPONSE_GAME_ENDED);
        answerPacket.addInt32(room.getSlot(client));

        return answerPacket;
    }

    public Packet getFinalGameStatsPacket(Room room, Scores scores, DanceClient[] winningOrder) {
        SendPacket answerPacket = new SendPacket(PacketType.GAME_RESPONSE_SEND_FINALSCREEN);
        answerPacket.addInt32(room.getActiveCount());

        for (DanceClient c : room.getActiveClients()) {
            if (c != null) {
                ScoreUser userScore = scores.get(c);
                answerPacket.addByte(room.getSlot(c));

                answerPacket.addInt32(userScore.getPerfects());
                answerPacket.addInt32(userScore.getCools());
                answerPacket.addInt32(userScore.getBads());
                answerPacket.addInt32(userScore.getMisses());
                answerPacket.addInt32(userScore.getMaxCombo());
                answerPacket.addInt32(userScore.getPoints());

                answerPacket.addByte(userScore.getPlace());
                answerPacket.addInt32(userScore.getCharacterExperience()); //experienceEarned
                answerPacket.addInt32(userScore.getCharacterPoints()); //Points Earned
                if(c.getCharacter().levelUpOnLastExperienceChange())
                {
                    System.out.println(c.getCharacter().getName()+ " has Level Up");
                }
                answerPacket.addByte(c.getCharacter().levelUpOnLastExperienceChange()?1:0);//leveled
                answerPacket.addByte(0);
                answerPacket.addByte(0);
                answerPacket.addByte(0);
                answerPacket.addByte(0);
                answerPacket.addByte(0);
            }
        }
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getGameStatsPacket(DanceClient client, Room room, ScoreUser stats) {
        SendPacket answerPacket = new SendPacket(PacketType.GAME_RESPONSE_GAME_STATS);

        answerPacket.addByte(room.getSlot(client));
        answerPacket.addInt32(stats.getPacketId());
        answerPacket.addInt32(stats.getUnknown());
        answerPacket.addInt32(stats.getPoints()); //Points?
        answerPacket.addInt32(stats.getEnergy());
        answerPacket.addInt32(stats.getPerfects());
        answerPacket.addInt32(stats.getCools());
        answerPacket.addInt32(stats.getBads());
        answerPacket.addInt32(stats.getMisses());
        answerPacket.addByte(0);
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getChangeTeamPacket(DanceClient client, Room room) {
        Packet answerPacket = new SendPacket(PacketType.GAME_RESPONSE_CHANGE_TEAM);

        answerPacket.addByte(room.getSlot(client));
        answerPacket.addInt32(client.getCharacter().getTeam().getNumValue());
        answerPacket.addByte(0);

        return answerPacket;
    }

    public Packet getLoadingReadyPacket() {
        Packet answerPacket = new SendPacket(PacketType.GAME_RESPONSE_LOADING_READY);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int dayMillis = (int) (System.currentTimeMillis() - c.getTimeInMillis());

        answerPacket.addInt32(0);
        answerPacket.addInt32(dayMillis);//Zeit in Millisekunden (Uhrzeit)
        //answerPacket.addHEXString("d6991e1e00");
        answerPacket.addByte(0);
        answerPacket.addByte(0);

        answerPacket.addByte(0);

        return answerPacket;
    }
}
