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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.Arrays;
import java.util.List;

public class Kick implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User botUser = event.getJDA().getSelfUser();

        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "This command can only be ran in servers.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        Member botMember = event.getGuild().getSelfMember();

        EmbedBuilder eb;

        if (!botMember.hasPermission(Permission.KICK_MEMBERS)) {
            // if Bot does not have Kick Members permission
            eb = EmbedCreator.newErrorEmbedMessage(botUser, "The bot is missing required " +
                    "permission `Kick Members`.");
        } else {

            if (!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                // if User does not have Kick Members permission
                eb = EmbedCreator.newErrorEmbedMessage(botUser,"User is missing required " +
                        "permission `Kick Members`.");
            } else {

                if (args.length == 0) {
                    // no user is provided
                    eb = EmbedCreator.newErrorEmbedMessage(botUser, "No user was provided " +
                            "For more information run the command `" + prefix + "usage kick`.");
                } else {
                    // fetch user Id
                    String Id = args[0].replaceAll("[^0-9.]", "");

                    try {
                        RestAction<Member> tempMember = event.getGuild().retrieveMemberById(Id);
                        Member providedMember = tempMember.complete();

                        eb = EmbedCreator.newCommandEmbedMessage(botUser);
                        eb.setTitle("Kick");

                        if (args.length > 1) {
                            // kick with reason
                            StringBuilder sb = new StringBuilder();

                            for (int i = 1; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            eb.setDescription("User `" + providedMember.getUser().getAsTag() + "` was kicked " +
                                            "successfully.")
                                    .addField("Reason", sb.toString(), false);
                        } else {
                            // kick without reason
                            event.getGuild()
                                    .kick(providedMember)
                                    .queue();

                                    eb.setDescription("User " + providedMember.getUser().getAsTag() + " was kicked " +
                                            "successfully.");

                        }
                    } catch (HierarchyException e) {
                        eb = EmbedCreator.newErrorEmbedMessage(botUser, "Provided user has the same " +
                                "or greater permission levels as the bot.");
                    } catch (Exception e) {
                        eb = EmbedCreator.newErrorEmbedMessage(botUser, "Provided user is either invalid " +
                                "or not in the server.");
                    }
                }
            }
        }
        // send message
        event.getChannel()
                .sendMessage(eb.build())
                .queue();
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
