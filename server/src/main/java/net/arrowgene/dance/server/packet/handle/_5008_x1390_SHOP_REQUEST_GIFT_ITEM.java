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

import net.arrowgene.dance.library.models.mail.MailType;
import net.arrowgene.dance.server.client.DanceClient;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.packet.PacketType;
import net.arrowgene.dance.server.packet.ReadPacket;
import net.arrowgene.dance.server.packet.SendPacket;
import net.arrowgene.dance.server.shop.ShopMessages;


public class _5008_x1390_SHOP_REQUEST_GIFT_ITEM extends HandlerBase {

    public _5008_x1390_SHOP_REQUEST_GIFT_ITEM(DanceServer server) {
        super(server);
    }

    @Override
    public SendPacket[] handle(ReadPacket packet, DanceClient client) {

        String receiverName = packet.getStringNulTerminated();
        String title = packet.getStringNulTerminated();
        String message = packet.getStringNulTerminated();

        int itemId = packet.getInt32();

        // TODO Pay for Item

        boolean bought = true;

        this.server.getPostOffice().sendMail(client.getCharacter().getName(), receiverName, title, message, itemId, MailType.NONE);


        SendPacket answerPacket = new SendPacket(PacketType.SHOP_RESPONSE_GIFT_ITEM);
        if (bought) {
            answerPacket.addInt32(ShopMessages.MSG_NO_ERROR.getNumValue());
        } else {
            answerPacket.addInt32(0xfffffff8);
        }
        answerPacket.addByte(0);

        client.sendPacket(answerPacket);

        return null;
    }
}
