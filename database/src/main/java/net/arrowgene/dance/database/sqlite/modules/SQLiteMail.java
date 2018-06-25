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

package net.arrowgene.dance.database.sqlite.modules;

import net.arrowgene.dance.database.sqlite.SQLiteController;
import net.arrowgene.dance.database.sqlite.SQLiteFactory;
import net.arrowgene.dance.library.models.mail.Mail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteMail {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteMail(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }


    public Mail getMail(int mailId) throws SQLException {
        Mail mail = null;
        PreparedStatement select = this.controller.createPreparedStatement("SELECT " +
            "ag_mail.mail_id, " +
            "ag_mail.mail_sender_id, " +
            "ag_mail.mail_receiver_id, " +
            "ag_mail.mail_gift_item_id, " +
            "ag_mail.mail_date, " +
            "ag_mail.mail_body, " +
            "ag_mail.mail_subject, " +
            "ag_mail.mail_type, " +
            "ag_mail.mail_read, " +
            "ag_mail.mail_special_sender, " +
            "sender.character_name AS sender_character_name " +
            "FROM ag_mail " +
            "INNER JOIN ag_character AS sender ON ag_mail.mail_sender_id = sender.character_id " +
            "WHERE mail_id=?;");
        select.setInt(1, mailId);

        ResultSet res = select.executeQuery();
        if (res.next()) {
            mail = this.factory.createMail(res);
        }

        res.close();
        select.close();

        return mail;
    }

    public List<Mail> getMails(int characterId) throws SQLException {
        List<Mail> mails = new ArrayList<Mail>();

        PreparedStatement select = this.controller.createPreparedStatement("SELECT " +
            "ag_mail.mail_id, " +
            "ag_mail.mail_sender_id, " +
            "ag_mail.mail_receiver_id, " +
            "ag_mail.mail_gift_item_id, " +
            "ag_mail.mail_date, " +
            "ag_mail.mail_body, " +
            "ag_mail.mail_subject, " +
            "ag_mail.mail_type, " +
            "ag_mail.mail_read, " +
            "ag_mail.mail_special_sender, " +
            "sender.character_name AS sender_character_name " +
            "FROM ag_mail " +
            "INNER JOIN ag_character AS sender ON ag_mail.mail_sender_id = sender.character_id " +
            "WHERE mail_receiver_id=?;");
        select.setInt(1, characterId);

        ResultSet res = select.executeQuery();
        while (res.next()) {
            Mail mail = this.factory.createMail(res);
            mails.add(mail);
        }

        res.close();
        select.close();

        return mails;
    }

    public void insertMail(Mail mail) throws SQLException {

        if (mail.getId() > -1) {
            PreparedStatement update = this.controller.createPreparedStatement("UPDATE ag_mail SET " +
                "mail_sender_id=?, " +
                "mail_receiver_id=?, " +
                "mail_gift_item_id=?, " +
                "mail_date=?, " +
                "mail_body=?, " +
                "mail_subject=?, " +
                "mail_type=?, " +
                "mail_read=?, " +
                "mail_special_sender=? " +
                "WHERE mail_id=?;");
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
            PreparedStatement insert = this.controller
                .createPreparedStatement("INSERT INTO ag_mail VALUES (?,?,?,?,?,?,?,?,?,?);");
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

            int id = this.controller.getAutoIncrement(insert);
            mail.setId(id);

            insert.close();
        }
    }

    public void insertMails(List<Mail> mails) throws SQLException {
        PreparedStatement insert = this.controller
            .createPreparedStatement("INSERT INTO ag_mail VALUES (?,?,?,?,?,?,?,?,?,?);");
        PreparedStatement update = this.controller.createPreparedStatement("UPDATE ag_mail SET " +
            "mail_sender_id=?, " +
            "mail_receiver_id=?, " +
            "mail_gift_item_id=?, " +
            "mail_date=?, " +
            "mail_body=?, " +
            "mail_subject=?, " +
            "mail_type=?, " +
            "mail_read=?, " +
            "mail_special_sender=? " +
            "WHERE mail_id=?;");

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

                int id = this.controller.getAutoIncrement(insert);
                mail.setId(id);
            }
        }

        insert.close();
        update.close();
    }

    public void deleteMail(int mailId) throws SQLException {
        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM ag_mail WHERE mail_id=?");
        delete.setInt(1, mailId);
        delete.execute();
        delete.close();
    }

    public void deleteMails(List<Mail> mails) throws SQLException {
        PreparedStatement delete = this.controller.createPreparedStatement("DELETE FROM ag_mail WHERE mail_id=?");
        for (Mail mail : mails) {
            delete.clearParameters();
            delete.setInt(1, mail.getId());
            delete.execute();
        }
        delete.close();
    }

}
