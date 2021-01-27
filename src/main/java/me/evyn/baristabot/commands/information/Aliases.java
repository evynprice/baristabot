package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.CommandWithCmds;
import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import me.evyn.baristabot.util.EasyEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Aliases implements CommandWithCmds {

    // Boilerplate
    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final CommandType type;

    // Constructor parameters
    private final String prefix;

    private List<Command> cmds;

    public Aliases(String prefix) {
        this.name = "aliases";
        this.aliases = Arrays.asList("");
        this.description = "Provides the aliases for the command given";
        this.usage = "aliases [command]";
        this.type = CommandType.INFORMATION;

        this.prefix = prefix;
    }

    @Override
    public void addCommands(List<Command> cmds) {
        this.cmds = cmds;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        if (args.isEmpty()) {
            EasyEmbed embed = new EasyEmbed();
            MessageBuilder message = embed.newErrorEmbedMessage("No arguments were given. " +
                    "Run `" + this.prefix + "help aliases` for more information");
            event.getChannel()
                    .sendMessage(message.build())
                    .queue();
            return;
        }

        // if there are more than 1 arguments, send error message and return
        if (args.size() > 1) {
            EasyEmbed embed = new EasyEmbed();

            event.getChannel()
                    .sendMessage(embed
                            .newErrorEmbedMessage(String.format("Too many arguments. Run `%shelp aliases` for more information", this.prefix))
                            .build())
                    .queue();
            return;
        }

        // Valid arguments are present, now attempt to find command in list
        String commandName = args.get(0);
        Optional<Command> matching = this.cmds.stream()
                // filter by command name and command aliases
                .filter(command -> command.getName().equals(commandName) || command.getAliases().contains(commandName))
                .findAny();

        Command cmd = matching.orElse(null);

        // If command is not in list, send error message and return
        if (cmd == null) {
            event.getChannel()
                    .sendMessage(String.format("Command %s was not found.", commandName))
                    .queue();
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        // Command has been found, now send help information on command
        eb.setColor(0x386895)
                .setTitle("Command: " + cmd.getName())
                .setDescription(cmd.getAliases().toString())
                .setTimestamp(Instant.now())
                .setFooter("Barista Bot", "https://i.imgur.com/WtJZ3Wk.png");

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
