package me.evyn.barista.core.commands;

import me.evyn.barista.core.utils.BaristaInfo;
import me.evyn.barista.core.utils.Command;
import me.evyn.barista.core.utils.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;

public class Invite implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embeds, String[] args) {
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

        // send generated invite
        if (embeds) {
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle(bot.getName() + " Invite")
                    .setColor(0xdbb381)
                    .setDescription("Invite the bot to your own server using [this link](" + inviteUrl + ")")
                    .setFooter(bot.getName(), bot.getAvatarUrl())
                    .setTimestamp(Instant.now());

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        } else {
            event.getChannel()
                    .sendMessage("Invite the bot to your own server using this link: \n<" + inviteUrl + ">")
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public CommandType getType() {
        return CommandType.CORE;
    }
}
