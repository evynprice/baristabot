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

package me.evyn.bot.commands.Settings;

import me.evyn.bot.resources.Config;
import me.evyn.bot.util.DataSourceCollector;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Prefix implements Setting {

    @Override
    public void edit(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {
        User bot = event.getJDA().getSelfUser();

        // too ma ny arguments
        if(args.length > 2) {
            String desc = "Invalid command usage. Try running `" + prefix + "settings help prefix` for more information.";
            if (embed) {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, desc);

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                event.getChannel()
                        .sendMessage("ERROR: " + desc)
                        .queue();
            }

            return;
        }

        long guildId = event.getGuild().getIdLong();

        String newPrefix;
        if(args[1].equals("reset")) {
           newPrefix = Config.prefix;
        } else {
            newPrefix = args[1];
        }

        boolean status = DataSourceCollector.setGuildPrefix(guildId, newPrefix);
        EmbedBuilder eb = null;
        String msg = "";

        if (status) {
            String desc = "Success! New prefix is `" + newPrefix + "`";
            if (embed) {
                eb = EmbedCreator.newCommandEmbedMessage(bot);
                eb.setColor(0x00CC00)
                        .setDescription(desc);
            } else {
                msg = desc;
            }

        } else {
            String desc = "There was an error resetting the prefix. Please try again later.";
            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(bot, desc);
            } else {
                msg = "ERROR: " + desc;
            }
        }

        if (embed) {
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            event.getChannel()
                    .sendMessage(msg)
                    .queue();
        }
    }

    @Override
    public void view(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {
        event.getChannel()
                .sendMessage("Current prefix is: `" + prefix + "`")
                .queue();
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getDescription() {
        return "Changes the prefix that the bot listens to. Updating the prefix to the value `reset` will change the " +
                "prefix to the default value of `" + Config.prefix + "`";
    }

    @Override
    public String getUsage() {
        return "prefix (reset/newPrefix)";
    }

    @Override
    public String getExample() {
        return "prefix b!";
    }
}
