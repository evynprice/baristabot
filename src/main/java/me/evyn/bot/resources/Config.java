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

package me.evyn.bot.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    public static String token;
    public static String prefix;
    public static String adminId;
    public static String database;
    public static String db_url;
    public static String db_user;
    public static String db_pass;

    public Config() {
        new Config(".env.local");
    }

    public Config(String configName) {
        File env = new File(configName);

        // temp variables
        String botToken = null;
        String botPrefix = null;
        String botAdminId = null;
        String botDatabase = null;
        String botDb_url = null;
        String botDb_user = null;
        String botDb_pass = null;

        if (env.exists()) {
            try {
                FileReader fileReader = new FileReader(env);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line = null;

                // parse configuration variables and add them to instance vars
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("BOT_TOKEN")) {
                        line = line.replace("BOT_TOKEN=", "");
                        botToken = line;
                    } else if (line.startsWith("BOT_PREFIX")) {
                        line = line.replace("BOT_PREFIX=", "");
                        botPrefix = line;
                    } else if (line.startsWith("BOT_ADMINID")) {
                        line = line.replace("BOT_ADMINID=", "");
                        botAdminId = line;
                    } else if (line.startsWith("DATABASE")) {
                        line = line.replace("DATABASE=", "");
                        botDatabase = line;
                    } else if (line.startsWith("DB_URL")) {
                        line = line.replace("DB_URL=", "");
                        botDb_url = line;
                    } else if (line.startsWith("DB_USER")) {
                        line = line.replace("DB_USER=", "");
                        botDb_user = line;
                    } else if (line.startsWith("DB_PASS")) {
                        line = line.replace("DB_PASS=", "");
                        botDb_pass = line;
                    }
                }

                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // If .env.local is not present, set config variables to system environment variables
            botToken = System.getenv("BOT_TOKEN");
            botPrefix = System.getenv("BOT_PREFIX");
            botAdminId = System.getenv("BOT_ADMINID");
            botDatabase = System.getenv("DATABASE");
            botDb_url = System.getenv("DB_URL");
            botDb_user = System.getenv("DB_USER");
            botDb_pass = System.getenv("DB_PASS");
        }

        // Check if token exists and if it matches the proper pattern. Exit if error occurs
        if (botToken == null) {
            LOGGER.error("Bot token was not in proper format. Please check env variables or .env.local file");
            System.exit(0);
        }

        // check if prefix exists, exit if error occurs
        if (botPrefix == null) {
            LOGGER.error("Bot prefix is invalid. Please check env variables or .env.local");
            System.exit(0);
        }

        // set static variables
        token = botToken;
        prefix = botPrefix;
        adminId = botAdminId;
        database = botDatabase;
        db_url = botDb_url;
        db_user = botDb_user;
        db_pass = botDb_pass;

        LOGGER.info("Loaded config file");
    }
}
