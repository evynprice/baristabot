package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.BaristaInfo;
import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * Provides statistics on bot application and user including memory used, guild information, and versions
 */
public class Statistics implements Command {

    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final CommandType type;

    public Statistics() {
        this.name = "statistics";
        this.aliases = Arrays.asList("stats", "info");
        this.description = "Provides bot statistics and information";
        this.usage = "statistics";
        this.type = CommandType.INFORMATION;
    }

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        JDA bot = event.getJDA();

        // get memory in bytes and convert to MB
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long usedMemory = totalMemory - Runtime.getRuntime().freeMemory() / (1024 * 1024);

        // format memory as String
        String memory = String.format("%dMB / %dMB", usedMemory, totalMemory);

        // get guild information
        int servers = bot.getGuilds().size();
        int channels = bot.getTextChannels().size();
        int users = bot.getUsers().size();

        // get versions
        String botVersion = BaristaInfo.BOT_VERSION;
        String jdaVersion = BaristaInfo.JDA_VERSION;

        // format information as embed
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0x386895)
                .setTitle("BaristaBot statistics")
                .addField("Memory", memory, true)
                .addField("Servers", String.valueOf(servers), true)
                .addField("Channels", String.valueOf(channels), true)
                .addField("Users", String.valueOf(users), true)
                .addField("Version", botVersion, true)
                .addField("JDA", jdaVersion, true)
                .setTimestamp(Instant.now())
                .setFooter("Barista Bot", "https://i.imgur.com/WtJZ3Wk.png");

        // send embed message
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
