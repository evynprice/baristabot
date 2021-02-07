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

package me.evyn.bot.commands.information;

import me.evyn.bot.util.BotInfo;
import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.EasyEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Statistics implements Command {

    /**
     * Provides statistics on the bot instance
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, List<String> args) {

        JDA api = event.getJDA();
        User botUser = event.getJDA().getSelfUser();

        // get memory in bytes and convert to MB
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long usedMemory = totalMemory - Runtime.getRuntime().freeMemory() / (1024 * 1024);

        // format memory as String
        String memory = String.format("%dMB / %dMB", usedMemory, totalMemory);

        // get guild information
        int servers = api.getGuilds().size();
        int channels = api.getTextChannels().size();
        int users = api.getUsers().size();

        // get versions
        String botVersion = BotInfo.BOT_VERSION;
        String jdaVersion = BotInfo.JDA_VERSION;

        // format information as embed
        EmbedBuilder eb = EasyEmbed.newInfoEmbedMessage(botUser);

        eb.setTitle(botUser.getName() + " statistics")
                .addField("Memory", memory, true)
                .addField("Servers", String.valueOf(servers), true)
                .addField("Channels", String.valueOf(channels), true)
                .addField("Users", String.valueOf(users), true)
                .addField("Version", botVersion, true)
                .addField("JDA", jdaVersion, true);

        // send embed message
        event.getChannel()
                .sendMessage(eb.build())
                .queue();
    }

    @Override
    public String getName() {
        return "statistics";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("stats");
    }

    @Override
    public String getDescription() {
        return "Provides bot statistics and information";
    }

    @Override
    public String getUsage() {
        return "statistics";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFORMATION;
    }
}
