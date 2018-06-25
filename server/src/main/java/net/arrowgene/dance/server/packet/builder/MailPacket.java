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


import net.arrowgene.dance.library.models.item.ItemDurationType;
import net.arrowgene.dance.library.models.item.ShopItem;
import net.arrowgene.dance.library.models.mail.Mail;
import net.arrowgene.dance.server.packet.Packet;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.SendPacket;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MailPacket {

    private static MailPacket instance = new MailPacket();

    public static MailPacket getInstance() {
        return instance;
    }

    public Packet getReadMailPacket(Mail mail) {
        return this.getReadMailPacket(mail, null);
    }

    public Packet getReadMailPacket(Mail mail, ShopItem gift) {

        Packet packet = new SendPacket(PacketType.MAIL_RESPONSE_READ_MAIL);
        if (mail != null) {
            packet.addInt32(mail.getId());
            packet.addInt32(0); //Error Code ?
            packet.addStringNulTerminated(mail.getBody());
            if (gift != null) {
                packet.addInt32(gift.getModelId());
            } else {
                packet.addInt32(Mail.NO_GIFT);
            }
            packet.addByte(0);
            packet.addByte(0);
            packet.addByte(0);
            packet.addByte(0);
            packet.addByte(1); // Needs to be 1 for Message Button
            packet.addByte(0);
            packet.addByte(0);
            if (gift != null) {
                String expires = this.generateExpireDay(gift);
                packet.addStringNulTerminated(expires);
                packet.addInt32(gift.getMinLevel());
            } else {
                packet.addStringNulTerminated("");
                packet.addInt32(0);
            }
            packet.addByte(mail.getType().getNumValue());
        } else {
            packet.addInt32(0);
        }
        packet.addByte(0);
        return packet;
    }


    public Packet getMailListPacket(List<Mail> mails) {

        Packet packet = new SendPacket(PacketType.MAIL_RESPONSE_MAIL_LIST);

        packet.addInt32(mails.size());
        packet.addByte(0);

        for (Mail mail : mails) {
            packet.addInt32(mail.getId());
            packet.addStringNulTerminated(mail.getSenderCharacterName());
            packet.addStringNulTerminated(mail.getSubject());
            packet.addStringNulTerminated(mail.getDateString());
            packet.addByte(mail.isRead() ? 1 : 0);
            packet.addInt32(0x7fffffff);
        }

        packet.addByte(0);

        return packet;
    }


    public Packet getSendMailPacket(boolean success) {

        Packet packet = new SendPacket(PacketType.MAIL_RESPONSE_SEND_MAIL);
        if (success) {
            packet.addInt32(0);
        } else {
            packet.addInt32(1);
        }
        packet.addByte(0);
        return packet;
    }

    private String generateExpireDay(ShopItem item) {

        if (item.getDuration() == ItemDurationType.PERMANENT) {
            return "PERMANENT";
        } else {
            Calendar c = Calendar.getInstance();
            switch (item.getDuration()) {
                case ZERO:
                    c.add(Calendar.DATE, 0);
                    break;
                case SEVEN:
                    c.add(Calendar.DATE, 7);
                    break;
                case THIRTY:
                    c.add(Calendar.DATE, 30);
                    break;
            }
            Format formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(c.getTimeInMillis());
        }
    }

}
