package me.evyn.bot.commands.settings;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Settings implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, List<String> args) {

        if (args.isEmpty()) {
            event.getChannel()
                    .sendMessage("Invalid usage: Please use " + prefix + " help settings for more information")
                    .queue();

            return;
        }

        if (args.get(0).equals("view")) {
            String setting = args.get(1);

            if (setting.equals("prefix")) {
                event.getChannel()
                        .sendMessage(prefix)
                        .queue();
            };
        }
    }

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("set", "config", "conf");
    }

    @Override
    public String getDescription() {
        return "Changes or views bot settings for the guild";
    }

    @Override
    public String getUsage() {
        return "settings [view/edit] [setting] (value)";
    }

    @Override
    public CommandType getType() {
        return CommandType.SETTINGS;
    }
}
