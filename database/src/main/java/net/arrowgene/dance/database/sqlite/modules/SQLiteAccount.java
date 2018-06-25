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

    public void insertAccount(Account account) throws SQLException {

        if (account.getId() > -1) {
            PreparedStatement update = this.controller.createPreparedStatement(
                "UPDATE ag_user SET user_account=?, user_password=?, user_state=?, user_active_character_id=? WHERE user_id=?;");
            update.setString(1, account.getUsername());
            update.setString(2, account.getPasswordHash());
            update.setInt(3, account.getState().getNumValue());
            if (account.getActiveCharacterId() >= 0) {
                update.setInt(4, account.getActiveCharacterId());
            } else {
                update.setNull(4, Types.INTEGER);
            }
            update.setInt(5, account.getId());
            update.execute();
            update.close();
        } else {
            PreparedStatement insert = this.controller
                .createPreparedStatement("INSERT INTO ag_user VALUES (?,?,?,?,?);");
            insert.setString(2, account.getUsername());
            insert.setString(3, account.getPasswordHash());
            insert.setInt(4, account.getState().getNumValue());
            if (account.getActiveCharacterId() >= 0) {
                insert.setInt(5, account.getActiveCharacterId());
            } else {
                insert.setNull(5, Types.INTEGER);
            }
            insert.execute();

            int id = this.controller.getAutoIncrement(insert);
            account.setId(id);

            insert.close();
        }
    }

    public void insertPassword(String accountName, String newPasswordHash) throws SQLException {

        PreparedStatement update = this.controller
            .createPreparedStatement("UPDATE ag_user SET user_password=? WHERE user_account=?;");
        update.setString(1, newPasswordHash);
        update.setString(2, accountName);
        update.execute();
        update.close();
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

    public Account getAccount(String accountName, String passwordHash) throws SQLException {
        Account account = null;

        PreparedStatement select = this.controller
            .createPreparedStatement("SELECT * FROM ag_user WHERE user_account=? AND user_password=?");
        select.setString(1, accountName);
        select.setString(2, passwordHash);
        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            account = this.factory.createAccount(rs);
        }

        rs.close();
        select.close();

        return account;
    }

}
