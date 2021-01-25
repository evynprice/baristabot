package me.evyn.baristabot.commands.fun;

import me.evyn.baristabot.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * Say Command that sends the arguments following the command in a new message and deletes the original message
 */
public class Say implements Command {

    private final String name = "say";
    private final String description = "Says the following text as the bot and deletes the original message";
    private final String usage = "say <content>";

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

        // replace mentions to avoid *problems* and add arguments to StringBuilder
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
}
