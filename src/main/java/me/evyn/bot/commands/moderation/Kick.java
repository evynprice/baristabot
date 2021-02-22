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
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.requests.RestAction;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class Kick implements Command {

    /**
     * If command is ran in guild and bot + user have required permissions, kicks mentioned user with optional reason
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param embed Guild embed setting
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {

        User botUser = event.getJDA().getSelfUser();

        // if command is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "This command can only be ran in servers.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        Member botMember = event.getGuild().getSelfMember();

        EmbedBuilder eb = null;
        String message = "";

        // if Bot does not have Kick Members permission
        if (!botMember.hasPermission(Permission.KICK_MEMBERS)) {
            String desc = "The bot is missing permission `Kick Members`";
            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);
            } else {
                message = "ERROR: " + desc;
            }

        } else {

            // if User does not have Kick Members permission
            if (!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                String desc = "You are missing permission `Kick Members`";
                if (embed) {
                    eb = EmbedCreator.newErrorEmbedMessage(botUser,desc);
                } else {
                    message = "ERROR: " + desc;
                }

            } else {

                // no user is provided
                if (args.length == 0) {
                    String desc = "No user was provided. For more information try running `" + prefix + "usage " +
                            "kick`";
                    if (embed) {
                        eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);
                    } else {
                        message = "ERROR: " + desc;
                    }

                } else {

                    // fetch user Id
                    String Id = args[0].replaceAll("[^0-9.]", "");

                    try {
                        RestAction<Member> tempMember = event.getGuild().retrieveMemberById(Id);
                        Member providedMember = tempMember.complete();

                        eb = EmbedCreator.newCommandEmbedMessage(botUser);

                        if (embed) {
                            eb.setTitle("Kick");
                        }

                        StringBuilder sb = new StringBuilder();

                        // kick with reason
                        if (args.length > 1) {

                            for (int i = 1; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            event.getGuild()
                                    .kick(providedMember, sb.toString())
                                    .queue();
                        } else {

                            // kick without reason
                            event.getGuild()
                                    .kick(providedMember)
                                    .queue();

                        }

                        String desc = "User `" + providedMember.getUser().getAsTag() + "` was kicked " +
                                "successfully.";
                        if (embed) {
                            eb.setDescription(desc);

                        } else {
                            message = desc;
                        }

                        TextChannel modLogs = ModLogs.getModLogChannel(event, event.getGuild().getIdLong());

                        if (modLogs != null) {
                            String logMessage = "**Action:** Kick" + "\n" + "**User:** " +
                                    providedMember.getUser().getAsTag() + " (" +
                                    providedMember.getUser().getId() + ") " + "\n" + "**Reason:** ";

                            if (sb.toString().equals("")) {
                                logMessage += "None";
                            } else {
                                logMessage += sb.toString();
                            }

                            logMessage += "\n" + "**Moderator:** " + event.getAuthor().getAsTag();

                            if (embed) {
                                EmbedBuilder modLog = new EmbedBuilder()
                                        .setDescription(logMessage)
                                        .setColor(0xffa500)
                                        .setTimestamp(Instant.now());

                                modLogs.sendMessage(modLog.build())
                                        .queue();
                            } else {
                                modLogs.sendMessage(logMessage)
                                        .queue();
                            }
                        }

                    } catch (HierarchyException e) {
                        String desc = "Provided user has the same or greater permission levels as the bot.";
                        if (embed) {
                            eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);
                        } else {
                            message = "ERROR" + desc;
                        }
                    } catch (IllegalArgumentException | ErrorResponseException e) {
                        String desc = "Provided user is either invalid or is no longer in the server.";
                        if (embed) {
                            eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);
                        } else {
                            message = "ERROR: " + desc;
                        }
                    }
                }
            }
        }

        // send message
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
    public String getName() {
        return "kick";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public String getDescription() {
        return "Kicks the provided user with an optional reason";
    }

    @Override
    public String getUsage() {
        return "kick [userMention/userId] (reason)";
    }

    @Override
    public CommandType getType() {
        return CommandType.MODERATION;
    }
}
