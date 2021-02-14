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
import me.evyn.bot.commands.CommandHandler;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Usage implements Command {
    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User bot = event.getJDA().getSelfUser();

        if (args.length == 0) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "Proper usage is " + prefix +
                    "usage [command]");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            String cmd = args[0];

            Command command = CommandHandler.findCommand(cmd);

            if (command == null) {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "Command was not found");

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                EmbedBuilder eb = EmbedCreator.newCommandEmbedMessage(bot);

                StringBuilder sb = new StringBuilder();

                if (command.getAliases().size() == 0) {
                    sb.append("None");
                } else {
                    for (String alias : command.getAliases()) {
                        sb.append(alias).append(", ");
                    }
                    sb.delete(sb.length() - 2, sb.length());
                }

                eb.setTitle("Usage: " + command.getName())
                        .setDescription("() Optional Argument" + "\n" + "[] Required Argument")
                        .addField("Command Usage", prefix + command.getUsage(), false)
                        .addField("Aliases", sb.toString(), false);

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            }
        }
    }

    @Override
    public String getName() {
        return "usage";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public String getDescription() {
        return "Provides the proper usage of a specific command";
    }

    @Override
    public String getUsage() {
        return "usage [command]";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }
}
