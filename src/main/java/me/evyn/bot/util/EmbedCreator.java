package me.evyn.bot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

public class EmbedCreator {

    /**
     * Generates a new EmbedBuilder object with an error message template. Description of error is provided by the
     * String argument
     * @param bot Discord bot user
     * @param description String Description of error message
     * @return EmbedBuilder error embed
     */
    public static EmbedBuilder newErrorEmbedMessage(User bot, String description) {

        EmbedBuilder eb = new EmbedBuilder();

        eb.setColor(0xd55745)
                .setTitle("Error")
                .setTimestamp(Instant.now())
                .setFooter(bot.getName(), bot.getAvatarUrl());

        if (description.isEmpty()) {
            eb.setDescription("There has been an error (no further explanation)");
        } else {
            eb.setDescription(description);
        }
        return eb;
    }

    /**
     * Generates a new EmbedBuilder object with info message template.
     * @param bot Discord bot user
     * @return EmbedBuilder info embed
     */
    public static EmbedBuilder newInfoEmbedMessage(User bot) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setColor(0xdbb381)
                .setTitle(bot.getName())
                .setTimestamp(Instant.now())
                .setFooter(bot.getName(), bot.getAvatarUrl());

        return eb;
    }

    /**
     * Generates a new EmbedBuilder object with command message template
     * @param bot Discord bot user
     * @return EmbedBuilder command embed
     */
    public static EmbedBuilder newCommandEmbedMessage(User bot) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setColor(0xe6a040)
                .setTimestamp(Instant.now())
                .setFooter(bot.getName(), bot.getAvatarUrl());

        return eb;
    }
}
