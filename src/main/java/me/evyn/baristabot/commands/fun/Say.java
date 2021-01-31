package me.evyn.baristabot.commands.fun;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Say implements Command {

    private String prefix;

    public Say(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        if (args.isEmpty()) {
            event.getChannel()
                    .sendMessage(String.format("There were no arguments. Try running %shelp for more information", this.prefix))
                    .queue();
            return;
        }

        StringBuilder sb = new StringBuilder();

        // replace mentions with blank string and add arguments to StringBuilder
        args.stream()
                .forEach(arg -> {
                    arg = arg.replace("@", "");
                    sb.append(arg).append(" ");
                });

        // Send StringBuilder to new message
        event.getChannel()
                .sendMessage(sb.toString())
                .queue();

        // delete original message
        event.getMessage()
                .delete()
                .queue();
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("speak");
    }

    @Override
    public String getDescription() {
        return "Says the following text as the bot and deletes the original message";
    }

    @Override
    public String getUsage() {
        return "say [content]";
    }

    @Override
    public CommandType getType() {
        return CommandType.FUN;
    }
}
