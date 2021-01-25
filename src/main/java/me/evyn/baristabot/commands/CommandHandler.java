package me.evyn.baristabot.commands;

import me.evyn.baristabot.commands.fun.Say;
import me.evyn.baristabot.commands.information.Commands;
import me.evyn.baristabot.commands.information.Help;
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
    private Map<String, Command> cmds = new HashMap<>();

    // fun commands
    private final Command say;

    // information commands
    private final Command ping;
    private final Command help;
    private final Command commands;

    /**
     * This is ran only once when the Main Class starts. This is done so that all of the command objects are not
     * re-initialized every time that a new command is ran
     * @param prefix Bot prefix
     */
    public CommandHandler(String prefix) {
        this.prefix = prefix;

        // Initialize and add fun commands
        this.say = new Say(this.prefix);
        cmds.put(this.say.getName(), this.say);

        // Initialize and add information commands
        this.ping = new Ping();
        cmds.put(this.ping.getName(), this.ping);

        this.help = new Help(this.prefix, this.cmds);
        cmds.put(this.help.getName(), this.help);

        this.commands = new Commands(this.prefix, this.cmds);
        cmds.put(this.commands.getName(), this.commands);

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

        command.run(event, args);
    }
}
