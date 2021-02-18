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
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
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

        // if command is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "This command can only be ran in servers.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        Member botMember = event.getGuild().getSelfMember();

        EmbedBuilder eb;

        // if no arguments are present send error message
        if (args.length == 0) {
            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(botUser, "Invalid command usage. Please run `" +
                        prefix + "usage ban` for more information.");

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                event.getChannel().sendMessage("ERROR: Invalid command usage. Please run `" + prefix + "usage" +
                        " ban` for more information.")
                        .queue();
            }
            return;
        }

        // fetch User id
        String Id = args[0].replaceAll("[^0-9.]", "");

        Member providedMember;

        try {
            RestAction<Member> tempMember = event.getGuild().retrieveMemberById(Id);
            providedMember = tempMember.complete();

            // member was not found
        } catch (IllegalArgumentException | ErrorResponseException e) {

            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(botUser, "Provided member was not found.");

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                event.getChannel()
                        .sendMessage("ERROR: Provided member was not found.")
                        .queue();
            }
            return;
        }

        // check if bot has required permissions
        if (!botMember.canInteract(providedMember)) {
            String desc = "The bot does not have the required permissions for this command. Missing permission " +
                    "`Ban Members`";

            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);

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

        // check if user has required permissions
        if (!event.getMember().canInteract(providedMember)) {
            String desc = "You do not have the required permissions for this command. Missing permission `Ban Members`";

            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);

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

        eb = EmbedCreator.newCommandEmbedMessage(botUser);
        String banMsg = "";
        if (embed) {
            eb.setTitle("Ban");
            eb.setDescription("User `" + providedMember.getUser().getAsTag() + "` was banned successfully.");
        } else {
            banMsg = "User `" + providedMember.getUser().getAsTag() + "` was banned successfully.";
        }

        // Run command if no time or reason is specified
        if (args.length == 1) {
            providedMember.ban(0).queue();

            if (embed) {
                eb.addField("Deleted message history (days)", "0", true);

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                banMsg += "\n" + "Deleted message history (days): 0";
                event.getChannel()
                        .sendMessage(banMsg)
                        .queue();
            }
            return;
        }

        // if the first argument is a number
        if (args[1].matches("[0-9]*")) {

            // if the number is in proper delDay format
            if (args[1].matches("[0-7]")) {

                // no reason provided
                if (args.length == 2) {
                    providedMember.ban(Integer.valueOf(args[1])).queue();

                    if (embed) {
                        eb.addField("Deleted message history (days)", args[1], true);

                        event.getChannel()
                                .sendMessage(eb.build())
                                .queue();
                    } else {
                        banMsg += "\n" + "Deleted message history (days): " + args[1];
                        event.getChannel()
                                .sendMessage(banMsg)
                                .queue();
                    }

                    return;
                } else {
                    // reason provided

                    StringBuilder sb = new StringBuilder();

                    for (int i=2; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }

                    providedMember.ban(Integer.valueOf(args[1]), sb.toString()).queue();

                    if (embed) {
                        eb.addField("Deleted message history (days)", args[1], true)
                                .addField("Reason", sb.toString(), false);

                        event.getChannel()
                                .sendMessage(eb.build())
                                .queue();
                    } else {
                        banMsg += "\n" + "Deleted message history (days): " + args[1] + "\n" + "Reason: " +
                                sb.toString();
                        event.getChannel()
                                .sendMessage(banMsg)
                                .queue();
                    }

                    return;
                }

            } else {
                String desc = "Invalid arguments. Please run `" + prefix + "usage ban` for more information.";
                if (embed) {
                    eb = EmbedCreator.newErrorEmbedMessage(botUser, desc);

                    event.getChannel()
                            .sendMessage(eb.build())
                            .queue();
                } else {
                    event.getChannel()
                            .sendMessage(desc)
                            .queue();
                }

                return;
            }
        }

        // No time was provided but args were provided
        StringBuilder sb = new StringBuilder();

        for (int i=1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        providedMember.ban(0, sb.toString()).queue();

        if (embed) {
            eb.addField("Deleted message history (days)", "0", true);
            eb.addField("Reason", sb.toString(), false);

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            banMsg += "Deleted message history (days): 0" + "\n" + "Reason: " + sb.toString();
            event.getChannel()
                    .sendMessage(banMsg)
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
