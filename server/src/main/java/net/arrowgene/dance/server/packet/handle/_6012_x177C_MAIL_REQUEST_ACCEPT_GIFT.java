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

import net.arrowgene.dance.library.models.item.ShopItem;
import net.arrowgene.dance.library.models.mail.Mail;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;

/**
 * The user clicked the "Accept"-button in a mail.
 */
public class _6012_x177C_MAIL_REQUEST_ACCEPT_GIFT extends HandlerBase {

    public static final int MSG_YOUR_STORAGE_IS_FULL = 0x7fffffff;
    public static final int MSG_GIFT_ACCEPTED = 0;

    public _6012_x177C_MAIL_REQUEST_ACCEPT_GIFT(DanceServer server) {
        super(server);
    }


    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        int mailId = packet.getInt32();
        SendPacket answerPacket = new SendPacket(PacketType.MAIL_RESPONSE_ACCEPT_GIFT);
        answerPacket.addInt32(mailId);

        Mail mail = client.getMailbox().getMail(mailId);

        if (mail != null) {
            ShopItem gift = super.server.getShop().getShopItem(mail.getGiftItemId());
            // TODO add gift to inventory and remove from mail, check if storage is available.
            answerPacket.addInt32(MSG_GIFT_ACCEPTED);
        } else {
            answerPacket.addInt32(MSG_YOUR_STORAGE_IS_FULL);
        }

        answerPacket.addByte(0);

        client.sendPacket(answerPacket);

        return null;
    }
}
