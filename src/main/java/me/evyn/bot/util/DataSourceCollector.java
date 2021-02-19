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

package me.evyn.bot.util;

import me.evyn.bot.resources.Config;
import me.evyn.bot.resources.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSourceCollector {

    /**
     * Returns the current prefix of the guild provided
     *
     * @param guildId long Discord API Guild Id
     * @return String bot prefix
     */
    public static String getPrefix(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatement = DataSource
                    .getConnection()
                    .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.prefix;
    }

    public static boolean setPrefix(long guildId, String newPrefix) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ? ")) {

            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static Boolean getEmbed(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT embed FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String results = resultSet.getString("embed");
                    if (results.equals("1")) {
                        return true;
                    } else if (results.equals("0")) {
                        return false;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static boolean setEmbed(long guildId, boolean value) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE guild_settings SET embed = ? WHERE guild_id = ? ")) {

            if (value) {
                preparedStatement.setString(1, String.valueOf(1));
            } else {
                preparedStatement.setString(1, String.valueOf(0));
            }

            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static String getModLog(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT modlog_id FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String results = resultSet.getString("modlog_id");
                    if (results.equals("0")) {
                        return null;
                    } else {
                        return results;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static boolean setModLog(long guildId, long channelId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE guild_settings SET modlog_id = ? WHERE guild_id = ? ")) {

            preparedStatement.setString(1, String.valueOf(channelId));
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
