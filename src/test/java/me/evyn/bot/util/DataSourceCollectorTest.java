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

import junit.framework.TestCase;
import me.evyn.bot.resources.Config;
import me.evyn.bot.resources.DataSource;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSourceCollectorTest extends TestCase {

    @Test
    public void testGetPrefix() {
        // attempt to add guild into database
        try (final PreparedStatement insertStatement = DataSource
                .getConnection()
                .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

            insertStatement.setString(1, String.valueOf("123456789"));

            insertStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // guild exists in database
        assertEquals(Config.prefix, DataSourceCollector.getPrefix(12345679));

        // attempt to delete guild from database
        try (final PreparedStatement insertStatement = DataSource
                .getConnection()
                .prepareStatement("DELETE FROM guild_settings WHERE guild_id = ?")) {

            insertStatement.setString(1, String.valueOf("123456789"));

            insertStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // attempt to find deleted guild in database
        try (final PreparedStatement preparedStatement = DataSource
                .getConnection()
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf("123456789"));

            String prefix = "";
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    prefix =  resultSet.getString("prefix");
                }
            } catch (Exception e) {
                prefix = null;
            } finally {
                assertTrue(prefix == "");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}