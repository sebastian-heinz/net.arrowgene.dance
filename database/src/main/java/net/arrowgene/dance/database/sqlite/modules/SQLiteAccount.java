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
import net.arrowgene.dance.library.models.account.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


public class SQLiteAccount {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteAccount(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public Account getAccount(String accountName) throws SQLException {
        Account account = null;

        PreparedStatement select = this.controller
            .createPreparedStatement("SELECT * FROM ag_user WHERE user_account=?");
        select.setString(1, accountName);

        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            account = this.factory.createAccount(rs);
        }

        rs.close();
        select.close();

        return account;
    }

    public Account getAccount(int accountId) throws SQLException {
        Account account = null;

        PreparedStatement select = this.controller
            .createPreparedStatement("SELECT * FROM ag_user WHERE user_id=?");
        select.setInt(1, accountId);

        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            account = this.factory.createAccount(rs);
        }

        rs.close();
        select.close();

        return account;
    }
}
