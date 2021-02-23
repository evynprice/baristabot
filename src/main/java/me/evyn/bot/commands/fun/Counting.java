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

package me.evyn.bot.commands.fun;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.DataSourceCollector;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Counting implements Command {

    /**
     * Includes all commands related to the counting game
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param embed embed messages boolean
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {

        User bot = event.getJDA().getSelfUser();
        EmbedBuilder eb = null;

        if (!event.isFromType(ChannelType.TEXT)) {
            eb = EmbedCreator.newErrorEmbedMessage(bot, "This command can only be ran in servers.");
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        long guildId = event.getGuild().getIdLong();
        List<String> negative = Arrays.asList("false", "disabled", "reset", "disable");
        String message = null;

        // help command
        if (args.length == 0 || args[0].equals("help")) {
            if (embed) {
                eb = EmbedCreator.newCommandEmbedMessage(bot);
                eb.setTitle("Counting Game")
                        .setColor(0x008080)
                        .setDescription("Think that you have what it takes to count?")
                        .addField("Rules", "- Counting starts at 1. Players must type " +
                                "the next number in line (2, 3, etc) to increase the count." + "\n" + "- If the " +
                                "next number entered is not the next number in line, the count resets." + "\n" +
                                "- One person cannot enter two numbers in a row (At least two people are required " +
                                "to play.", false)
                        .addField("Getting Started", "`" + prefix + "counting channel <channel-mention>`", false)
                        .addField("View Top Players", "`" + prefix + "counting top`", false)
                        .setFooter("Barista Bot")
                        .setTimestamp(Instant.now());
            } else {
                message = "**Counting Game**" + "\n" + "Think that you have what it takes to count? The rules of " +
                        "the game are simple: \n\n" + "- Count starts at 1. Players must type the next number in line" +
                        " (2, 3, etc) to increase the count. \n" + "- If the next number entered is not the next " +
                        "number in line, the count resets." + "\n" + "- One person cannot enter two numbers in a " +
                        "row. (At least two people are required to play)" + "\n\n" + "To get started counting, run: `" +
                        prefix + "counting channel <channel-mention>`" + "\n" + "To view the top players in your " +
                        "server, run `" + prefix + "counting top`";
            }

            // get top users in guild
        } else if (args[0].equals("top")) {

            TextChannel countingChannel = Counting.getCountingChannel(event, guildId);

            if (countingChannel != null) {
                Map<String, Integer> top = DataSourceCollector.getCountingGuildTopUsers(event.getGuild().getIdLong());

                StringBuilder sb = new StringBuilder();

                top.keySet().stream()
                        .forEach(memberId -> {
                            Member member = event.getGuild().getMemberById(memberId);
                            sb.append(member.getEffectiveName()).append(":** ").append(top.get(memberId)).append("**\n");
                        });

                if (embed) {
                    eb = EmbedCreator.newCommandEmbedMessage(bot);
                    eb.setTitle("Top 10 Counters")
                            .setColor(0x008080)
                            .setDescription(sb.toString())
                            .setFooter("Barista Bot")
                            .setTimestamp(Instant.now());
                } else {
                    message = "**Top 10 Counters**" + "\n" + sb.toString();
                }

            } else {
                String msg = "The counting game is currently disabled";
                if (embed) {
                    eb = EmbedCreator.newErrorEmbedMessage(bot, "The counting game is currently disabled.");

                } else {
                    message = "ERROR" + msg;
                }
            }

        // change channel
        } else if (args[0].equals("channel")) {
            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                String msg = "This command requires the `Manage Server` permission.";
                if (embed) {
                    eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
                } else {
                    message = "ERROR: " + msg;
                }
            } else if (args.length == 1) {
                // view channel
                TextChannel channel = Counting.getCountingChannel(event, guildId);

                if (channel != null) {
                    String msg = "The current counting channel is " + channel.getAsMention();

                    if (embed) {
                        eb = EmbedCreator.newCommandEmbedMessage(bot);
                        eb.setTitle("Counting Game")
                                .setColor(0x008080)
                                .setDescription(msg)
                                .setFooter("Barista Bot")
                                .setTimestamp(Instant.now());
                    } else {
                        message = msg;
                    }

                } else {
                    String msg = "The current counting channel is either invalid or not set up.";
                    if (embed) {
                        eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
                    } else {
                        message = "ERROR: " + msg;
                    }
                }

                // edit channel
            } else if ((args[1].matches("<#[0-9]{18}>")) || args[1].matches("[0-9]{18}")) {

                boolean result = false;
                TextChannel channel = null;

                // get channel id
                String channelId = args[1].replaceAll("[^0-9]", "");

                try {
                    channel = event.getGuild().getTextChannelById(channelId);
                } catch (NumberFormatException e) {

                    String msg = "The provided channel is invalid. Please choose a valid channel.";
                    if (embed) {
                        eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
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

                result = DataSourceCollector.setCountingChannel(guildId, channel.getIdLong());

                if (result) {
                    String msg = "Success! The new counting channel will be " + channel.getAsMention();
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

                // disable game
            } else if (negative.contains(args[1])) {

                boolean result = false;
                TextChannel channel = null;

                result = DataSourceCollector.setCountingChannel(guildId, 0);

                if (result) {
                    String msg = "Success! The counting game will now be disabled.";
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

            } else {
                // invalid arguments
                String msg = "Invalid arguments were provided. Try running `" + prefix + "counting help` for more " +
                        "information.";
                if (embed) {
                    eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
                } else {
                    message = "ERROR: " + msg;
                }
            }
        }

        if (eb != null) {
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else if (message != null){
            event.getChannel()
                    .sendMessage(message)
                    .queue();
        }
    }

    /**
     * Gets the TextChannel object of the current guild counting channel. Returns null if counting is disabled.
     * @param event Discord API Message Event
     * @param guildId long guildId
     * @return TextChannel countingChannel
     */
    public static TextChannel getCountingChannel(MessageReceivedEvent event, long guildId) {
        String channelId = DataSourceCollector.getCountingChannel(guildId);

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
        return "counting";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public String getDescription() {
        return "Contains settings and commands for the counting game";
    }

    @Override
    public String getUsage() {
        return "counting help";
    }

    @Override
    public CommandType getType() {
        return CommandType.FUN;
    }
}
