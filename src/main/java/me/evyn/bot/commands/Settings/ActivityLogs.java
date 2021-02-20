package me.evyn.bot.commands.Settings;

import me.evyn.bot.util.DataSourceCollector;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ActivityLogs implements Setting {

    @Override
    public void edit(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {
        User bot = event.getJDA().getSelfUser();
        long guildId = event.getGuild().getIdLong();

        String usageErr = "Invalid command usage. Please run `" + prefix + "settings help activity-logs` for more " +
                "information.";

        // too many arguments
        if(args.length > 2) {
            if (embed) {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, usageErr);

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                event.getChannel()
                        .sendMessage("ERROR: " + usageErr)
                        .queue();
            }

            return;
        }

        List<String> negative = Arrays.asList("false", "disabled", "no");

        boolean result = false;
        TextChannel channel = null;

        if (negative.contains(args[1])) {
            result = DataSourceCollector.setActivityLog(guildId, 000000000000000000);

        } else {
            String channelId = args[1].replaceAll("[^0-9]", "");

            try {
                channel = event.getGuild().getTextChannelById(channelId);
            } catch (NumberFormatException e) {

                String msg = "The provided channel is invalid. Please choose a valid channel.";
                if (embed) {
                    EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
                    event.getChannel()
                            .sendMessage(eb.build())
                            .queue();
                } else {
                    event.getChannel()
                            .sendMessage("ERROR: " + msg)
                            .queue();
                }
                return;
            }

            result = DataSourceCollector.setActivityLog(guildId, channel.getIdLong());
        }

        EmbedBuilder eb = null;
        String message = "";

        if (result) {
            String msg = "";
            if (channel != null) {
                msg = "Success! The new mod-log channel will be " + channel.getAsMention();
            } else {
                msg = "Success! The mod-log setting has been disabled.";
            }
            if (embed) {
                eb = EmbedCreator.newCommandEmbedMessage(bot);
                eb.setColor(0x00CC00)
                        .setDescription(msg);
            } else {
                message = msg;
            }

        } else {
            String msg = "There was an error updating the setting. Please try again later.";
            if (embed) {
                eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
            } else {
                message = msg;
            }
        }

        if (embed) {
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            event.getChannel()
                    .sendMessage(message)
                    .queue();
        }
    }

    @Override
    public void view(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {
        User bot = event.getJDA().getSelfUser();

        GuildChannel channel = ActivityLogs.getActivityChannel(event, event.getGuild().getIdLong());

        if (channel == null) {
            String msg = "activity-logs are not currently set up or the current channel is invalid. " +
                    "For more information, run " + prefix + "`settings help activity-logs`";
            if (embed) {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, msg);
                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            } else {
                event.getChannel()
                        .sendMessage("ERROR: " + msg)
                        .queue();
            }
            return;
        }

        event.getChannel()
                .sendMessage("The current activity-log channel is <#" + channel.getId() + "> (" +
                        channel.getId() + ")")
                .queue();
    }

    public static TextChannel getActivityChannel(GenericGuildEvent event, long guildId) {
        String channelId = DataSourceCollector.getActivityLog(guildId);

        if (channelId == null) {
            return null;
        } else {
            try {
                return event.getGuild().getTextChannelById(channelId);
            } catch (NumberFormatException e ) {
                return null;
            }
        }
    }

    public static TextChannel getActivityChannel(GenericMessageEvent event, long guildId) {
        String channelId = DataSourceCollector.getActivityLog(guildId);

        if (channelId == null) {
            return null;
        } else {
            try {
                return event.getGuild().getTextChannelById(channelId);
            } catch (NumberFormatException e ) {
                return null;
            }
        }
    }

    @Override
    public String getName() {
        return "activity-logs";
    }

    @Override
    public String getDescription() {
        return "Sends logs of guild activity to specified channel";
    }

    @Override
    public String getUsage() {
        return "settings activity-logs (disabled/channel-id/ChannelMention)";
    }

    @Override
    public String getExample() {
        return "settings activity-logs #activity-logs";
    }
}
