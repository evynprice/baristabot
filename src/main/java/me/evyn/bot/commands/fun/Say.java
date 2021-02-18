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
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Say implements Command {

    /**
     * If command is ran in guild, clean any mentions in command arguments and send cleaned content. Then delete
     * the original message
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {

        User botUser = event.getJDA().getSelfUser();

        // If message is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "This command can only be ran in servers.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        Member botMember = event.getGuild().getSelfMember();

        // check if bot has Manage Messages permissions
        if (botMember.hasPermission(Permission.MESSAGE_MANAGE)) {

            if (args.length > 0) {
                StringBuilder sb = new StringBuilder();

                // delete all occurrences of "@" and append arguments together
                Arrays.stream(args)
                        .forEach(arg -> {
                            arg = arg.replace("@", "");
                            sb.append(arg).append(" ");
                        });

                // send cleaned command arguments
                event.getChannel()
                        .sendMessage(sb.toString())
                        .queue();

                // delete original message
                event.getMessage()
                        .delete()
                        .queue();
            } else {
                // no command arguments present
                if (embed) {
                    EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "Missing message. " +
                            "Try running `" + prefix + "usage say` for proper command usage.");

                    event.getChannel()
                            .sendMessage(eb.build())
                            .queue();
                    return;
                } else {
                    event.getChannel()
                            .sendMessage("ERROR: Missing message. " + "\n"
                                    + "Try running `" + prefix + "usage say` for proper command usage.")
                            .queue();
                    return;
                }

            }
        } else {
            // bot is missing manage messages
            if (embed) {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "The bot is missing the " +
                        "required permission `Manage Messages`.");
                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
                return;
            } else {
                event.getChannel()
                        .sendMessage("ERROR: The bot is missing the required permission `Manage Messages`.")
                        .queue();
            }
        }
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("speak");
    }

    @Override
    public String getDescription() {
        return "Says the provided content and deletes the original message";
    }

    @Override
    public String getUsage() {
        return "say [content]";
    }

    @Override
    public CommandType getType() {
        return CommandType.FUN;
    }
}
