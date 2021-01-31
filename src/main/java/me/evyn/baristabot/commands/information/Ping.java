package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Ping implements Command {

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        event.getChannel()
                .sendMessage("Pong!")
                .queue();
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("");
    }

    @Override
    public String getDescription() {
        return "Replies with `Pong` in the channel that the command was sent";
    }

    @Override
    public String getUsage() {
        return "ping";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFORMATION;
    }
}
