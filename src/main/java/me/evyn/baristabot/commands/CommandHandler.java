package me.evyn.baristabot.commands;

import me.evyn.baristabot.commands.fun.Say;
import me.evyn.baristabot.commands.information.Commands;
import me.evyn.baristabot.commands.information.Help;
import me.evyn.baristabot.commands.information.Statistics;
import me.evyn.baristabot.commands.privileged.Shutdown;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.evyn.baristabot.commands.information.Ping;

/**
 * This Class takes in all of the information that was gathered in the MessageListener event, and then runs
 * the correct command with that information.
 */
public class CommandHandler {

    private final String prefix;
    private final String privilegedID;
    private Map<String, Command> cmds = new HashMap<>();

    // fun commands
    private final Command say;

    // information commands
    private final Command statistics;
    private final Command ping;
    private final Command help;
    private final Command commands;

    // privileged commands
    private final Command shutdown;

    /**
     * This is ran only once when the Main Class starts. This is done so that all of the command objects are not
     * re-initialized every time that a new command is ran
     * @param prefix Bot prefix
     */
    public CommandHandler(String prefix, String privilegedID) {
        this.prefix = prefix;
        this.privilegedID = privilegedID;

        // Initialize and add fun commands
        this.say = new Say(this.prefix);
        cmds.put(this.say.getName(), this.say);

        // Initialize and add information commands
        this.statistics = new Statistics();
        cmds.put(this.statistics.getName(), this.statistics);

        this.ping = new Ping();
        cmds.put(this.ping.getName(), this.ping);

        this.help = new Help(this.prefix, this.cmds);
        cmds.put(this.help.getName(), this.help);

        this.commands = new Commands(this.prefix, this.cmds);
        cmds.put(this.commands.getName(), this.commands);

        // Initialize and add privileged commands
        this.shutdown = new Shutdown();
        cmds.put(this.shutdown.getName(), this.shutdown);

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
        // Look for command in map
        Command command = this.cmds.getOrDefault(cmd, null);
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
