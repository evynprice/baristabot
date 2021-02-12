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
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Say implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User botUser = event.getJDA().getSelfUser();
        Member botMember = event.getGuild().getSelfMember();

        if (botMember.hasPermission(Permission.MESSAGE_MANAGE)) {

            if (args.length > 0) {
                StringBuilder sb = new StringBuilder();

                Arrays.stream(args)
                        .forEach(arg -> {
                            arg = arg.replace("@", "");
                            sb.append(arg).append(" ");
                        });

                event.getChannel()
                        .sendMessage(sb.toString())
                        .queue();

                event.getMessage()
                        .delete()
                        .queue();
            } else {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "Missing message. " +
                        "Try running `" + prefix + "usage say` for proper command usage.");
                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            }
        } else {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "The bot is missing the " +
                            "required permission `Manage Messages`.");
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
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
