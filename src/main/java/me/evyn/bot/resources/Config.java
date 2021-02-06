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

    // These will be set with the constructor class and can be used with accessor methods
    private String token;
    private String prefix;
    private String elevated;

    /**
     * Constructor for class Config. Attempts to read variables in .env.local first, otherwise reads system env
     * variables.
     */
    public Config() {

        // attempt to read variables in .env.local
        File env = new File(".env.local");

        if (env.exists()) {
            try {
                FileReader fileReader = new FileReader(env);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line = null;

                // parse configuration variables and add them to instance vars
                while ((line = bufferedReader.readLine()) != null) {

                    if (line.startsWith("BOT_TOKEN")) {
                        line = line.replace("BOT_TOKEN=", "");
                        this.token = line;
                    } else if (line.startsWith("BOT_PREFIX")) {
                        line = line.replace("BOT_PREFIX=", "");
                        this.prefix = line;
                    } else if (line.startsWith("BOT_ELEVATED")) {
                        line = line.replace("BOT_ELEVATED=", "");
                        this.elevated = line;
                    }
                }

                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // If .env.local is not present, set config variables to system environment variables
            this.token = System.getenv("TOKEN");
            this.prefix = System.getenv("PREFIX");
            this.elevated = System.getenv("PRIVILEGED");
        }

        // Check if token exists and if it matches the proper pattern. Exit if error occurs
        if (this.token == null || !this.token.matches(".{59}")) {
            System.out.println("Bot token was not in the proper format. Please check env variables or .env.local");
            System.exit(0);
        }

        // check if prefix exists, exit if error occurs
        if (this.prefix == null) {
            System.out.println("Bot prefix is invalid. Please check env variables or .env.local");
            System.exit(0);
        }
    }

    /**
     * Gets the bot configuration token
     * @return String bot token
     */
    public String getToken() {
        return this.token;
    }


    /**
     * Get the bot configuration prefix
     * @return String bot prefix
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Get the bot configuration elevated user ID
     * @return String elevated user ID
     */
    public String getElevated() {
        return this.elevated;
    }
}
