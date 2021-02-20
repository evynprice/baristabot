package me.evyn.bot.listeners;

import me.evyn.bot.commands.Settings.ActivityLogs;
import me.evyn.bot.util.DataSourceCollector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;

public class ActivityListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        long guildId = event.getGuild().getIdLong();

        TextChannel actLogChannel = ActivityLogs.getActivityChannel(event, guildId);

        if (actLogChannel != null) {
            boolean embed = DataSourceCollector.getEmbed(guildId);
            if (embed) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(event.getUser().getAsTag() + " (" + event.getUser().getId() + ")",
                        event.getUser().getAvatarUrl(), event.getUser().getAvatarUrl())
                        .setColor(0x00FF00)
                        .setFooter("User Joined.")
                        .setTimestamp(Instant.now());

                actLogChannel.sendMessage(eb.build()).queue();
            } else {
                actLogChannel.sendMessage("User " + event.getUser().getAsTag() + "(" +
                        event.getUser().getId() + ") joined.");
            }
        }
    }
}
