/*
 * MIT License
 *
 * Copyright (c) 2021 Evyn Price
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.evyn.barista.core.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class Datasource {

    private static final Logger LOGGER = LoggerFactory.getLogger(Datasource.class);
    private static final HikariConfig CONFIG = new HikariConfig();
    private static final HikariDataSource DS;

    static {
        if (Config.DATASOURCE_ENABLED) {

            List<String> mySQL = Arrays.asList("mysql", "MYSQL", "MySQL");
            List<String> sqLite = Arrays.asList("SqLite", "SQLITE", "sqlite");

            if (mySQL.contains(Config.DATASOURCE_TYPE)) {

                CONFIG.setJdbcUrl("jdbc:mysql://" + Config.DATASOURCE_IP + ":" + Config.DATASOURCE_PORT + "/" +
                        Config.DATASOURCE_DATABASE);
                CONFIG.setUsername(Config.DATASOURCE_USER);
                CONFIG.setPassword(Config.DATASOURCE_PASSWORD);
                CONFIG.setDriverClassName("com.mysql.cj.jdbc.Driver");
                CONFIG.setMaximumPoolSize(20);

            } else if (sqLite.contains(Config.DATASOURCE_TYPE)) {

                try {
                   final File DB = new File("database.db");

                   if (!DB.exists()) {
                       DB.createNewFile();
                       LOGGER.info("Created SQLite DB");
                   }
                } catch (IOException e) {
                    LOGGER.error("Could not create SQLite DB");
                    e.printStackTrace();
                }

                CONFIG.setJdbcUrl("jdbc:sqlite:database.db");
                CONFIG.setMaximumPoolSize(20);

            } else {
                LOGGER.error("Database is enabled but the selected datasource is invalid");
                System.exit(0);
            }

            DS = new HikariDataSource(CONFIG);

            try (Connection conn = DS.getConnection()) {
                conn.isValid(0);
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            // create guild settings table
            try (final Connection conn = getConnection();
                final Statement statement = conn.createStatement()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS guild_settings (guild_id VARCHAR(20) " +
                        "NOT NULL PRIMARY KEY, " + "prefix VARCHAR(255), embeds VARCHAR(1));");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else { // datasource is not enabled
            DS = null;
            LOGGER.info("Database Disabled");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

}
