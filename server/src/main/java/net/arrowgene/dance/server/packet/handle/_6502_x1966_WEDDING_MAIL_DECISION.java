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
import net.arrowgene.dance.server.wedding.*;

/**
 * The user clicked the "Accept"- or "Reject"-button in an mail for wedding or divorce request.
 */
public class _6502_x1966_WEDDING_MAIL_DECISION extends HandlerBase {


    public _6502_x1966_WEDDING_MAIL_DECISION(DanceServer server) {
        super(server);
    }


    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {


        int mailDecision = packet.getByte();
        int accepted = packet.getByte();
        String partnerName = packet.getStringNulTerminated();
        int mailId = packet.getInt32();
        int d = packet.getByte(); // Ring ? 100 ?

        WeddingDecisionType decision = WeddingDecisionType.getType(mailDecision);
        boolean isAccepted = accepted == 1;

        int characterId = client.getCharacter().getCharacterId();

        SendPacket answerPacket = new SendPacket(PacketType.WEDDING_RESPONSE_MAIL_DECISION);
        answerPacket.addByte(decision.getNumValue());
        answerPacket.addByte(accepted);
        if (decision == WeddingDecisionType.DIVORCE) {
            if (isAccepted) {
                AcceptDivorceMsg acceptDivorceMessage = this.server.getLoveMagistrate().acceptDivorce(characterId);
                answerPacket.addInt32(acceptDivorceMessage.getNumValue());
            } else {
                RejectDivorceMsg rejectDivorceMessage = this.server.getLoveMagistrate().rejectDivorce(characterId);
                answerPacket.addInt32(rejectDivorceMessage.getNumValue());
            }
        } else if (decision == WeddingDecisionType.PROPOSE) {
            if (isAccepted) {
                AcceptProposeMsg acceptProposeMessage = this.server.getLoveMagistrate().acceptPropose(characterId);
                answerPacket.addInt32(acceptProposeMessage.getNumValue());
            } else {
                RejectProposeMsg rejectProposeMessage = this.server.getLoveMagistrate().rejectPropose(characterId);
                answerPacket.addInt32(rejectProposeMessage.getNumValue());
            }
        }

       this.server.getPostOffice().deleteMail(mailId);

        answerPacket.addByte(0);
        client.sendPacket(answerPacket);

        return null;
    }
}
