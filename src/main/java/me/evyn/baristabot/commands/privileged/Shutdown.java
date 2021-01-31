package me.evyn.baristabot.commands.privileged;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Shutdown implements Command {

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
       JDA api = event.getJDA();
       api.shutdown();
    }

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("stop");
    }

    @Override
    public String getDescription() {
        return "Shuts down the bot instance (privileged users only)";
    }

    @Override
    public String getUsage() {
        return "shutdown";
    }

    @Override
    public CommandType getType() {
        return CommandType.PRIVILEGED;
    }
}
