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

package me.evyn.barista.core.commands.core;

import me.evyn.barista.core.BaristaInfo;
import me.evyn.barista.core.Config;
import me.evyn.barista.core.commands.Command;
import me.evyn.barista.core.commands.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help implements Command {
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embeds, String[] args) {
        User bot = event.getJDA().getSelfUser();

        if (embeds) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(bot.getName(), "https://barista.evyn.me")
                    .setColor(0xdbb381)
                    .setThumbnail(bot.getAvatarUrl())
                    .setDescription("Barista is a simple yet effective, multi-purpose Discord bot written with JDA." +
                            "It makes a perfect addition to your community, study group, or hangout server, and it " +
                            "adds a bit of fun and utility to any environment.")
                    .addField("GitHub", "[Link](https://github.com/evynprice/baristabot)", true)
                    .addField("Version", BaristaInfo.VERSION, true)
                    .addField("Support", "[Support Discord Server](https://discord.gg/u8hAu6sEtw)", true)
                    .addField("Commands", prefix + "commands", false)
                    .setFooter("Barista");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            event.getChannel().sendMessage(
                    "Barista is a simple yet effective, multi-purpose Discord bot written with JDA. It makes a " +
                            "perfect addition to your community, study group, or hangout server, and it adds a bit of" +
                            " fun and utility to any environment." + "\n\n" + "**GitHub:** <https://github.com/" +
                            "evynprice/baristabot>" + "\n" + "**Version:** " + BaristaInfo.VERSION + "\n" + "**Support" +
                            ":** <https://discord.gg/u8hAu6sEtw> " + "\n" + "**Commands:** " + prefix + "commands"
            ).queue();
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public CommandType getType() {
        return CommandType.CORE;
    }
}
