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

package me.evyn.bot.commands.info;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.BotInfo;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Stats implements Command {

    /**
     * Provides information on bot instance
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param embed Guild embed setting
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {

        JDA api = event.getJDA();
        User bot = api.getSelfUser();

        // get process memory usage
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long usedMemory = totalMemory - Runtime.getRuntime().freeMemory() / (1024 * 1024);

        String memory = String.format("%dMB / %dMB", usedMemory, totalMemory);

        int guilds = api.getGuilds().size();
        int channels = api.getTextChannels().size();
        int users = api.getUsers().size();

        if (embed) {
            EmbedBuilder eb = EmbedCreator.newInfoEmbedMessage(bot)
                    .setTitle(bot.getName() + " statistics")
                    .addField("Memory", memory, true)
                    .addField("Guilds", "" + guilds, true)
                    .addField("Channels", "" + channels, true)
                    .addField("Users", "" + users, true)
                    .addField("Version", BotInfo.BOT_VERSION, true)
                    .addField("JDA", BotInfo.JDA_VERSION, true);

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            event.getChannel()
                    .sendMessage("**" + bot.getName() + " statistics**" + "\n\n" + "**Memory:** " + memory +
                            "\n" + "**Guilds:** " + guilds + "\n" + "**Channels:** " + channels + "\n" +
                            "**Users:** " + users + "\n" + "**Version:** " + BotInfo.BOT_VERSION + "\n" + "**JDA:** " +
                            BotInfo.JDA_VERSION)
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("statistics", "performance");
    }

    @Override
    public String getDescription() {
        return "Provides technical information on the bot processes";
    }

    @Override
    public String getUsage() {
        return "stats";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }
}
