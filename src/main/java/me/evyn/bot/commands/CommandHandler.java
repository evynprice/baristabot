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

package me.evyn.bot.commands;

import me.evyn.bot.commands.elevated.Shutdown;
import me.evyn.bot.commands.fun.*;
import me.evyn.bot.commands.information.*;
import me.evyn.bot.resources.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandHandler {

    // Constructor parameters
    private final Config config;
    private final String elevated;

    // Elevated commands
    private final Command shutdown;

    // Fun commands
    private final Command say;

    // Information commands
    private final CommandWithCmds aliases;
    private final CommandWithCmds commands;
    private final CommandWithCmds help;
    private final Command ping;
    private final Command statistics;
    private final Command userinfo;

    // Commands list
    private List<Command> cmds;

    /**
     * Initializes bot commands when application starts and prints the amount of successful commands that were loaded
     * @param config Bot configuration
     */
    public CommandHandler(Config config) {

        this.config = config;
        this.elevated = config.getElevated();

        // Initialize commands
        this.shutdown = new Shutdown();
        this.say = new Say();
        this.aliases = new Aliases();
        this.commands = new Commands();
        this.help = new Help();
        this.ping = new Ping();
        this.statistics = new Statistics();
        this.userinfo = new UserInfo();

        // Add commands to list
        cmds = Arrays.asList(say, aliases, commands, help, ping, statistics, userinfo);

        // Send commands list to required commands
        this.aliases.addCommands(cmds);
        this.commands.addCommands(cmds);
        this.help.addCommands(cmds);

        // print the amount of commands loaded
        System.out.printf("Successfully loaded %d commands%n", this.cmds.size());
    }

    /**
     * Gets a list of the bot commands
     * @return List<Command> bot commands
     */
    public List<Command> getCommands() {
        return this.cmds;
    }

    /**
     * Attempts to find command in command HashMap. If command is not found, sends error message. If command is found,
     * runs command method.
     * @param event Discord API message event
     * @param prefix Current server prefix
     * @param cmd String Command name
     * @param args List<String> command arguments
     */
    public void run(MessageReceivedEvent event, String prefix, String cmd, List<String> args) {

        // Look for command in list
        Optional<Command> matching = this.cmds.stream()
                .filter(command -> command.getName().equals(cmd) || command.getAliases().contains(cmd))
                .findAny();
        Command command = matching.orElse(null);

        // return error if command is not found
        if (command == null) {
            event.getChannel()
                    .sendMessage("That command does not exist")
                    .queue();
            return;
        }

        // if command requires elevated permissions, check if author is the elevated user. Return error if not true.
        if (command.getType() == CommandType.ELEVATED) {

            if (this.elevated == null) {

                System.out.println("Error running an elevated command: No elevated ID is defined in the config");
                event.getChannel()
                        .sendMessage("An error has occured when running this command." +
                                "Please check the bot logs for more information")
                        .queue();

                return;
            }
            if (!event.getAuthor().getId().equals(this.elevated)) {
                event.getChannel()
                        .sendMessage("You do not have the required permission to run this command")
                        .queue();
                return;
            }
        }

        // run the command that was requested
        command.run(event, prefix, args);
    }
}
