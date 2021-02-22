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

package me.evyn.bot.commands.moderation;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.commands.Settings.ModLogs;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Purge implements Command {

    /**
     * If command is ran in guild and bot + user have required permissions, delete selected amount of messages in
     * current or mentioned channel.
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param embed Guild embed setting
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {

        User bot = event.getJDA().getSelfUser();

        EmbedBuilder eb = null;
        String message = null;

        // command was not run in guild
        if (!event.isFromType(ChannelType.TEXT)) {
            eb = EmbedCreator.newErrorEmbedMessage(bot, "This command can only be ran in servers.");

        } else if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            // bot is missing mange messages

            String description = "The bot is missing permission: `Manage Messages`";

            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(bot, description);

            } else {
                message = "ERROR: " + description;
            }

        } else if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            // User is missing manage messages

            String description = "You are missing permission: `Manage Messages`";

            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(bot, description);
            } else {
                message = "ERROR: " + description;
            }

        } else if ((args.length == 0)) {
            // no arguments were given

            String description = "Invalid command usage. Try running `" + prefix + "usage purge` for more information.";

            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(bot, description);
            } else {
                message = "ERROR: " + description;
            }

        } else if (!args[0].matches("(0*(?:[1-9][0-9]?|100))")) {

            // invalid usage
            String description = "Invalid command usage. Try running `" + prefix + "usage purge` for more information.";

            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(bot, description);
            } else {
                message = "ERROR: " + description;
            }
        } else {

            // add one to the amount to also delete command message
            int amount = Integer.valueOf(args[0]) + 1;

            MessageChannel channel = null;
            StringBuilder reason = new StringBuilder();

            if (args.length == 1) {
                channel = event.getChannel();

            } else if (args[1].matches("<#[0-9]{18}>") || args[1].matches("[0-9]{18}")) {
                // first argument is channel Id or mention

                // add one to the amount to also delete command message
                amount = Integer.valueOf(args[0]);

                String id = args[1].replaceAll("[^0-9]", "");

                try {
                    channel = event.getGuild().getTextChannelById(id);
                } catch (NumberFormatException e) {
                    channel = null;
                }

                // if reason is provided
                if (args.length > 2) {
                    for(int i=2; i < args.length; i++) {
                        reason.append(args[i]).append(" ");
                    }

                    reason.delete(reason.length() - 1, reason.length());
                }
            } else {
                channel = event.getChannel();

                // if reason is provided
                if (args.length > 2) {
                    for(int i=1; i < args.length; i++) {
                        reason.append(args[i]).append(" ");
                    }

                    reason.delete(reason.length() - 1, reason.length());
                }
            }

            if (channel != null) {
                // fetch message history
                MessageHistory history = channel.getHistory();
                List<Message> messages = history.retrievePast(amount).complete();

                // purge messages
                channel.purgeMessages(messages);

                // send completion message
                Message m;
                String msg = "Successfully purged " + args[0] + " messages.";
                if (embed) {
                    eb = EmbedCreator.newCommandEmbedMessage(bot)
                            .setTitle("Purge")
                            .setDescription(msg);
                    m = event.getChannel().sendMessage(eb.build()).complete();
                } else {
                    m = event.getChannel().sendMessage(msg).complete();
                }

                // delete completion message
                m.delete().completeAfter(3, TimeUnit.SECONDS);

                TextChannel modLogChannel = ModLogs.getModLogChannel(event, event.getGuild().getIdLong());

                if (modLogChannel != null) {
                    if (reason.toString().equals("")) {
                        reason.append("None");
                    }

                    String logMessage = "**Action:** Purge" + "\n" + "**Amount:** " +
                            args[0] + "\n" + "**Channel:** " + "<#" + channel.getId() + ">" + "\n" + "**Reason:** " +
                            reason.toString() + "\n" + "**Moderator:** " + event.getAuthor().getAsTag();

                    if (embed) {
                        EmbedBuilder modLog = new EmbedBuilder()
                                .setDescription(logMessage)
                                .setColor(0xffa500)
                                .setTimestamp(Instant.now());

                        modLogChannel.sendMessage(modLog.build())
                                .queue();
                    } else {
                        modLogChannel.sendMessage(logMessage)
                                .queue();
                    }
                }

                return;
            } else {
                String description = "Invalid channel was provided.";
                if (embed) {
                    eb = EmbedCreator.newErrorEmbedMessage(bot, description);
                } else {
                    message = "ERROR: " + description;
                }
            }
        }


        // error occurred somewhere, send message
        if (eb != null) {
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
    public String getName() {
        return "purge";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("prune", "delete", "remove");
    }

    @Override
    public String getDescription() {
        return "Removes up to 100 of the most recent messages in this channel or mentioned channel";
    }

    @Override
    public String getUsage() {
        return "purge (0-100) (channel name/reason) (reason)";
    }

    @Override
    public CommandType getType() {
        return CommandType.MODERATION;
    }
}
