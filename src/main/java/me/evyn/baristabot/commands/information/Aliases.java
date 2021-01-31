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

public class Aliases implements CommandWithCmds {

    private final String prefix;
    private List<Command> cmds;

    public Aliases(String prefix) {
        this.prefix = prefix;
    }

    /**
     * This is used to pass the MessageListener commands list to the Aliases object. This has to be done after all of
     * the commands are initialized so that it includes the full list.
     * @param cmds
     */
    @Override
    public void addCommands(List<Command> cmds) {
        this.cmds = cmds;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        User bot = event.getJDA().getSelfUser();

        // if no args are present, send error message and return
        if (args.isEmpty()) {
            EasyEmbed embed = new EasyEmbed();
            MessageBuilder message = embed.newErrorEmbedMessage(bot, "No arguments were given. " +
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
                            .newErrorEmbedMessage(bot, String.format("Too many arguments. Run `%shelp aliases` for more information", this.prefix))
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

        // Command has been found, create new embed
        EmbedBuilder eb = new EmbedBuilder();

        eb.setColor(0x386895)
                .setTitle("Command: " + cmd.getName())
                .setDescription(cmd.getAliases()
                        .toString())
                .setTimestamp(Instant.now())
                .setFooter(bot.getName(), bot.getAvatarUrl());

        // send created embed message
        MessageBuilder message = new MessageBuilder();
        message.setEmbed(eb.build());
        event.getChannel()
                .sendMessage(message.build())
                .queue();
    }


    @Override
    public String getName() {
        return "aliases";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("");
    }

    @Override
    public String getDescription() {
        return "Provides the aliases for the command given:";
    }

    @Override
    public String getUsage() {
        return "aliases [command]";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFORMATION;
    }
}
