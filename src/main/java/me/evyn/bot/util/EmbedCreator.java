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
