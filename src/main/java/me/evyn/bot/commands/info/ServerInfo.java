/*
 * MIT License
 *
 * Copyright (c) 2021 Evyn Price
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.evyn.bot.commands.info;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.DataSourceCollector;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ServerInfo implements Command {

    /**
     * If ran in guild, provides information on it
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {

        User bot = event.getJDA().getSelfUser();

        // if command is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "This command can only be ran in servers.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        Guild guild = event.getGuild();

        // create date and time for guild creation
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        LocalDateTime registerTimeCreated = guild.getTimeCreated().toLocalDateTime();

        String creationDate = formatter.format(registerTimeCreated);

        if (embed) {
            EmbedBuilder eb = EmbedCreator.newInfoEmbedMessage(bot);
            eb.setTitle("Server Info: " + guild.getName())
                    .setThumbnail(guild.getIconUrl())
                    .setDescription(guild.getDescription())
                    .addField("ID", guild.getId(), true)
                    .addField("Owner", guild.getOwner().getUser().getAsTag(), true)
                    .addBlankField(true)
                    .addField("Region", guild.getRegion().getName(), true)
                    .addField("Created", creationDate, true)
                    .addField("Boost Tier", guild.getBoostTier().name(), true)
                    .addField("Members", "" + guild.getMembers().size(), true)
                    .addField("Channels", "" + guild.getChannels().size(), true)
                    .addField("Emojis", "" + guild.getEmotes().size(), true);

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            event.getChannel()
                    .sendMessage("**Server Info:** " + guild.getName() + "\n\n" + "**ID:** " + guild.getId() +
                            "\n" + "**Owner:** " + guild.getOwner().getUser().getAsTag() + "\n" + "**Region:** " +
                            guild.getRegion().getName() + "\n" + "**Created:** " + creationDate + "\n" +
                            "**Boost Tier:** " + guild.getBoostTier().name() + "\n" + "**Members:** " +
                            guild.getMembers().size() + "\n" + "**Channels:** " + guild.getChannels().size() +
                            "\n" + "**Emojis:** " + guild.getEmotes().size())
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "serverinfo";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("server", "guild", "guildinfo");
    }

    @Override
    public String getDescription() {
        return "Provides information about the current server";
    }

    @Override
    public String getUsage() {
        return "serverinfo";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFO;
    }
}
