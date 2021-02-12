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
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Help implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User bot = event.getJDA().getSelfUser();
        EmbedBuilder eb = EmbedCreator.newInfoEmbedMessage(bot);

        eb.setDescription("Barista bot is a simple yet effective, multi-purpose Discord bot written with JDA. " +
                "It makes a perfect addition to your community, study group, or hang out server, and it " +
                "adds a bit of fun and utility to any environment")
                .setThumbnail(bot.getAvatarUrl())
                .addField("Github", "[link](https://github.com/thetechnicalfox/baristabot)", true)
                .addField("Logo", "[attribution]" +
                        "(https://github.com/thetechnicalfox/baristabot/blob/main/branding/attribution.txt)", true)
                .addField("Version", BotInfo.BOT_VERSION, true)
                .addField("Commands", prefix + "commands", false)
                .addField("Command usage", prefix + "usage [command]", false);

        event.getChannel()
                .sendMessage(eb.build())
                .queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("info", "information");
    }

    @Override
    public String getDescription() {
        return "Provides information about the bot";
    }

    @Override
    public String getUsage() {
        return "help";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }
}
