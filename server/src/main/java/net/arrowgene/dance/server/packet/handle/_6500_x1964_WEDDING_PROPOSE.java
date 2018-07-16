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
import net.arrowgene.dance.server.wedding.DivorceMsg;
import net.arrowgene.dance.server.wedding.ProposeMsg;
import net.arrowgene.dance.library.models.wedding.RingType;
import net.arrowgene.dance.server.wedding.WeddingDecisionType;


/**
 * The user clicked the "Send"-button in the menu of the "Get married"- or "Get Divorced"-button.
 * This handles a wedding or divorce request.
 */
public class _6500_x1964_WEDDING_PROPOSE extends HandlerBase {

    public _6500_x1964_WEDDING_PROPOSE(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        int weddingDecision = packet.getByte();
        String partnerName = packet.getStringNulTerminated();
        String message = packet.getStringNulTerminated();

        WeddingDecisionType decision = WeddingDecisionType.getType(weddingDecision);

        SendPacket answerPacket = new SendPacket(PacketType.WEDDING_RESPONSE_PROPOSE);
        answerPacket.addByte(decision.getNumValue());

        if(decision == WeddingDecisionType.PROPOSE){
            int ringIdentification = packet.getByte();
            RingType ringType = RingType.getType(ringIdentification);
            ProposeMsg response = this.server.getLoveMagistrate().propose(client, partnerName, message, ringType);
            answerPacket.addInt32(response.getNumValue());
        } else if (decision == WeddingDecisionType.DIVORCE){
            DivorceMsg divorceMessage = this.server.getLoveMagistrate().divorce(client.getCharacter().getId(), message);
            answerPacket.addInt32(divorceMessage.getNumValue());
        }

        answerPacket.addByte(0);
        client.sendPacket(answerPacket);

        return null;
    }

}
