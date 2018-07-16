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

import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.mail.Mail;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.packet.builder.MailPacket;


public class _6004_x1774_MAIL_REQUEST_SEND_MAIL extends HandlerBase {

    public _6004_x1774_MAIL_REQUEST_SEND_MAIL(DanceServer server) {
        super(server);
    }


    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        String recipientName = packet.getStringNulTerminated();
        String subject = packet.getStringNulTerminated();
        String body = packet.getStringNulTerminated();

        Character recipient = super.server.getCharacterManager().getCharacterByName(recipientName);

        Packet answerPacket;

        if (recipient != null) {

            Mail mail = new Mail();
            mail.setBody(body);
            mail.setSubject(subject);
            mail.setReceiverId(recipient.getId());
            mail.setSenderId(client.getCharacter().getId());
            mail.setSenderCharacterName(client.getCharacter().getName());
            super.server.getPostOffice().sendMail(mail);

            answerPacket = MailPacket.getInstance().getSendMailPacket(true);
        } else {
            answerPacket = MailPacket.getInstance().getSendMailPacket(false);
        }

        client.sendPacket(answerPacket);
        return null;
    }
}
