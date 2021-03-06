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

package me.evyn.bot.commands.info;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UserInfo implements Command {

    /**
     * If ran in guild, provides information on user account
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param embed Guild embed setting
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {

        User bot = event.getJDA().getSelfUser();

        // if command is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "This command can only be ran in servers");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        Member member;
        User user;

        // no arguments are present
        if (args.length == 0) {
            member = event.getMember();
            user = member.getUser();
        } else {
            // get Id from argument
            String Id = args[0].replaceAll("[^0-9.]", "");

            // attempt to find user with Id
            try {
                RestAction<Member> tempMember = event.getGuild().retrieveMemberById(Id);
                member = tempMember.complete();
                user = member.getUser();
            } catch (IllegalArgumentException | ErrorResponseException e) {
                // user could not be found
                String desc = "That ID is either invalid or the user provided is not in the server.";
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
        }

        // create date time for user join and register date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        LocalDateTime registerTimeCreated = user.getTimeCreated().toLocalDateTime();
        LocalDateTime joinTimeCreated = member.getTimeJoined().toLocalDateTime();

        String registerDate = formatter.format(registerTimeCreated);
        String joinDate = formatter.format(joinTimeCreated);

        // find and format roles
        StringBuilder roles = new StringBuilder();

        if (member.getRoles().size() > 0) {
            member.getRoles().stream()
                    .map(role -> role.getName())
                    .forEach(role -> roles.append(role).append(", "));
            roles.delete(roles.length() - 2, roles.length() - 1);
        } else {
            roles.append("none");
        }


        if (embed) {
            EmbedBuilder eb = EmbedCreator.newCommandEmbedMessage(bot);

            eb.setTitle(user.getAsTag())
                    .setThumbnail(user.getAvatarUrl())
                    .addField("ID", user.getId(), true)
                    .addField("Bot", String.valueOf(user.isBot()), true)
                    .addField("Joined", joinDate, true)
                    .addField("Registered", registerDate, false)
                    .addField("Roles (" + member.getRoles().size() + ")", roles.toString(), true);

            // send embed
            event.getTextChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            event.getChannel()
                    .sendMessage("**User: **" + user.getAsTag() + "\n\n" + "**ID:** " + user.getId() + "\n" +
                            "**Bot:** " + user.isBot() + "\n" + "**Joined:** " + joinDate + "\n" + "**Registered:** " +
                            registerDate + "\n" + "**Roles (" + member.getRoles().size() + "):** " + roles.toString())
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("user");
    }

    @Override
    public String getDescription() {
        return "Provides information about you or a specific user";
    }

    @Override
    public String getUsage() {
        return "userinfo (userID/userMention)";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }
}
