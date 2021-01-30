package me.evyn.baristabot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

/**
 * This Class includes methods to easily create common embed objects
 */
public class EasyEmbed {

    public MessageBuilder newErrorEmbedMessage(User bot, String description) {

        EmbedBuilder eb = new EmbedBuilder();

        eb.setColor(0xFF0000)
                .setTitle("Error")
                .setTimestamp(Instant.now())
                .setFooter(bot.getName(), bot.getAvatarUrl());

        if (description.isEmpty()) {
            eb.setDescription("There has been an error (no further explanation)");
        } else {
            eb.setDescription(description);
        }

        MessageBuilder message = new MessageBuilder();
        message.setEmbed(eb.build());

        return message;
    }
}
