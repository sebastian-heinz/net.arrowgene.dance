/*
 * This file is part of net.arrowgene.dance.
 *
 * net.arrowgene.dance is a server implementation for the game "Dance! Online".
 * Copyright (C) 2013-2018  Sebastian Heinz (github: sebastian-heinz)
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

package net.arrowgene.dance.database.maria;


import net.arrowgene.dance.database.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;


public class MariaDbController {

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final Logger logger = LogManager.getLogger(MariaDbController.class);

    private final boolean usePool;

    private MariaDbPoolDataSource pool;
    private Connection connection;


    public MariaDbController(boolean usePool) {
        this.usePool = usePool;
        try {
            Class.forName(JDBC_DRIVER);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public boolean initialize(String host, short port, String database, String user, String password, int timeout) {
        boolean success;
        try {
            Class.forName(JDBC_DRIVER);
            if (usePool) {
                pool = new MariaDbPoolDataSource(getConnectionPoolString(host, port, database, user, password, timeout, 10));
            } else {
                connection = DriverManager.getConnection(getConnectionString(host, port, database, user, password, timeout));
            }
            prepareDatabase();
            success = true;
        } catch (Exception ex) {
            logger.error(ex);
            success = false;
        }
        return success;
    }

    public PreparedStatement createPreparedStatement(String sql) {
        try {
            return getConnection().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception ex) {
            logger.error(ex);
            return null;
        }
    }

    public Statement createStatement() {
        try {
            return getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
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

    public void close() {
        try {
            if (usePool) {
                if (pool != null) {
                    pool.close();
                }
            } else {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        Connection connection;
        if (usePool) {
            try {
                connection = pool.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                connection = null;
            }
        } else {
            connection = this.connection;
        }
        return connection;
    }

    private String getConnectionString(String host, short port, String database, String user, String password, int timeout) {
        return String.format("jdbc:mariadb://%s:%d/%s?user=%s&password=%s&connectTimeout=%d&disableMariaDbDriver", host, port, database, user, password, timeout);
    }

    private String getConnectionPoolString(String host, short port, String database, String user, String password, int timeout, int maxPoolSize) {
        return String.format("jdbc:mariadb://%s:%d/%s?user=%s&password=%s&connectTimeout=%d&pool&maxPoolSize=%d&disableMariaDbDriver", host, port, database, user, password, timeout, maxPoolSize);
    }


// TODO replace import builder?

    private void prepareDatabase() {

        logger.info("Preparing SQLiteDb with structure and default data");
        importFile("mariadb_items.sql");
        importFile("mariadb_songs.sql");
        importFile("mariadb_default.sql");
    }

    private void importFile(String fileName) {
        InputStream sqlStructureFile = Database.class.getResourceAsStream(fileName);
        if (sqlStructureFile != null) {
            logger.info(String.format("Importing %s into SQLiteDb", fileName));
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader bfReader = new BufferedReader(new InputStreamReader(sqlStructureFile));
                String sCurrentLine;
                while ((sCurrentLine = bfReader.readLine()) != null) {
                    sb.append(sCurrentLine).append("\n");
                }
                String[] strSQL = sb.toString().split(";");
                for (String aStrSQL : strSQL) {
                    if (!aStrSQL.trim().equals("")) {
                        executeSQL(aStrSQL + ";");
                    }
                }
            } catch (Exception ex) {
                logger.error(ex);
            }
        } else {
            logger.fatal(String.format("Could not find file %s", fileName));
        }
    }

    private void executeSQL(String sql) {
        Statement stmt = createStatement();
        try {
            stmt.execute(sql);
            stmt.close();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
}
