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

package net.arrowgene.dance.database.sqlite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.*;

public class SQLiteController {

    private static final Logger logger = LogManager.getLogger(SQLiteController.class);
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";

    private Connection connection;
    private String databasePath;

    public SQLiteController(String databasePath) {
        this.databasePath = databasePath;
    }

    public Statement createStatement() {
        try {
            return this.connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    public PreparedStatement createPreparedStatement(String sql) {
        try {
            return this.connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    /**
     * Gets the latest auto increment value.
     *
     * @param stmt an open and executed statement (before "close()'-method call).
     * @return the latest auto increment key or -1 if no key has been generated.
     */
    public int getAutoIncrement(PreparedStatement stmt) {
        int autoInc = -1;
        try {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                autoInc = rs.getInt("last_insert_rowid()");
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return autoInc;
    }

    public void delete() {
        File dbFile = new File(databasePath);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    public boolean initialize() {
        boolean success;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(getConnectionString());
            success = true;
        } catch (Exception ex) {
            logger.error(ex);
            success = false;
        }
        return success;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                if (connection.isClosed()) {
                    logger.info("SQLite Connection Closed");
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private String getConnectionString() {
        return String.format("jdbc:sqlite:%s", databasePath);
    }
}
