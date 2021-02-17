package me.evyn.bot.commands;

import me.evyn.bot.util.DataSourceCollector;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Settings implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {
        if (args.length == 0) {
            return;
        }

        if (args[0].equals("prefix")) {
            long guildId = event.getGuild().getIdLong();

            if (args.length == 1) {
                String dbPrefix = DataSourceCollector.getPrefix(guildId);
                event.getChannel()
                        .sendMessage("Current Prefix is: " + dbPrefix)
                        .queue();
            } else {
                boolean status = DataSourceCollector.setPrefix(guildId, args[1]);
                if (status == true) {
                    event.getChannel()
                            .sendMessage("Prefix updated successfully!")
                            .queue();
                }
            }
            }
    }

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("setting", "config", "conf");
    }

    @Override
    public String getDescription() {
        return "Changes per-guild settings";
    }

    @Override
    public String getUsage() {
        return "setting [setting] (value)";
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMIN;
    }
}
