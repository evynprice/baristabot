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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Config {

    public static final String token;
    public static final String prefix;
    public static final String elevated;

    static {
        // attempt to read variables in .env.local
        File env = new File(".env.local");

        String botToken = null;
        String botPrefix = null;
        String botElevated = null;
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
                    } else if (line.startsWith("BOT_ELEVATED")) {
                        line = line.replace("BOT_ELEVATED=", "");
                        botElevated = line;
                    }
                }

                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // If .env.local is not present, set config variables to system environment variables
            botToken = System.getenv("TOKEN");
            botPrefix = System.getenv("PREFIX");
            botElevated = System.getenv("PRIVILEGED");
        }

        // Check if token exists and if it matches the proper pattern. Exit if error occurs
        if (botToken == null || !botToken.matches(".{59}")) {
            System.out.println("Bot token was not in the proper format. Please check env variables or .env.local");
            System.exit(0);
        }

        // check if prefix exists, exit if error occurs
        if (botPrefix == null) {
            System.out.println("Bot prefix is invalid. Please check env variables or .env.local");
            System.exit(0);
        }

        token = botToken;
        prefix = botPrefix;
        elevated = botElevated;
    }
}
