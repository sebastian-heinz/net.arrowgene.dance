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
import net.arrowgene.dance.server.game.GameStatsData;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;


public class _4014_xFAE_GAME_REQUEST_GAME_STATS extends HandlerBase {

    public _4014_xFAE_GAME_REQUEST_GAME_STATS(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        GameStatsData stats = new GameStatsData(packet);

        client.getRoom().setGameStats(client, stats);

        return null;

        // packet.getInt32(); // slot, glaub ich aber nicht
        // int unknown = packet.getInt32(); // unbekannt

        // ScoreUser userScore = score.get(client);

        // userScore.addPerfects(packet.getInt32());
        // userScore.addCools(packet.getInt32());
        // userScore.addBads(packet.getInt32());
        // userScore.addMisses(packet.getInt32());
        // userScore.setMaxCombo(packet.getInt32());
        // userScore.setEnergy(packet.getInt32());

        // //System.out.println(userScore.toString());

        // Packet answerPacket = new Packet(PacketType.GAME_RESPONSE_GAME_STATS);
        // answerPacket.addByte(getSlot(client));
        // answerPacket.addInt32(20);
        // answerPacket.addInt32(40);
        // answerPacket.addInt32(userScore.getPoints()); //Points?
        // answerPacket.addInt32(userScore.getEnergy());
        // answerPacket.addInt32(userScore.getPerfects());
        // answerPacket.addInt32(userScore.getCools());
        // answerPacket.addInt32(userScore.getBads());
        // answerPacket.addInt32(userScore.getMisses());
        // answerPacket.addByte(0);

        // //System.out.println(answerPacket);
        // sendToAll(answerPacket);
        // return new SendPacket[0];
    }
}
