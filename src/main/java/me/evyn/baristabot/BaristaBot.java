/**
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

package me.evyn.baristabot;

import me.evyn.baristabot.commands.CommandHandler;
import me.evyn.baristabot.data.Config;
import me.evyn.baristabot.listeners.MessageListener;
import me.evyn.baristabot.listeners.ReactionListener;
import me.evyn.baristabot.listeners.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BaristaBot {

    public static void main(String[] args) throws Exception {
        // TODO look into adding database support
        Config config = new Config();
        CommandHandler ch = new CommandHandler(config.getPrefix(), config.getPrivilegedID());
        ReactionListener reactionListener = new ReactionListener();

        JDA api = JDABuilder.createDefault(config.getToken())
                .addEventListeners(new ReadyListener(config.getPrefix(), reactionListener))
                .addEventListeners(new MessageListener(config.getPrefix(), ch))
                .addEventListeners(reactionListener)
                .build();
    }
}
