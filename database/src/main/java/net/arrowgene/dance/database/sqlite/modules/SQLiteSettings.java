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
import net.arrowgene.dance.library.models.account.AccountSettings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteSettings {

    private SQLiteController controller;
    private SQLiteFactory factory;

    public SQLiteSettings(SQLiteController controller, SQLiteFactory factory) {
        this.controller = controller;
        this.factory = factory;
    }

    public void insertSettings(AccountSettings settings) throws SQLException {

        PreparedStatement insert = this.controller
                .createPreparedStatement("INSERT OR REPLACE INTO [ag_settings] VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);");

        insert.setInt(1, settings.getAccId());
        insert.setInt(2, settings.getKeyArrowLeft());
        insert.setInt(3, settings.getKeyArrowUp());
        insert.setInt(4, settings.getKeyArrowDown());
        insert.setInt(5, settings.getKeyArrowRight());
        insert.setInt(6, settings.getVolBackground());
        insert.setInt(7, settings.getVolSoundEffect());
        insert.setInt(8, settings.getVolGameMusic());
        insert.setInt(9, settings.getNotePanelTransparency());
        insert.setInt(10, settings.getVideoSoften());
        insert.setInt(11, settings.getEffectsScene());
        insert.setInt(12, settings.getEffectsAvatar());
        insert.setInt(13, settings.getCameraView());
        insert.setInt(14, settings.getRate());

        insert.execute();
        insert.close();
    }

    public AccountSettings getSettings(int userId) throws SQLException {
        AccountSettings settings = null;

        PreparedStatement select = this.controller
                .createPreparedStatement("SELECT * FROM [ag_settings] WHERE [user_id]=?");
        select.setInt(1, userId);

        ResultSet rs = select.executeQuery();
        if (rs.next()) {
            settings = this.factory.createAccountSettings(rs);
        }

        rs.close();
        select.close();

        return settings;
    }

    public void deleteSettings(int userId) throws SQLException {
        PreparedStatement delete = this.controller
                .createPreparedStatement("DELETE FROM [ag_settings] WHERE [user_id]=?");
        delete.setInt(1, userId);
        delete.execute();
        delete.close();
    }

}
