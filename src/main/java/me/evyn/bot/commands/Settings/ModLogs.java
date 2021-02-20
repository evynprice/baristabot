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
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ModLogs implements Setting {

    @Override
    public void edit(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {
        User bot = event.getJDA().getSelfUser();
        long guildId = event.getGuild().getIdLong();

        String usageErr = "Invalid command usage. Please run `" + prefix + "settings help mod-logs` for more " +
                "information.";

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

        List<String> negative = Arrays.asList("false", "disabled", "no");

        boolean result = false;
        TextChannel channel = null;

        if (negative.contains(args[1])) {
            result = DataSourceCollector.setModLog(guildId, 000000000000000000);

        } else {
            String channelId = args[1].replaceAll("[^0-9]", "");

            try {
                channel = event.getGuild().getTextChannelById(channelId);
            } catch (NumberFormatException e) {

                String msg = "The provided channel is invalid. Please choose a valid channel.";
                if (embed) {
                    EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
                    event.getChannel()
                            .sendMessage(eb.build())
                            .queue();
                } else {
                    event.getChannel()
                            .sendMessage("ERROR: " + msg)
                            .queue();
                }
                return;
            }

            result = DataSourceCollector.setModLog(guildId, channel.getIdLong());
        }

        EmbedBuilder eb = null;
        String message = "";

        if (result) {
            String msg = "";
            if (channel != null) {
                msg = "Success! The new mod-log channel will be " + channel.getAsMention();
            } else {
                msg = "Success! The mod-log setting has been disabled.";
            }
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
        User bot = event.getJDA().getSelfUser();

        GuildChannel channel = ModLogs.getModLogChannel(event, event.getGuild().getIdLong());

        if (channel == null) {
            String msg = "mod-logs are not currently set up or the current channel is invalid. " +
                    "For more information, run " + prefix + "`settings help mod-logs`";
            if (embed) {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                event.getChannel()
                        .sendMessage("ERROR: " + msg)
                        .queue();
            }
            return;
        }

        event.getChannel()
                .sendMessage("The current mod-log channel is " + channel.getName() + " (" +
                        channel.getId() + ")")
                .queue();
    }

    public static TextChannel getModLogChannel(MessageReceivedEvent event, long guildId) {
        String channelId = DataSourceCollector.getModLog(guildId);

        if (channelId == null) {
            return null;
        } else {
            try {
                return event.getGuild().getTextChannelById(channelId);
            } catch (NumberFormatException e ) {
                return null;
            }
        }
    }

    @Override
    public String getName() {
        return "mod-logs";
    }

    @Override
    public String getDescription() {
        return "Sends logs of moderation activity to specified channel";
    }

    @Override
    public String getUsage() {
        return "settings mod-logs (disabled/channel-id/ChannelMention)";
    }

    @Override
    public String getExample() {
        return "settings mod-logs #mod-logs";
    }
}
