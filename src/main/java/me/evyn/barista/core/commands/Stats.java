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

package me.evyn.barista.core.commands;

import me.evyn.barista.core.utils.BaristaInfo;
import me.evyn.barista.core.utils.Command;
import me.evyn.barista.core.utils.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;

public class Stats implements Command {

    /**
     * Generates and sends bot statistics in event's channel
     * @param event Message Event
     * @param prefix Bot prefix
     * @param embedsEnabled embedsEnabled
     * @param args command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embedsEnabled, String[] args) {
        // fetch JDA and bot user objects
        JDA api = event.getJDA();
        User bot = api.getSelfUser();

        // get process memory usage
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long usedMemory = totalMemory - Runtime.getRuntime().freeMemory() / (1024 * 1024);

        String memory = usedMemory + "MB / " + totalMemory + "MB";

        // fetch bot guild, channel, and user statistics
        int guilds = api.getGuilds().size();
        int channels = api.getTextChannels().size();
        int users = api.getUsers().size();

        if (embedsEnabled) {
            // generate statistics embed
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(bot.getName() + " statistics")
                    .setColor(0xdbb381)
                    .addField("Memory", memory, true)
                    .addField("Guilds",  String.valueOf(guilds), true)
                    .addField("Channels", String.valueOf(channels), true)
                    .addField("Users", String.valueOf(users), true)
                    .addField("Version", BaristaInfo.VERSION, true)
                    .addField("JDA", BaristaInfo.JDA_VERSION, true)
                    .setFooter(bot.getName(), bot.getAvatarUrl())
                    .setTimestamp(Instant.now());

            // send statistics embed
            event.getChannel().sendMessage(eb.build()).queue();
        } else {
            // generate plaintext statistics
            String msg = "**" + bot.getName() + " statistics**" + "\n\n" + "**Memory:** " + memory +
                    "\n" + "**Guilds:** " + guilds + "\n" + "**Channels:** " + channels + "\n" +
                    "**Users:** " + users + "\n" + "**Version:** " + BaristaInfo.VERSION + "\n" + "**JDA:** " +
                    BaristaInfo.JDA_VERSION;

            // send plaintext statistics
            event.getChannel()
                    .sendMessage(msg)
                    .queue();
        }
    }

    /**
     * Provides name of command
     * @return command name
     */
    @Override
    public String getName() {
        return "stats";
    }

    /**
     * Provides type of command
     * @return command type
     */
    @Override
    public CommandType getType() {
        return CommandType.CORE;
    }
}
