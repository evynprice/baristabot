package me.evyn.baristabot.commands.fun;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Say Command that sends the arguments following the command in a new message and deletes the original message
 */
public class Say implements Command {

    private String prefix;
    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final CommandType type;

    public Say(String prefix) {
        this.prefix = prefix;

        this.name = "say";
        this.aliases = Arrays.asList("speak");
        this.description = "Says the following text as the bot and deletes the original message";
        this.usage = "say [content";
        this.type = CommandType.FUN;
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
