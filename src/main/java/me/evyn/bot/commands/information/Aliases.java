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

package me.evyn.bot.commands.information;

import me.evyn.bot.commands.CommandWithCmds;
import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.EasyEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Aliases implements CommandWithCmds {

    private List<Command> cmds;

    /**
     * This is used to pass the MessageListener commands list to the Aliases object. This has to be done after all of
     * the commands are initialized so that it includes the full list.
     * @param cmds bot commands
     */
    @Override
    public void addCommands(List<Command> cmds) {
        this.cmds = cmds;
    }

    /**
     * Sends a list of aliases for the command name that is provided
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, List<String> args) {

        // get bot user
        User bot = event.getJDA().getSelfUser();

        EmbedBuilder eb = new EmbedBuilder();

        // if no args are present, send error message and return
        if (args.isEmpty()) {
            eb = EasyEmbed.newErrorEmbedMessage(bot, "No arguments were given. " +
                    "Run `" + prefix + "help aliases` for more information");
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // if there are more than 1 arguments, send error message and return
        if (args.size() > 1) {
            eb = EasyEmbed.newErrorEmbedMessage(bot, String.format("Too many arguments." +
                    " Run `%shelp aliases` for more information", prefix));
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // Valid arguments are present, now attempt to find command in list
        String commandName = args.get(0);
        Optional<Command> matching = this.cmds.stream()
                // filter by command name and command aliases
                .filter(command -> command.getName().equals(commandName) || command.getAliases().contains(commandName))
                .findAny();

        Command cmd = matching.orElse(null);

        // If command is not in list, send error message and return
        if (cmd == null) {
            event.getChannel()
                    .sendMessage(String.format("Command %s was not found.", commandName))
                    .queue();
            return;
        }

        // create embed
        eb = EasyEmbed.newCommandEmbedMessage(bot);
        eb.setTitle("Command Aliases: " + cmd.getName())
                .setDescription(cmd.getAliases().toString());

        // send created embed message
        event.getChannel()
                .sendMessage(eb.build())
                .queue();
    }


    @Override
    public String getName() {
        return "aliases";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("");
    }

    @Override
    public String getDescription() {
        return "Provides the aliases for the command given:";
    }

    @Override
    public String getUsage() {
        return "aliases [command]";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFORMATION;
    }
}
