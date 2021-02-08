package me.evyn.bot.util;

import me.evyn.bot.resources.Config;
import me.evyn.bot.resources.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSourceCollector {

    /**
     * Returns the current prefix of the guild provided
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
}
