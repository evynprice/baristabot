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

    public static String getActivityLog(long guildId) {
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

    public static boolean setActivityLog(long guildId, long channelId) {
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

    public static String getCurrentScore(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT current_score FROM counting_guilds WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("current_score");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static boolean setCurrentScore(long guildId, String num) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE counting_guilds SET current_score = ? WHERE guild_id = ? ")) {

            preparedStatement.setString(1, num);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static String getLastUserId(long guildId) {
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

    public static boolean setLastUserId(long guildId, String userId) {
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

    public static String getTopScore(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT top_score FROM counting_guilds WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("top_score");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static boolean setTopScore(long guildId, String newTop) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE counting_guilds SET top_score = ? WHERE guild_id = ? ")) {

            preparedStatement.setString(1, newTop);
            preparedStatement.setString(2, String.valueOf(guildId));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static String getUserTotalCount(long guildId, long userId) {

        String memberId = String.valueOf(guildId) + userId;

        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT total_count FROM counting_users WHERE member_id = ?")) {

            preparedStatement.setString(1, memberId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("total_count");
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
        return "0";
    }

    public static boolean setUserTotalScore(long guildId, long userId, String newScore) {

        String memberId = String.valueOf(guildId) + userId;

        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("UPDATE counting_users SET total_count = ? WHERE member_id = ?")) {

            preparedStatement.setString(1, newScore);
            preparedStatement.setString(2, memberId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static Map<String, Integer> getTopUsers(long guildId) {
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT * FROM counting_users WHERE guild_id = ? ORDER BY total_count DESC LIMIT 10")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                Map<String, Integer> members = new HashMap<>();
                while(resultSet.next()) {
                    String id = resultSet.getString("user_id");
                    int count = Integer.valueOf(resultSet.getString("total_count"));
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
