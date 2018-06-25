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

package net.arrowgene.dance.server.chat;

import net.arrowgene.dance.server.client.DanceClient;

import java.util.Date;


public class ChatMessage {

    private String message;
    private DanceClient recipient;
    private DanceClient sender;
    private String senderCharacterName;
    private String recipientCharacterName;
    private Date date;
    private ChatType chatType;
    private boolean deliver;

    public ChatMessage(String message, DanceClient sender, String senderCharacterName, ChatType chatType) {
        this(message, sender, senderCharacterName, chatType, null, null);
    }

    public ChatMessage(String message, DanceClient sender, String senderCharacterName, ChatType chatType, String recipientCharacterName, DanceClient recipient) {
        this.date = new Date();
        this.deliver = true;
        this.message = message;
        this.sender = sender;
        this.chatType = chatType;
        this.recipient = recipient;
        this.recipientCharacterName = recipientCharacterName;
        this.senderCharacterName = senderCharacterName;
    }

    /**
     * Prevent the message from delivery.
     * This can not be reversed, when any middleware decides
     * to prevent delivery the message should be dropped.
     */
    public void StopDelivery() {
        this.deliver = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DanceClient getSender() {
        return sender;
    }

    public void setSender(DanceClient sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public boolean isDeliver() {
        return deliver;
    }

    public DanceClient getRecipient() {
        return recipient;
    }

    public void setRecipient(DanceClient recipient) {
        this.recipient = recipient;
    }

    public String getRecipientCharacterName() {
        return recipientCharacterName;
    }

    public void setRecipientCharacterName(String recipientCharacterName) {
        this.recipientCharacterName = recipientCharacterName;
    }

    public String getSenderCharacterName() {
        return senderCharacterName;
    }

    public void setSenderCharacterName(String senderCharacterName) {
        this.senderCharacterName = senderCharacterName;
    }
}
