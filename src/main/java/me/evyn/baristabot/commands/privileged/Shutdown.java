package me.evyn.baristabot.commands.privileged;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Shutdown implements Command {

    /**
     * This command simply stops the bot instance. Useful as a simple way of restarting the bot when combined with
     * restart unless-stopped
     */
    private String name;
    private List<String> aliases;
    private String description;
    private String usage;
    private CommandType type;

    public Shutdown() {
        this.name = "shutdown";
        this.aliases = Arrays.asList("stop");
        this.description = "Shuts down the bot instance (privileged users only)";
        this.usage = "shutdown";
        this.type = CommandType.PRIVILEGED;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
       JDA api = event.getJDA();
       api.shutdown();
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
