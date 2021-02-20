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

package me.evyn.bot.listeners;

import me.evyn.bot.commands.Settings.ActivityLogs;
import me.evyn.bot.util.DataSourceCollector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        long guildId = event.getGuild().getIdLong();

        TextChannel actLogChannel = ActivityLogs.getActivityChannel(event, guildId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        LocalDateTime registerTimeCreated = event.getUser().getTimeCreated().toLocalDateTime();
        String registerDate = formatter.format(registerTimeCreated);


        if (actLogChannel != null) {
            boolean embed = DataSourceCollector.getEmbed(guildId);
            if (embed) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(event.getUser().getAsTag() + " (" + event.getUser().getId() + ")",
                        event.getUser().getAvatarUrl(), event.getUser().getAvatarUrl())
                        .setDescription("Account Created on: " + registerDate)
                        .setColor(0x00FF00)
                        .setFooter("User Joined.")
                        .setTimestamp(Instant.now());

                actLogChannel.sendMessage(eb.build()).queue();
            } else {
                actLogChannel.sendMessage("User " + event.getUser().getAsTag() + "(" +
                        event.getUser().getId() + ") joined.").queue();
            }
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {

        long guildId = event.getGuild().getIdLong();

        TextChannel actLogChannel = ActivityLogs.getActivityChannel(event, guildId);

        if (actLogChannel != null) {
            boolean embed = DataSourceCollector.getEmbed(guildId);
            if (embed) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(event.getUser().getAsTag() + " (" + event.getUser().getId() + ")",
                        event.getUser().getAvatarUrl(), event.getUser().getAvatarUrl())
                        .setColor(0xffa500)
                        .setFooter("User Left.")
                        .setTimestamp(Instant.now());

                actLogChannel.sendMessage(eb.build()).queue();
            } else {
                actLogChannel.sendMessage("User " + event.getUser().getAsTag() + "(" +
                        event.getUser().getId() + ") left.").queue();
            }
        }
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        long guildId = event.getGuild().getIdLong();

        TextChannel actLogChannel = ActivityLogs.getActivityChannel(event, guildId);

        if (actLogChannel != null) {
            StringBuilder sb = new StringBuilder();
            event.getRoles().stream()
                    .forEach(role -> sb.append(role.getName()).append(", "));

            sb.delete(sb.length() - 2, sb.length());

            boolean embed = DataSourceCollector.getEmbed(guildId);
            if (embed) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(event.getUser().getAsTag() + " (" + event.getUser().getId() + ")",
                        event.getUser().getAvatarUrl(), event.getUser().getAvatarUrl())
                        .setDescription("Added to new role: **" + sb.toString() + "**")
                        .setColor(0x005AFF)
                        .setFooter("Role update.")
                        .setTimestamp(Instant.now());

                actLogChannel.sendMessage(eb.build()).queue();
            } else {
                actLogChannel.sendMessage("User " + event.getUser().getAsTag() + "(" +
                        event.getUser().getId() + ") Added to new role: **" + sb.toString() + "**").queue();
            }
        }
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        long guildId = event.getGuild().getIdLong();

        TextChannel actLogChannel = ActivityLogs.getActivityChannel(event, guildId);

        if (actLogChannel != null) {
            StringBuilder sb = new StringBuilder();
            event.getRoles().stream()
                    .forEach(role -> sb.append(role.getName()).append(", "));

            sb.delete(sb.length() - 2, sb.length());

            boolean embed = DataSourceCollector.getEmbed(guildId);
            if (embed) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(event.getUser().getAsTag() + " (" + event.getUser().getId() + ")",
                        event.getUser().getAvatarUrl(), event.getUser().getAvatarUrl())
                        .setDescription("Removed from role: **" + sb.toString() + "**")
                        .setColor(0xFF005A)
                        .setFooter("Role update.")
                        .setTimestamp(Instant.now());

                actLogChannel.sendMessage(eb.build()).queue();
            } else {
                actLogChannel.sendMessage("User " + event.getUser().getAsTag() + "(" +
                        event.getUser().getId() + ") Removed from role: **" + sb.toString() + "**").queue();
            }
        }
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        long guildId = event.getGuild().getIdLong();

        TextChannel actLogChannel = ActivityLogs.getActivityChannel(event, guildId);

        if (actLogChannel != null) {
            String oldNickname = event.getOldNickname();
            if (oldNickname == null) {
                oldNickname = event.getUser().getName();
            }
            String newNickname = event.getNewNickname();
            if (newNickname == null) {
                newNickname = event.getUser().getName();
            }

            boolean embed = DataSourceCollector.getEmbed(guildId);
            if (embed) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(event.getUser().getAsTag() + " (" + event.getUser().getId() + ")",
                        event.getUser().getAvatarUrl(), event.getUser().getAvatarUrl())
                        .setDescription("Updated their nickname from: **" + oldNickname + "**" + "\n"
                        + "to: **" + newNickname + "**")
                        .setColor(0xFFFF00)
                        .setFooter("Nickname update.")
                        .setTimestamp(Instant.now());

                actLogChannel.sendMessage(eb.build()).queue();
            } else {
                actLogChannel.sendMessage("User " + event.getUser().getAsTag() + "(" +
                        event.getUser().getId() + ") changed their nickname from: **" + oldNickname + "** to: **" +
                        newNickname + "**").queue();
            }
        }
    }
}
