package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * The Help command provides information on the bot if no arguments are present, or provides information on a
 * specific command if included in the arguments list.
 */
public class Help implements Command {

    private final String name = "help";
    private final String description = "Provides bot information and information on a specific command";
    private final String usage = "help [command]";

    private final String prefix;
    private final Map<String, Command> cmds;

    public Help(String prefix, Map<String, Command> cmds) {
        this.prefix = prefix;
        this.cmds = cmds;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        EmbedBuilder eb = new EmbedBuilder();

        // if there are no arguments, provide bot information and return
        if (args.isEmpty()) {
            eb.setColor(0x386895)
                    .setTitle("Barista Bot")
                    .setDescription("Just a simple Discord bot written in JDA")
                    .setThumbnail("https://i.imgur.com/WtJZ3Wk.png")
                    .addField("Version", "1.0", true)
                    .addField("Maintained by", "TheTechnicalFox#0056",true)
                    .setTimestamp(Instant.now())
                    .setFooter("Barista Bot", "https://i.imgur.com/WtJZ3Wk");

            MessageBuilder message = new MessageBuilder();
            message.setEmbed(eb.build());
            event.getChannel()
                    .sendMessage(message.build())
                    .queue();

            return;
        }

        // if there are more than 1 arguments, send error message and return
        if (args.size() > 1) {
            event.getChannel()
                    .sendMessage(String.format("Invalid arguments. Format should be %shelp <command-name", this.prefix))
                    .queue();
            return;
        }

        // Valid arguments are present, now attempt to find command in list
        String commandName = args.get(0);
        Command cmd = this.cmds.get(commandName);

        // If command is not in list, send error message and return
        if (cmd == null) {
            event.getChannel()
                    .sendMessage(String.format("Command %s was not found.", commandName))
                    .queue();
            return;
        }

        // Command has been found, now send help information on command
        eb.setColor(0x386895)
                .setTitle(cmd.getName() + " command")
                .setDescription(cmd.getDescription())
                .addField("Usage", this.prefix + cmd.getUsage(), true)
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
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }
}
