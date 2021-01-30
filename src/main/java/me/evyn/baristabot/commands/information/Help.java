package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.commands.CommandWithCmds;
import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import me.evyn.baristabot.util.EasyEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Help implements CommandWithCmds {

    // boilerplate
    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final CommandType type;

    // required parameters
    private final String prefix;

    // addCommands method parameter
    private List<Command> cmds;

    public Help(String prefix) {
        this.name = "help";
        this.aliases = Arrays.asList("command", "cmd", "info");
        this.description = "Provides bot information" + "\n" +
                "Optional argument <command> for information on specific command";
        this.usage = "help <command>";
        this.type = CommandType.INFORMATION;

        this.prefix = prefix;
    }

    @Override
    public void addCommands(List<Command> cmds) {
        this.cmds = cmds;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        User bot = event.getJDA().getSelfUser();

        EmbedBuilder eb = new EmbedBuilder();

        // if there are no arguments, provide bot information and return
        if (args.isEmpty()) {
            eb.setColor(0x386895)
                    .setTitle(bot.getName())
                    .setDescription("Just a simple Discord bot written in JDA")
                    .setThumbnail(bot.getAvatarUrl())
                    .addField("Maintained by", "TheTechnicalFox#0056",true)
                    .addField("Version", "1.0", true)
                    .addField("Commands", this.prefix + "commands", false)
                    .setTimestamp(Instant.now())
                    .setFooter(bot.getName(), bot.getAvatarUrl());

            MessageBuilder message = new MessageBuilder();
            message.setEmbed(eb.build());
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
                            .newErrorEmbedMessage(bot, String.format("Format should be %shelp <command-name>", this.prefix))
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

        // Command has been found, now send help information on command
        eb.setColor(0x386895)
                .setTitle("Command: " + cmd.getName())
                .setDescription(cmd.getDescription())
                .addField("Aliases", cmd.getAliases().toString(), true)
                .addField("Usage", this.prefix + cmd.getUsage(), true)
                .setTimestamp(Instant.now())
                .setFooter(bot.getName(), bot.getAvatarUrl());

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
