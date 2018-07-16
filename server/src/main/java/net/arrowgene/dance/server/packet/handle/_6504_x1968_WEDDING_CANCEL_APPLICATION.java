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
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.wedding.CancelDivorceMsg;
import net.arrowgene.dance.server.wedding.CancelProposeMsg;
import net.arrowgene.dance.server.wedding.WeddingDecisionType;


/**
 * 'Cancel Marriage'- or 'Cancel Divorce'-button inside the Wedding-menu on the map was clicked.
 */
public class _6504_x1968_WEDDING_CANCEL_APPLICATION extends HandlerBase {


    public _6504_x1968_WEDDING_CANCEL_APPLICATION(DanceServer server) {
        super(server);
    }


    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {
        int cancelDecision = packet.getByte();

        WeddingDecisionType decision = WeddingDecisionType.getType(cancelDecision);

        SendPacket answerPacket = new SendPacket(PacketType.WEDDING_RESPONSE_CANCEL_APPLICATION);
        answerPacket.addByte(decision.getNumValue());

        if (decision == WeddingDecisionType.DIVORCE) {
            CancelDivorceMsg cancelDivorceMessage = this.server.getLoveMagistrate().cancelDivorce(client.getCharacter().getId());
            answerPacket.addInt32(cancelDivorceMessage.getNumValue());
        } else if (decision == WeddingDecisionType.PROPOSE) {
            CancelProposeMsg cancelProposeMessage = this.server.getLoveMagistrate().cancelPropose(client.getCharacter().getId());
            answerPacket.addInt32(cancelProposeMessage.getNumValue());
        }

        answerPacket.addByte(0);
        client.sendPacket(answerPacket);

        return null;
    }
}
