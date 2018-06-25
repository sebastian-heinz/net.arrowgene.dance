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

package net.arrowgene.dance.library.models.mail;


import java.text.Format;
import java.text.SimpleDateFormat;


public class Mail {

    public static final int NO_GIFT = 0x7fffffff;

    private boolean read;
    private int id;
    private int senderId;
    private int receiverId;
    private int giftItemId;
    private long date;
    private String senderCharacterName;
    private SpecialSenderType specialSender;
    private String body;
    private String subject;
    private MailType type;

    public Mail() {
        this.id = -1;
        this.type = MailType.NONE;
        this.date = 0;
        this.giftItemId = NO_GIFT;
        this.read = false;
        this.specialSender = SpecialSenderType.NONE;
    }

    public String getDateString() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(this.date * 1000);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getGiftItemId() {
        return giftItemId;
    }

    public void setGiftItemId(int giftItemId) {
        this.giftItemId = giftItemId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public MailType getType() {
        return type;
    }

    public void setType(MailType type) {
        this.type = type;
    }

    public SpecialSenderType getSpecialSender() {
        return specialSender;
    }

    public void setSpecialSender(SpecialSenderType specialSender) {
        this.specialSender = specialSender;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getSenderCharacterName() {
        switch (this.specialSender) {
            case LOVE_MAGISTRATE:
                return "Love Magistrate";
            default:
                return this.senderCharacterName;
        }
    }

    public void setSenderCharacterName(String senderCharacterName) {
        this.senderCharacterName = senderCharacterName;
    }

}
