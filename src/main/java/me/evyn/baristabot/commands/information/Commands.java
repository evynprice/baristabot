package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * This Class sends a list of valid commands when ran
 */
public class Commands implements Command {

    private final String name = "commands";
    private final String description = "Returns a list of valid bot commands";
    private final String usage = "commands";
    private final CommandType type = CommandType.INFORMATION;

    private final String prefix;
    private final Map<String, Command> cmds;

    public Commands(String prefix, Map<String, Command> cmds) {
        this.prefix = prefix;
        this.cmds = cmds;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        StringBuilder sb = new StringBuilder();

        // Add all of the commands in the map to the StringBuilder
        cmds.keySet().stream()
                .forEach(cmd -> {
                    sb.append(cmd).append(", ");
                });

        // Delete the last comma
        sb.deleteCharAt(sb.length() - 2);

        // Create the embed with the commands in the description
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0x386895)
                .setTitle("BaristaBot commands")
                .setDescription(String.format("Use `%shelp <command>` for information on a specific command", this.prefix))
                .setDescription(sb.toString())
                .setTimestamp(Instant.now())
                .setFooter("Barista Bot", "https://i.imgur.com/WtJZ3Wk.png");

        // Create a Message object with the built embed, send
        MessageBuilder message = new MessageBuilder();
        message.setEmbed(eb.build());
        event.getChannel()
                .sendMessage(message.build())
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

    @Override
    public CommandType getType() {
        return this.type;
    }
}
