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
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

import java.util.Calendar;


public class _2029_x7ED_LOBBY_REQUEST_PING extends HandlerBase {

    public _2029_x7ED_LOBBY_REQUEST_PING(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int dayMillis = (int) (System.currentTimeMillis() - c.getTimeInMillis());

        /*long timeDifference = new GregorianCalendar().getTimeInMillis() - lastPingDateTime;

        if (lastPingTime == -1) {
            Random rand = new Random();
            lastPingTime = rand.nextInt();
        }
        lastPingDateTime = new GregorianCalendar().getTimeInMillis();
        lastPingTime += (int) (timeDifference);
*/
        int data = packet.getInt32();

        Packet answerPacket = new SendPacket(PacketType.LOBBY_RESPONSE_PING);
        answerPacket.addInt32(data);
        answerPacket.addInt32(dayMillis);
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);

        return null;
    }
}
