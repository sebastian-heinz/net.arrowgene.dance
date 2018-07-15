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

package net.arrowgene.dance.database.maria.modules;

import net.arrowgene.dance.database.maria.MariaDbController;
import net.arrowgene.dance.database.maria.MariaDbFactory;
import net.arrowgene.dance.library.models.mail.Mail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MariaDbMail {

    private MariaDbController controller;
    private MariaDbFactory factory;

    public MariaDbMail(MariaDbController controller, MariaDbFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public Mail getMail(int mailId) throws SQLException {
        Mail mail = null;
        PreparedStatement select = controller.createPreparedStatement("SELECT " +
            "dance_mail.id, " +
            "dance_mail.sender_id, " +
            "dance_mail.receiver_id, " +
            "dance_mail.gift_item_id, " +
            "dance_mail.date, " +
            "dance_mail.body, " +
            "dance_mail.subject, " +
            "dance_mail.type, " +
            "dance_mail.read, " +
            "dance_mail.special_sender, " +
            "sender.character_name AS sender_character_name " +
            "FROM dance_mail " +
            "INNER JOIN dance_character AS sender ON dance_mail.sender_id = sender.id " +
            "WHERE id=?;");
        select.setInt(1, mailId);
        ResultSet res = select.executeQuery();
        if (res.next()) {
            mail = factory.createMail(res);
        }
        res.close();
        select.close();
        return mail;
    }

    public List<Mail> getMails(int characterId) throws SQLException {
        List<Mail> mails = new ArrayList<Mail>();
        PreparedStatement select = controller.createPreparedStatement("SELECT " +
            "dance_mail.id, " +
            "dance_mail.sender_id, " +
            "dance_mail.receiver_id, " +
            "dance_mail.gift_item_id, " +
            "dance_mail.date, " +
            "dance_mail.body, " +
            "dance_mail.subject, " +
            "dance_mail.type, " +
            "dance_mail.read, " +
            "dance_mail.special_sender, " +
            "sender.character_name AS sender_character_name " +
            "FROM dance_mail " +
            "INNER JOIN dance_character AS sender ON dance_mail.sender_id = sender.id " +
            "WHERE receiver_id=?;");
        select.setInt(1, characterId);
        ResultSet res = select.executeQuery();
        while (res.next()) {
            Mail mail = factory.createMail(res);
            mails.add(mail);
        }
        res.close();
        select.close();
        return mails;
    }

    public void insertMail(Mail mail) throws SQLException {
        if (mail.getId() > -1) {
            PreparedStatement update = controller.createPreparedStatement("UPDATE dance_mail SET " +
                "sender_id=?, " +
                "receiver_id=?, " +
                "gift_item_id=?, " +
                "date=?, " +
                "body=?, " +
                "subject=?, " +
                "type=?, " +
                "read=?, " +
                "special_sender=? " +
                "WHERE id=?;");
            update.setInt(1, mail.getSenderId());
            update.setInt(2, mail.getReceiverId());
            update.setInt(3, mail.getGiftItemId());
            update.setLong(4, mail.getDate());
            update.setString(5, mail.getBody());
            update.setString(6, mail.getSubject());
            update.setByte(7, mail.getType().getNumValue());
            update.setBoolean(8, mail.isRead());
            update.setByte(9, mail.getSpecialSender().getNumValue());
            update.setInt(10, mail.getId());
            update.execute();
            update.close();
        } else {
            PreparedStatement insert = controller.createPreparedStatement("INSERT INTO dance_mail VALUES (?,?,?,?,?,?,?,?,?,?);");
            insert.setInt(2, mail.getSenderId());
            insert.setInt(3, mail.getReceiverId());
            insert.setInt(4, mail.getGiftItemId());
            insert.setLong(5, mail.getDate());
            insert.setString(6, mail.getBody());
            insert.setString(7, mail.getSubject());
            insert.setByte(8, mail.getType().getNumValue());
            insert.setBoolean(9, mail.isRead());
            insert.setByte(10, mail.getSpecialSender().getNumValue());
            insert.execute();
            int id = controller.getAutoIncrement(insert);
            mail.setId(id);
            insert.close();
        }
    }

    public void insertMails(List<Mail> mails) throws SQLException {
        PreparedStatement insert = controller.createPreparedStatement("INSERT INTO dance_mail VALUES (?,?,?,?,?,?,?,?,?,?);");
        PreparedStatement update = controller.createPreparedStatement("UPDATE dance_mail SET " +
            "sender_id=?, " +
            "receiver_id=?, " +
            "gift_item_id=?, " +
            "date=?, " +
            "body=?, " +
            "subject=?, " +
            "type=?, " +
            "read=?, " +
            "special_sender=? " +
            "WHERE id=?;");
        for (Mail mail : mails) {
            if (mail.getId() > -1) {
                update.clearParameters();
                update.setInt(1, mail.getSenderId());
                update.setInt(2, mail.getReceiverId());
                update.setInt(3, mail.getGiftItemId());
                update.setLong(4, mail.getDate());
                update.setString(5, mail.getBody());
                update.setString(6, mail.getSubject());
                update.setByte(7, mail.getType().getNumValue());
                update.setBoolean(8, mail.isRead());
                update.setByte(9, mail.getSpecialSender().getNumValue());
                update.setInt(10, mail.getId());
                update.execute();
            } else {
                insert.clearParameters();
                insert.setInt(2, mail.getSenderId());
                insert.setInt(3, mail.getReceiverId());
                insert.setInt(4, mail.getGiftItemId());
                insert.setLong(5, mail.getDate());
                insert.setString(6, mail.getBody());
                insert.setString(7, mail.getSubject());
                insert.setByte(8, mail.getType().getNumValue());
                insert.setBoolean(9, mail.isRead());
                insert.setByte(10, mail.getSpecialSender().getNumValue());
                insert.execute();
                int id = controller.getAutoIncrement(insert);
                mail.setId(id);
            }
        }
        insert.close();
        update.close();
    }

    public void deleteMail(int mailId) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM dance_mail WHERE id=?");
        delete.setInt(1, mailId);
        delete.execute();
        delete.close();
    }

    public void deleteMails(List<Mail> mails) throws SQLException {
        PreparedStatement delete = controller.createPreparedStatement("DELETE FROM dance_mail WHERE id=?");
        for (Mail mail : mails) {
            delete.clearParameters();
            delete.setInt(1, mail.getId());
            delete.execute();
        }
        delete.close();
    }
}
