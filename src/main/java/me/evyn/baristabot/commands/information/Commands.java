package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.CommandWithCmds;
import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This Class sends a list of valid commands when ran
 */
public class Commands implements CommandWithCmds {

    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final CommandType type;

    private final String prefix;
    private List<Command> cmds;

    // These hold organized lists of all of the commands
    private List<Command> funCommands;
    private List<Command> infoCommands;


    public Commands(String prefix) {
        this.prefix = prefix;

        this.name = "commands";
        this.aliases = Arrays.asList("cmds");
        this.description = "Returns a list of valid bot commands";
        this.usage = "commands";
        this.type = CommandType.INFORMATION;

        this.funCommands = new ArrayList<>();
        this.infoCommands = new ArrayList<>();
    }

    public void addCommands(List<Command> cmds) {
        this.cmds = cmds;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {

        // This would preferably be ran in the constructor, but is here so that this command is also added to the list
        // Takes a stream of the command map, and adds to the instance lists for the appropriate command type
        this.cmds.stream()
                .forEach(cmd -> {
                    if (cmd.getType().equals(CommandType.FUN)) {
                        this.funCommands.add(cmd);
                    }
                    else if (cmd.getType().equals(CommandType.INFORMATION)) {
                        this.infoCommands.add(cmd);
                    }
                });

        // (Developer) needs to be refactored.
        // Creates a string object for each of the command type instance variables, and then adds the name of each
        // command to the object. Then removes the last comma and space

        StringBuilder funString = new StringBuilder();
        this.funCommands.stream()
                .forEach(cmd -> {
                    funString.append(cmd.getName()).append(", ");
                });
        funString.delete(funString.length() - 2, funString.length() - 1);

        StringBuilder infoString = new StringBuilder();
        this.infoCommands.stream()
                .forEach(cmd -> {
                    infoString.append(cmd.getName()).append(", ");
                });
        infoString.delete(infoString.length() - 2, infoString.length() - 1);


        // Create the embed with the commands as fields
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0x386895)
                .setTitle("BaristaBot commands")
                .setDescription(String.format("Use `%shelp <command>` for information on a specific command", this.prefix))
                .addField("Fun",funString.toString(),false)
                .addField("Information",infoString.toString(), false)
                .setTimestamp(Instant.now())
                .setFooter("Barista Bot", "https://i.imgur.com/WtJZ3Wk.png");

        // Create a Message object with the built embed, send message
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
