package me.evyn.baristabot.commands;

import me.evyn.baristabot.commands.fun.Say;
import me.evyn.baristabot.commands.information.Commands;
import me.evyn.baristabot.commands.information.Help;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.evyn.baristabot.commands.testing.Ping;

/**
 * This Class takes in all of the information that was gathered in the MessageListener event, and then runs
 * the correct command with that information.
 */
public class CommandHandler {

    private final String prefix;
    private Map<String, Command> cmds = new HashMap<>();

    private final Command ping;
    private final Command say;
    private final Command help;
    private final Command commands;

    /**
     * This is ran only once when the Main Class starts. This is done so that all of the command objects are not
     * re-initialized every time that a new command is ran
     * @param prefix Bot prefix
     */
    public CommandHandler(String prefix) {
        this.prefix = prefix;

        // Initializes commands
        this.ping = new Ping();
        this.say = new Say(this.prefix);
        this.help = new Help(this.prefix, this.cmds);
        this.commands = new Commands(this.prefix, this.cmds);

        // Add commands to map. There is probably a more efficient way to do this
        cmds.put(this.ping.getName(), this.ping);
        cmds.put(this.say.getName(), this.say);
        cmds.put(this.help.getName(), this.help);
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
