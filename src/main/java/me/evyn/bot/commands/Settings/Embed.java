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

import me.evyn.bot.util.DataSourceCollector;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Embed implements Setting {

    @Override
    public void edit(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {
        User bot = event.getJDA().getSelfUser();

        String usageErr = "Invalid command usage. Please run `" + prefix + "settings help embed` for more information.";

        // too many arguments
        if(args.length > 2) {
            if (embed) {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, usageErr);

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                event.getChannel()
                        .sendMessage("ERROR: " + usageErr)
                        .queue();
            }

            return;
        }

        long guildId = event.getGuild().getIdLong();
        EmbedBuilder eb = null;

        List<String> positive = Arrays.asList("true", "enabled", "yes");
        List<String> negative = Arrays.asList("false", "disabled", "no");

        boolean result = false;
        if (positive.contains(args[1])) {
            result = DataSourceCollector.setEmbed(guildId, true);
        } else if (negative.contains(args[1])) {
            result = DataSourceCollector.setEmbed(guildId, false);
        } else {
            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(bot, usageErr);
                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                event.getChannel()
                        .sendMessage("ERROR: " + usageErr)
                        .queue();
            }

            return;
        }

        String message = "";
        if (result) {
            String msg = "Success! The new Embed Messages setting will now go into effect.";
            if (embed) {
                eb = EmbedCreator.newCommandEmbedMessage(bot);
                eb.setColor(0x00CC00)
                        .setDescription(msg);
            } else {
                message = msg;
            }

        } else {
            String msg = "There was an error updating the setting. Please try again later.";
            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
            } else {
                message = msg;
            }

        }

        if (embed) {
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            event.getChannel()
                    .sendMessage(message)
                    .queue();
        }
    }

    @Override
    public void view(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {
        boolean value = DataSourceCollector.getEmbed(event.getGuild().getIdLong());

        event.getChannel()
                .sendMessage("Current value is: `" + value + "`")
                .queue();
    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getDescription() {
        return "Changes if the bot should respond with embed messages (Default is true)";
    }

    @Override
    public String getUsage() {
        return "setting embed (true/false)";
    }

    @Override
    public String getExample() {
        return "setting embed enabled";
    }
}
