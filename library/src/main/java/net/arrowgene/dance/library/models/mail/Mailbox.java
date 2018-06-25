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

import java.util.ArrayList;
import java.util.List;

public class Mailbox {


    private List<Mail> mails;

    public Mailbox() {
        this.mails = new ArrayList<Mail>();
    }

    public ArrayList<Mail> getMails() {
        return new ArrayList<Mail>(this.mails);
    }

    public Mail getMail(int mailId) {
        Mail result = null;
        for (Mail mail : this.mails) {
            if (mail.getId() == mailId) {
                result = mail;
                break;
            }
        }
        return result;
    }

    public void addMail(Mail mail) {
        this.mails.add(mail);
    }

    public void addMails(List<Mail> mails) {
        for (Mail mail : mails) {
            this.addMail(mail);
        }
    }

    public void removeMail(int mailId) {
        Mail remove = null;
        for (Mail mail : mails) {
            if (mail.getId() == mailId) {
                remove = mail;
                break;
            }
        }
        if (remove != null) {
            this.mails.remove(remove);
        }
    }

    public void removeAllMails() {
        this.mails.clear();
    }

}
