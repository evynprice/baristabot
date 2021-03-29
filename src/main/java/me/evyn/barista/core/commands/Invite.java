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

package me.evyn.barista.core.commands;

import me.evyn.barista.core.utils.Command;
import me.evyn.barista.core.utils.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;

public class Invite implements Command {

    /**
     * Generates and sends bot invite link in event's channel
     * @param event Message Event
     * @param prefix Bot prefix
     * @param embedsEnabled embedsEnabled
     * @param args command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embedsEnabled, String[] args) {
        // fetch bot user
        User bot = event.getJDA().getSelfUser();

        // generate invite URL with required permissions
        String inviteUrl = event.getJDA().getInviteUrl(
                Permission.MANAGE_ROLES,
                Permission.MANAGE_CHANNEL,
                Permission.KICK_MEMBERS,
                Permission.BAN_MEMBERS,
                Permission.NICKNAME_MANAGE,
                Permission.NICKNAME_CHANGE,
                Permission.MANAGE_EMOTES,
                Permission.VIEW_AUDIT_LOGS,
                Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_MANAGE,
                Permission.MESSAGE_EMBED_LINKS,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_MENTION_EVERYONE,
                Permission.MESSAGE_ADD_REACTION,
                Permission.VOICE_CONNECT,
                Permission.VOICE_SPEAK,
                Permission.VOICE_DEAF_OTHERS,
                Permission.VOICE_MOVE_OTHERS
        );

        if (embedsEnabled) {
            // generate invite embed
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(bot.getName() + " Invite")
                    .setColor(0xdbb381)
                    .setDescription("Invite the bot to your own server using [this link](" + inviteUrl + ")")
                    .setFooter(bot.getName(), bot.getAvatarUrl())
                    .setTimestamp(Instant.now());

            // send invite embed
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            // generate and send plaintext invite
            event.getChannel()
                    .sendMessage("Invite the bot to your own server using this link: \n<" + inviteUrl + ">")
                    .queue();
        }
    }

    /**
     * Provides name of command
     * @return command name
     */
    @Override
    public String getName() {
        return "invite";
    }

    /**
     * Provides type of command
     * @return command type
     */
    @Override
    public CommandType getType() {
        return CommandType.CORE;
    }
}
