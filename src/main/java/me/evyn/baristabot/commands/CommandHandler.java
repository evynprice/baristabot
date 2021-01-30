package me.evyn.baristabot.commands;

import me.evyn.baristabot.commands.CommandWithCmds;
import me.evyn.baristabot.commands.fun.Say;
import me.evyn.baristabot.commands.information.*;
import me.evyn.baristabot.commands.privileged.Shutdown;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

/**
 * This Class takes in all of the information that was gathered in the MessageListener event, and then runs
 * the correct command with that information.
 */
public class CommandHandler {

    // Constructor parameters
    private final String prefix;
    private final String privilegedID;

    // fun commands
    private final Command say;

    // information commands
    private final Command statistics;
    private final Command ping;
    private final CommandWithCmds help;
    private final CommandWithCmds commands;
    private final CommandWithCmds aliases;

    // privileged commands
    private final Command shutdown;

    // Commands list
    private List<Command> cmds;

    /**
     * This is ran only once when the Main Class starts. This is done so that all of the command objects are not
     * re-initialized every time that a new command is ran
     * @param prefix Bot prefix
     */
    public CommandHandler(String prefix, String privilegedID) {

        this.prefix = prefix;
        this.privilegedID = privilegedID;

        // Initialize commands
        this.say = new Say(this.prefix);
        this.statistics = new Statistics();
        this.ping = new Ping();
        this.help = new Help(this.prefix);
        this.commands = new Commands(this.prefix);
        this.shutdown = new Shutdown();
        this.aliases = new Aliases(this.prefix);

        cmds = Arrays.asList(say, statistics, ping, help, commands, aliases, shutdown);

        help.addCommands(cmds);
        commands.addCommands(cmds);
        aliases.addCommands(cmds);

        System.out.printf("Successfully loaded %d commands", this.cmds.size());
    }

    /**
     * Attempts to find command in command HashMap. If command is not found, sends error message. If command is found,
     * runs command method.
     * @param event
     * @param cmd
     * @param args
     */
    public void run(MessageReceivedEvent event, String cmd, List<String> args) {
        // Look for command in list
        Optional<Command> matching = this.cmds.stream()
                .filter(command -> command.getName().equals(cmd) || command.getAliases().contains(cmd))
                .findAny();
        Command command = matching.orElse(null);

        if (command == null) {
            event.getChannel()
                    .sendMessage("That command does not exist")
                    .queue();
            return;
        }

        if (command.getType() == CommandType.PRIVILEGED) {
            if (!event.getAuthor().getId().equals(this.privilegedID)) {
                event.getChannel()
                        .sendMessage("You do not have the required permission to run this command")
                        .queue();
                return;
            }
        }

        command.run(event, args);
    }
}
