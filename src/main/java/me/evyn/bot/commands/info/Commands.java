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

public class Commands implements Command {

    /**
     * Provides a list of bot commands separated by type
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User bot = event.getJDA().getSelfUser();

        // create StringBuilder objects for each command type
        StringBuilder info = new StringBuilder();
        StringBuilder fun = new StringBuilder();
        StringBuilder moderation = new StringBuilder();
        StringBuilder admin = new StringBuilder();

        // stream commands and organize by type
        CommandHandler.commands.stream()
                .forEach(command -> {
                    if (command.getType() == CommandType.INFO) {
                        info.append(command.getName()).append(", ");
                    } else if (command.getType() == CommandType.FUN) {
                        fun.append(command.getName()).append(", ");
                    } else if (command.getType() == CommandType.MODERATION) {
                        moderation.append(command.getName()).append(", ");
                    } else if (command.getType() == CommandType.ADMIN) {
                        admin.append(command.getName()).append(", ");
                    }
                });

        // delete last comma and space of each string
        info.delete(info.length() - 2, info.length());
        fun.delete(fun.length() - 2, fun.length());
        admin.delete(admin.length() - 2, admin.length());

        // build embed and send
        EmbedBuilder eb = EmbedCreator.newCommandEmbedMessage(bot);
        eb.setTitle("Bot commands")
                .setDescription("To run any command, type `" + prefix + "commandName (arguments)`" + "\n" +
                        "To find the proper usage of a command, run `" + prefix + "usage commandName`")
                .addField("Info", info.toString(), false)
                .addField("Fun", fun.toString(), false)
                .addField("Moderation", moderation.toString(), false)
                .addField("Admin", admin.toString(), false);

        event.getChannel()
                .sendMessage(eb.build())
                .queue();
    }

    @Override
    public String getName() {
        return "commands";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("commands", "cmds");
    }

    @Override
    public String getDescription() {
        return "Provides a list of bot commands";
    }

    @Override
    public String getUsage() {
        return "commands";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }
}
