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

import net.arrowgene.dance.log.ILogger;

import java.io.File;
import java.sql.*;

public class SQLiteController {
    private Connection connection;
    private String DB_PATH = "data.db3";
    private ILogger logger;

    protected SQLiteController(ILogger logger) {
        this.logger = logger;
    }

    public SQLiteController(String DB_PATH, ILogger logger) {
        this.DB_PATH = DB_PATH;
        this.logger = logger;
    }

    public Statement createStatement() {
        try {
            return this.connection.createStatement(1003, 1007);
        } catch (SQLException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public PreparedStatement createPreparedStatement(String sql) {
        try {
            return this.connection.prepareStatement(sql, 1003, 1007);
        } catch (SQLException var2) {
            var2.printStackTrace();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return autoInc;
    }


    public void clear() {
        File dbFile = new File(this.DB_PATH);
        if (dbFile.exists()) {
            dbFile.delete();
        }

    }

    public boolean initDBConnection() {
        boolean ret = false;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException var1) {
            this.logger.writeLog(var1);
            this.logger.writeDebug("DB:error while loading jdbc-drivers");
        }

        try {
            boolean e = false;
            if (this.connection != null) {
                return ret;
            }

            File dbFile = new File(this.DB_PATH);
            if (!dbFile.exists()) {
                e = true;
            }

            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.DB_PATH);
            if (!this.connection.isClosed()) {
                ret = true;
            }

            if (ret && e) {
                this.create();
            }
        } catch (SQLException var4) {
            throw new RuntimeException(var4);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                SQLiteController.this.close();
            }
        });
        return ret;
    }

    protected void create() {
    }

    protected void defaultValues() {
    }

    public void close() {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                this.connection.close();
                if (this.connection.isClosed()) {
                    this.logger.writeDebug("DB:conenction closed");
                    //           Console.log("DB", "connection closed", 8);
                }
            }
        } catch (SQLException var2) {
            var2.printStackTrace();
        }
    }

}
