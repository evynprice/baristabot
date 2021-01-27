package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Simple ping command, responds with "Pong!"
 */
public class Ping implements Command {

    // boilerplate
    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final CommandType type;

    public Ping() {
        this.name = "ping";
        this.aliases = Arrays.asList("");
        this.description = "Replies with `Pong` in the channel that the command was sent";
        this.usage = "ping";
        this.type = CommandType.INFORMATION;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        event.getChannel()
                .sendMessage("Pong!")
                .queue();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public CommandType getType() {
        return this.type;
    }
}
