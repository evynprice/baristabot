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
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ServerInfo implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User bot = event.getJDA().getSelfUser();
        Guild guild = event.getGuild();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        LocalDateTime registerTimeCreated = guild.getTimeCreated().toLocalDateTime();

        String createDate = formatter.format(registerTimeCreated);

        EmbedBuilder eb = EmbedCreator.newInfoEmbedMessage(bot);
        eb.setTitle("Server Info: " + guild.getName())
                .setThumbnail(guild.getIconUrl())
                .setDescription(guild.getDescription())
                .addField("Id", guild.getId(), true)
                .addField("Owner", guild.getOwner().getUser().getAsTag(), true)
                .addBlankField(true)
                .addField("Region", guild.getRegion().getName(), true)
                .addField("Created", createDate, true)
                .addField("Boost Tier", guild.getBoostTier().name(), true)
                .addField("Members", "" + guild.getMembers().size(), true)
                .addField("Channels", "" + guild.getChannels().size(), true)
                .addField("Emojis", "" + guild.getEmotes().size(), true);

        event.getChannel()
                .sendMessage(eb.build())
                .queue();
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
