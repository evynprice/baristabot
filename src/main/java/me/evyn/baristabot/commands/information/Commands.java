package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.commands.CommandWithCmds;
import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands implements CommandWithCmds {

    // required parameters
    private final String prefix;

    // addCommands method parameters
    private List<Command> cmds;

    // These hold all of the command names sorted by type
    private List<String> funCommands;
    private List<String> infoCommands;

    // Formatted strings of command names
    private String fun;
    private String info;


    public Commands(String prefix) {
        this.prefix = prefix;
        this.funCommands = new ArrayList<>();
        this.infoCommands = new ArrayList<>();
    }

    @Override
    public void addCommands(List<Command> cmds) {
        this.cmds = cmds;

        // Extra logic is here to sort commands by their type and add their names to the appropriate ArrayLists
        this.cmds.stream()
                .forEach(cmd -> {
                    if (cmd.getType().equals(CommandType.FUN)) {
                        this.funCommands.add(cmd.getName());
                    }
                    else if (cmd.getType().equals(CommandType.INFORMATION)) {
                        this.infoCommands.add(cmd.getName());
                    }
                });

        // Extra logic to format commands as string
        this.fun = this.commandFormatter(this.funCommands);
        this.info = this.commandFormatter(this.infoCommands);
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        User bot = event.getJDA().getSelfUser();

        // Create the embed with the commands as fields
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0x386895)
                .setTitle(bot.getName() + " commands")
                .setDescription(String.format("Use `%shelp <command>` for information on a specific command", this.prefix))
                .addField("Fun", this.fun,false)
                .addField("Information", this.info, false)
                .setTimestamp(Instant.now())
                .setFooter(bot.getName(), bot.getAvatarUrl());

        // Create a Message object with the built embed, send message
        MessageBuilder message = new MessageBuilder();
        message.setEmbed(eb.build());
        event.getChannel()
                .sendMessage(message.build())
                .queue();
    }

    /**
     * Takes in list of command names and returns formatted version
     * @param commandType
     * @return formatted String
     */
    private String commandFormatter(List<String> commandType) {
        StringBuilder sb = new StringBuilder();
        commandType.stream()
                .forEach(cmd -> {
                    sb.append(cmd).append(", ");
                });

        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

    @Override
    public String getName() {
        return "commands";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("cmds");
    }

    @Override
    public String getDescription() {
        return "Returns a list of valid bot commands";
    }

    @Override
    public String getUsage() {
        return "commands";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFORMATION;
    }
}
