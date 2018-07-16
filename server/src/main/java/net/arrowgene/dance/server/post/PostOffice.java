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

package net.arrowgene.dance.server.post;

import net.arrowgene.dance.library.models.character.Character;
import net.arrowgene.dance.library.models.mail.Mail;
import net.arrowgene.dance.library.models.mail.MailType;
import net.arrowgene.dance.library.models.mail.Mailbox;
import net.arrowgene.dance.library.models.mail.SpecialSenderType;
import net.arrowgene.dance.server.DanceServer;
import net.arrowgene.dance.server.ServerComponent;
import net.arrowgene.dance.server.client.DanceClient;

import java.util.List;

public class PostOffice extends ServerComponent {

    public PostOffice(DanceServer server) {
        super(server);
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void clientAuthenticated(DanceClient client) {
        if (client.getCharacter() != null) {
            Mailbox mailbox = this.getMailboxByCharacterId(client.getCharacter().getId());
            client.setMailbox(mailbox);
        }
    }

    @Override
    public void clientDisconnected(DanceClient client) {
        if (client.getCharacter() != null) {
            super.getDatabase().syncMails(client.getCharacter().getId(), client.getMailbox().getMails());
        }
    }

    @Override
    public void clientConnected(DanceClient client) {

    }

    @Override
    public void writeDebugInfo() {

    }

    public void sendMail(String senderName, String recipientName, String subject, String body) {
        this.sendMail(senderName, recipientName, subject, body, 0, MailType.NONE);
    }

    public void sendMail(String senderName, String recipientName, String subject, String body, int giftItemId,
                         MailType type) {

        Character recipient = super.server.getCharacterManager().getCharacterByName(recipientName);
        Character sender = super.server.getCharacterManager().getCharacterByName(senderName);

        Mail mail = new Mail();
        mail.setBody(body);
        mail.setSubject(subject);
        mail.setReceiverId(recipient.getId());
        mail.setSenderId(sender.getId());
        mail.setSenderCharacterName(sender.getName());

        if (giftItemId >= 0) {
            mail.setGiftItemId(giftItemId);
        }

        mail.setType(type);

        this.sendMail(mail);
    }

    public void sendMailPropose(int senderCharacterId, int recipientCharacterId, String body, int giftItemId) {

        Character recipient = super.server.getCharacterManager().getCharacterById(recipientCharacterId);
        Character sender = super.server.getCharacterManager().getCharacterById(senderCharacterId);

        Mail mail = new Mail();
        mail.setBody(body);
        mail.setSubject(sender.getName());
        mail.setReceiverId(recipient.getId());
        mail.setSenderId(sender.getId());
        mail.setType(MailType.PROPOSE_REQUEST);
        mail.setSpecialSender(SpecialSenderType.LOVE_MAGISTRATE);
        mail.setGiftItemId(giftItemId);
        this.sendMail(mail);
    }

    public void sendMailDivorce(int senderCharacterId, int recipientCharacterId, String body) {

        Character recipient = super.server.getCharacterManager().getCharacterById(recipientCharacterId);
        Character sender = super.server.getCharacterManager().getCharacterById(senderCharacterId);

        Mail mail = new Mail();
        mail.setBody(body);
        mail.setSubject(sender.getName());
        mail.setReceiverId(recipient.getId());
        mail.setSenderId(sender.getId());
        mail.setType(MailType.DIVORCE_REQUEST);
        mail.setSpecialSender(SpecialSenderType.LOVE_MAGISTRATE);
        this.sendMail(mail);
    }

    public void sendProposeExpireNotice(int senderCharacterId, int recipientCharacterId) {

        Character recipient = super.server.getCharacterManager().getCharacterById(recipientCharacterId);
        Character sender = super.server.getCharacterManager().getCharacterById(senderCharacterId);

        Mail mail = new Mail();
        mail.setBody("Your propose to " + sender.getName() + " expired");
        mail.setSubject("Propose Notice");
        mail.setReceiverId(recipient.getId());
        mail.setSenderId(sender.getId());
        mail.setSenderCharacterName(sender.getName());
        mail.setSpecialSender(SpecialSenderType.LOVE_MAGISTRATE);
        this.sendMail(mail);

        Mail mail1 = new Mail();
        mail1.setBody("The propose from " + recipient.getName() + " expired because you tried to propose again or to someone else. Please check 'My Application' for current status if you are unsure.");
        mail1.setSubject("Propose Notice");
        mail1.setReceiverId(sender.getId());
        mail1.setSenderId(recipient.getId());
        mail1.setSpecialSender(SpecialSenderType.LOVE_MAGISTRATE);
        mail1.setSenderCharacterName(recipient.getName());
        this.sendMail(mail1);
    }

    /**
     * Sends a mail.
     * <p>
     * The mail needs to be stored instantly and can't be saved on server / player exit.
     * This is because we need to give the mail an ID.
     *
     * @param mail The mail-object to send.
     */
    public void sendMail(Mail mail) {


        mail.setDate(DanceServer.getUnixTimeNow());
        super.getDatabase().insertMail(mail);

        DanceClient receiver = super.server.getClientController().getClientByCharacterId(mail.getReceiverId());
        if (receiver != null) {
            receiver.getMailbox().addMail(mail);
        }
    }

    public void deleteMail(int mailId) {
        Mail mail = super.getDatabase().getMail(mailId);

        if (mail != null) {
            DanceClient receiver = super.server.getClientController().getClientByCharacterId(mail.getReceiverId());
            if (receiver != null) {
                receiver.getMailbox().removeMail(mailId);
            }
            super.getDatabase().deleteMail(mailId);
        }
    }


    public Mailbox getMailboxByCharacterId(int characterId) {
        List<Mail> mails = super.getDatabase().getMails(characterId);
        Mailbox mailbox = new Mailbox();
        mailbox.addMails(mails);
        return mailbox;
    }

}
