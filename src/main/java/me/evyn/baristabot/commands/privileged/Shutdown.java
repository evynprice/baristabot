package me.evyn.baristabot.commands.privileged;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Restart implements Command {

    /**
     * This command works by shutting down the bot instance. This should only be ran 
     */
    private String name = "restart";
    private String description = "Restarts bot instance (privileged users only)";
    private String usage = "restart";
    private CommandType type = CommandType.PRIVILEGED;

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
