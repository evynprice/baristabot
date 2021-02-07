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

package me.evyn.bot.listeners;

import me.evyn.bot.resources.Config;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

    private ReactionListener reactionListener;

    /**
     * Creates the ReadyListener object and initializes the instance variables
     * @param reactionListener listener event
     */
    public ReadyListener(ReactionListener reactionListener) {
        this.reactionListener = reactionListener;
    }

    /**
     * When bot is ready, sends information to console and sets bot presence.
     * @param event Bot ready event
     */
    @Override
    public void onReady(ReadyEvent event) {
        System.out.printf("Bot is ready in %s guilds",event.getGuildTotalCount());
        event.getJDA().getPresence().setActivity(Activity.watching("you | " + Config.prefix + "help"));

        reactionListener.init(event.getJDA().getGuildById("662895013352701952"));
    }
}
