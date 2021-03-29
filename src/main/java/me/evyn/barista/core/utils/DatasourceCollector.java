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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatasourceCollector {

    public static String getGuildPrefix(String guildId) {
        try (final Connection conn = Datasource.getConnection();
             final PreparedStatement statement = conn
                     .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

            statement.setString(1, guildId);

            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String prefix = resultSet.getString("prefix");
                    if (prefix == null) {
                        return Config.DEFAULT_PREFIX;
                    }
                    return prefix;
                }
            }

            try (final PreparedStatement insertStatement = conn
                         .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

                insertStatement.setString(1, guildId);

                insertStatement.executeUpdate();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Config.DEFAULT_PREFIX;
    }

    public static boolean getGuildEmbedsEnabled(String guildId) {
        try (final Connection conn = Datasource.getConnection();
             final PreparedStatement statement = conn
                    .prepareStatement("SELECT embeds FROM guild_settings WHERE guild_id = ?")) {

            statement.setString(1, guildId);

            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String embeds = resultSet.getString("embeds");
                    if (embeds != null) {
                        if (embeds.equals("0")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Config.DEFAULT_EMBEDS_ENABLED;
    }

}
