package me.evyn.bot.commands.Settings;

import me.evyn.bot.resources.Config;
import me.evyn.bot.util.DataSourceCollector;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Prefix implements Setting {

    @Override
    public void edit(MessageReceivedEvent event, String prefix, String[] args) {
        User bot = event.getJDA().getSelfUser();

        // too ma ny arguments
        if(args.length > 2) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "Invalid command usage. Please run `" +
                    prefix + "settings help prefix` for more information.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        long guildId = event.getGuild().getIdLong();

        String newPrefix;
        if(args[1].equals("reset")) {
           newPrefix = Config.prefix;
        } else {
            newPrefix = args[1];
        }

        boolean status = DataSourceCollector.setPrefix(guildId, newPrefix);
        EmbedBuilder eb;

        if (status) {
            eb = EmbedCreator.newCommandEmbedMessage(bot);
            eb.setColor(0x00CC00)
                    .setDescription("Success! New prefix is `" + newPrefix + "`");
        } else {
            eb = EmbedCreator.newErrorEmbedMessage(bot, "There was an error resetting the prefix. Please " +
                    "try again later.");
        }

        event.getChannel()
                .sendMessage(eb.build())
                .queue();
    }

    @Override
    public void view(MessageReceivedEvent event, String prefix, String[] args) {
        event.getChannel()
                .sendMessage("Current prefix is: `" + prefix + "`")
                .queue();
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getDescription() {
        return "Changes the prefix that the bot listens to. Updating the prefix to the value `reset` will change the " +
                "prefix to the default value of `" + Config.prefix + "`";
    }

    @Override
    public String getUsage() {
        return "prefix (reset/newPrefix)";
    }

    @Override
    public String getExample() {
        return "prefix b!";
    }
}
