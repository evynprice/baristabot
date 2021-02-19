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
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.Arrays;
import java.util.List;

public class Ban implements Command {

    /**
     * If command is ran in guild and bot + user have perms, bans user with optional message deletion time and reason
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {

        User botUser = event.getJDA().getSelfUser();
        Member botMember = event.getGuild().getSelfMember();
        EmbedBuilder eb = null;
        String message = null;


        // if command is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            eb = EmbedCreator.newErrorEmbedMessage(botUser, "This command can only be ran in servers.");

        } else {

            // get guildId, if mod-logs are enabled, and the bot account
            long guildId = event.getGuild().getIdLong();
            TextChannel modLogChannel = ModLogs.getModLogChannel(event, guildId);
            Member bot = event.getGuild().getSelfMember();

            // if no arguments are present, send error message
            if (args.length == 0) {

                if (embed) {
                    eb = EmbedCreator.newErrorEmbedMessage(botUser, "Invalid command usage. Please run `" +
                            prefix + "usage ban` for more information.");
                } else {
                    message = "ERROR: Invalid command usage. Please run `" + prefix + "usage" +
                            " ban` for more information.";
                }

            } else {

                // fetch User id
                String Id = args[0].replaceAll("[^0-9.]", "");

                Member providedMember = null;

                try {
                    RestAction<Member> tempMember = event.getGuild().retrieveMemberById(Id);
                    providedMember = tempMember.complete();

                    // member was not found
                } catch (IllegalArgumentException | ErrorResponseException e) {
                    if (embed) {
                        eb = EmbedCreator.newErrorEmbedMessage(botUser, "Provided member was not found.");
                    } else {
                        message = "ERROR: Provided member was not found.";
                    }
                }

                if (providedMember != null) {

                    // check if bot has required permissions
                    if (!botMember.canInteract(providedMember)) {
                        String desc = "The bot does not have the required permissions for this command. " +
                                "Missing permission `Ban Members`";
                        if (embed) {
                            eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);
                        } else {
                            message = "ERROR: " + desc;
                        }
                    } else {

                        // check if user has required permissions
                        if (!event.getMember().canInteract(providedMember)) {
                            String desc = "You do not have the required permissions for this command. " +
                                    "Missing permission `Ban Members`";
                            if (embed) {
                                eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);
                            } else {
                                message = "ERROR: " + desc;
                            }
                        } else {

                            String delDays = "";
                            String reason = "none";

                            // If no time or reason is specified
                            if (args.length == 1) {
                                providedMember.ban(0).queue();
                                delDays = "0";
                                reason = null;
                            } else {
                                // if the first argument is a number
                                if (args[1].matches("[0-9]*")) {
                                    // if the number is in proper delDay format
                                    if (args[1].matches("[0-7]")) {
                                        // no reason provided
                                        if (args.length == 2) {
                                            providedMember.ban(Integer.valueOf(args[1])).queue();
                                            delDays = args[1];
                                            reason = null;
                                        } else {
                                            // reason is provided
                                            StringBuilder sb = new StringBuilder();

                                            for (int i = 2; i < args.length; i++) {
                                                sb.append(args[i]).append(" ");
                                            }

                                            providedMember.ban(Integer.valueOf(args[1]), sb.toString()).queue();

                                            delDays = args[1];
                                            reason = sb.toString();
                                        }
                                    }
                                } else {
                                    // no time was provided but args are provided
                                    StringBuilder sb = new StringBuilder();

                                    for (int i=1; i < args.length; i++) {
                                        sb.append(args[i]).append(" ");
                                    }

                                    providedMember.ban(0, sb.toString()).queue();

                                    delDays = "0";
                                    reason = sb.toString();
                                }
                            }

                            // create ban message
                            if (embed) {
                                eb = EmbedCreator.newCommandEmbedMessage(botUser);
                                eb.setTitle("Ban")
                                        .setDescription("User `" + providedMember.getUser().getAsTag() +
                                                "` was banned successfully.");
                            } else {
                                message = "User `" + providedMember.getUser().getAsTag() + "` was banned " +
                                        "successfully.";
                            }

                            // send mod-logs message
                            TextChannel modLogs = ModLogs.getModLogChannel(event, guildId);

                            if (modLogs != null) {
                                String logMessage = "**Action:** Ban" + "\n" + "**User:** " +
                                        providedMember.getUser().getAsTag() + " (" +
                                        providedMember.getUser().getId() + ") " + "\n" + "**Message History " +
                                        "Deletion (Days):** " + delDays + "\n" + "**Reason:** " + reason +
                                        "\n" + "**Moderator:** " + event.getAuthor().getAsTag();
                                if (embed) {
                                    EmbedBuilder modLog = new EmbedBuilder()
                                            .setDescription(logMessage)
                                            .setColor(0xFF0000);

                                    event.getChannel()
                                            .sendMessage(modLog.build())
                                            .queue();
                                } else {
                                    event.getChannel()
                                            .sendMessage(logMessage)
                                            .queue();
                                }
                            }
                        }
                    }
                }
            }
        }

        if (eb != null) {
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else if (message != null) {
            event.getChannel()
                    .sendMessage(message)
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public String getDescription() {
        return "Bans the provided user and deletes messages sent by user based on days with optional reason";
    }

    @Override
    public String getUsage() {
        return "ban [userMention/userId] (delDays 0-7) (reason)";
    }

    @Override
    public CommandType getType() {
        return CommandType.MODERATION;
    }
}
