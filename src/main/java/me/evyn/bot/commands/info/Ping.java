package me.evyn.bot.commands.info;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Ping implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        String gatewayPing = String.valueOf(event.getJDA().getGatewayPing());

        event.getChannel()
                .sendMessage(String.format("Current gateway ping: %sms", gatewayPing))
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
        return "Provides the current bot ping";
    }

    @Override
    public String getUsage() {
        return "ping";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }
}
