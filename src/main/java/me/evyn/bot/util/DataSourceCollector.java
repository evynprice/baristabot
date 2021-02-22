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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSourceCollector {

    /**
     * Returns the current prefix of the guild provided
     * @param guildId long Discord API Guild Id
     * @return String bot prefix
     */
    public static String getGuildPrefix(long guildId) {
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


    /**
     * Sets the current prefix of the guild provided
     * @param guildId long Discord API Guild Id
     * @param newPrefix String
     * @return boolean update status
     */
    public static boolean setGuildPrefix(long guildId, String newPrefix) {
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

    /**
     * Returns the current guild embed setting status
     * @param guildId
     * @return Boolean True or False if exists, Null if setting does not exist
     */
    public static Boolean getGuildEmbed(long guildId) {
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

    /**
     * Sets the current value of the guild prefix setting
     * @param guildId
     * @param value
     * @return boolean status
     */
    public static boolean setGuildEmbed(long guildId, boolean value) {
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

    /**
     * Returns the current mod-log channel Id. Returns null if mod-logs are disabled
     * @param guildId
     * @return String channelId
     */
    public static String getGuildModLogId(long guildId) {
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

    /**
     * Sets the current mod-log channel to new Id
     * @param guildId
     * @param channelId
     * @return boolean status
     */
    public static boolean setGuildModLogId(long guildId, long channelId) {
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

    /**
     * Returns the current activity-log channel Id. Returns null if activity-logs are disabled
     * @param guildId
     * @return channelId
     */
    public static String getGuildActivityLogId(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT activitylog_id FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String results = resultSet.getString("activitylog_id");
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

    /**
     * Sets the current activity-log channel Id. Returns status.
     * @param guildId
     * @param channelId
     * @return boolean status
     */
    public static boolean setGuildActivityLogId(long guildId, long channelId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE guild_settings SET activitylog_id = ? WHERE guild_id = ? ")) {

            preparedStatement.setString(1, String.valueOf(channelId));
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the current counting game channel. Returns null if counting game is disasbled.
     * @param guildId
     * @return String Channel Id
     */
    public static String getCountingChannel(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT channel FROM counting_guilds WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getString("channel").equals("0")) {
                        return null;
                    } else {
                        return resultSet.getString("channel");
                    }
                }
            }

            try (final PreparedStatement insertStatement = DataSource
                    .getConnection()
                    .prepareStatement("INSERT INTO counting_guilds(guild_id) VALUES(?)")) {

                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the current counting channel. Returns status
     * @param guildId
     * @param channelId
     * @return boolean status
     */
    public static boolean setCountingChannel(long guildId, long channelId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE counting_guilds SET channel = ? WHERE guild_id = ? ")) {

            preparedStatement.setString(1, String.valueOf(channelId));
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the current counting score for guild
     * @param guildId
     * @return String score
     */
    public static Integer getCountingCurrentGuildScore(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT current_score FROM counting_guilds WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("current_score");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    /**
     * Sets the current counting score for guild. Returns status
     * @param guildId
     * @param num
     * @return boolean status
     */
    public static boolean setCountingCurrentGuildScore(long guildId, int num) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE counting_guilds SET current_score = ? WHERE guild_id = ? ")) {

            preparedStatement.setInt(1, num);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the user ID of the last person in guild to add to count
     * @param guildId
     * @return String userId
     */
    public static String getCountingGuildLastUserId(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT last_userid FROM counting_guilds WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("last_userid");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    /**
     * Sets the user ID of the last person in guild to add count. Returns status
     * @param guildId
     * @param userId
     * @return boolean status
     */
    public static boolean setCountingGuildLastUserId(long guildId, String userId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE counting_guilds SET last_userid = ? WHERE guild_id = ? ")) {

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the counting high score in guild. Returns null if counting is disabled
     * @param guildId
     * @return String high score
     */
    public static Integer getCountingGuildTopScore(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT top_score FROM counting_guilds WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("top_score");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    /**
     * Sets the counting high score in guild. Returns null if counting is disabled.
     * @param guildId
     * @param newTop
     * @return
     */
    public static boolean setCountingGuildTopScore(long guildId, int newTop) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE counting_guilds SET top_score = ? WHERE guild_id = ? ")) {

            preparedStatement.setInt(1, newTop);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the total count for user in guild.
     * @param guildId
     * @param userId
     * @return String count
     */
    public static Integer getCountingUserTotalCount(long guildId, long userId) {

        String memberId = String.valueOf(guildId) + userId;

        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT total_count FROM counting_users WHERE member_id = ?")) {

            preparedStatement.setString(1, memberId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_count");
                }
            }

            try (final PreparedStatement insertStatement = DataSource
                    .getConnection()
                    .prepareStatement("INSERT INTO counting_users(guild_id,user_id,member_id) VALUES(?, ?, ?)")) {

                insertStatement.setString(1, String.valueOf(guildId));
                insertStatement.setString(2, String.valueOf(userId));
                insertStatement.setString(3, memberId);

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Sets the total count for user in guild.
     * @param guildId
     * @param userId
     * @param newScore
     * @return boolean status
     */
    public static boolean setCountingUserTotalCount(long guildId, long userId, int newScore) {

        String memberId = String.valueOf(guildId) + userId;

        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE counting_users SET total_count = ? WHERE member_id = ?")) {

            preparedStatement.setInt(1, newScore);
            preparedStatement.setString(2, memberId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the top users in guild. Returns null if counting is disabled.
     * @param guildId
     * @return Map<String, Integer> userId, count
     */
    public static Map<String, Integer> getCountingGuildTopUsers(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT * FROM counting_users WHERE guild_id = ? ORDER BY total_count DESC LIMIT 10")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                Map<String, Integer> members = new HashMap<>();
                while(resultSet.next()) {
                    String id = resultSet.getString("user_id");
                    int count = resultSet.getInt("total_count");
                    members.put(id, count);
                }
                return members;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
