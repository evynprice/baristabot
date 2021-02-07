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
import me.evyn.bot.commands.settings.Settings;
import me.evyn.bot.resources.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandHandler {


    // Elevated commands
    private static final Command shutdown;

    // Fun commands
    private static final Command say;

    // Information commands
    private static final CommandWithCmds aliases;
    private static final CommandWithCmds commands;
    private static final CommandWithCmds help;
    private static final Command ping;
    private static final Command statistics;
    private static final Command userinfo;

    // Settings command
    private static final Command settings;

    // Commands list
    public static final List<Command> cmds;

    static {
        // Initialize commands
        shutdown = new Shutdown();
        say = new Say();
        aliases = new Aliases();
        commands = new Commands();
        help = new Help();
        ping = new Ping();
        statistics = new Statistics();
        userinfo = new UserInfo();
        settings = new Settings();

        // Add commands to list
        cmds = Arrays.asList(say, aliases, commands, help, ping, statistics, userinfo, settings);

        // Send commands list to required commands
        aliases.addCommands(cmds);
        commands.addCommands(cmds);
        help.addCommands(cmds);

        // print the amount of commands loaded
        System.out.printf("Successfully loaded %d commands%n", cmds.size());
    }


    /**
     * Attempts to find command in command HashMap. If command is not found, sends error message. If command is found,
     * runs command method.
     * @param event Discord API message event
     * @param prefix Current server prefix
     * @param cmd String Command name
     * @param args List<String> command arguments
     */
    public static void run(MessageReceivedEvent event, String prefix, String cmd, List<String> args) {

        // Look for command in list
        Optional<Command> matching = cmds.stream()
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

        // if command is settings, check if user has administrator permissions
        if (command.getType() == CommandType.SETTINGS) {

            Member member = event.getMember();

            if (!member.hasPermission(Permission.ADMINISTRATOR)) {
                event.getChannel()
                        .sendMessage("You are missing the required permissions to run this command: " +
                                "ADMINISTRATOR")
                        .queue();
                return;
            }
        }

        // if command requires elevated permissions, check if author is the elevated user. Return error if not true.
        if (command.getType() == CommandType.ELEVATED) {

            if (Config.elevated == null) {

                System.out.println("Error running an elevated command: No elevated ID is defined in the config");
                event.getChannel()
                        .sendMessage("An error has occurred while running this command." +
                                "Please check the bot logs for more information")
                        .queue();

                return;
            }
            if (!event.getAuthor().getId().equals(Config.elevated)) {
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
