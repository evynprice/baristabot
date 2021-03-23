package me.evyn.barista.core.commands;

import me.evyn.barista.core.utils.BaristaInfo;
import me.evyn.barista.core.utils.Command;
import me.evyn.barista.core.utils.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;

public class Stats implements Command {
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embeds, String[] args) {
        JDA api = event.getJDA();
        User bot = api.getSelfUser();

        // get process memory usage
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long usedMemory = totalMemory - Runtime.getRuntime().freeMemory() / (1024 * 1024);

        String memory = usedMemory + "MB / " + totalMemory + "MB";

        int guilds = api.getGuilds().size();
        int channels = api.getTextChannels().size();
        int users = api.getUsers().size();

        if (embeds) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(bot.getName() + " statistics")
                    .setColor(0xdbb381)
                    .addField("Memory", memory, true)
                    .addField("Guilds",  String.valueOf(guilds), true)
                    .addField("Channels", String.valueOf(channels), true)
                    .addField("Users", String.valueOf(users), true)
                    .addField("Version", BaristaInfo.VERSION, true)
                    .addField("JDA", BaristaInfo.JDA_VERSION, true)
                    .setFooter(bot.getName(), bot.getAvatarUrl())
                    .setTimestamp(Instant.now());

            event.getChannel().sendMessage(eb.build()).queue();
        } else {
            event.getChannel()
                    .sendMessage("**" + bot.getName() + " statistics**" + "\n\n" + "**Memory:** " + memory +
                            "\n" + "**Guilds:** " + guilds + "\n" + "**Channels:** " + channels + "\n" +
                            "**Users:** " + users + "\n" + "**Version:** " + BaristaInfo.VERSION + "\n" + "**JDA:** " +
                            BaristaInfo.JDA_VERSION)
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public CommandType getType() {
        return CommandType.CORE;
    }
}
