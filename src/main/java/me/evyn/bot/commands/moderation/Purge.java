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

package me.evyn.bot.commands.moderation;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Purge implements Command {

    /**
     * If command is ran in guild and bot + user have required permissions, delete selected amount of messages in
     * current or mentioned channel.
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User bot = event.getJDA().getSelfUser();

        // if command is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "This command can only be ran in servers.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        EmbedBuilder eb;

        // bot lacks manage messages permission
        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            eb = EmbedCreator.newErrorEmbedMessage(bot, "Bot is missing the required permission: " +
                    "`Manage Messages`.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // user lacks manage messages permission
        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            eb = EmbedCreator.newErrorEmbedMessage(bot, "You are missing the required permission: " +
                    "`Manage Messages`.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // no arguments were given
        if (args.length == 0) {
            eb = EmbedCreator.newErrorEmbedMessage(bot, "Invalid command usage. Please run `" +
                    prefix + "usage purge` for more information.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // check if arguments matches 0-100
        if (args[0].matches("(0*(?:[1-9][0-9]?|100))")) {

            // add one to amount to also delete command message
            int amount = Integer.valueOf(args[0]) + 1;

            if (args.length == 1) {

                // fetch message history
                MessageHistory history = event.getChannel().getHistory();
                List<Message> messages = history.retrievePast(amount).complete();

                // purge messages
                event.getChannel().purgeMessages(messages);

                eb = EmbedCreator.newCommandEmbedMessage(bot);
                eb.setTitle("Purge")
                        .setDescription("Successfully purged " + args[0] + " messages.");

                // send purge success message
                Message m = event.getChannel().sendMessage(eb.build()).complete();

                // delete purge success message
                m.delete().completeAfter(3, TimeUnit.SECONDS);

            } else if (args.length > 1) {

                // get channel Id from arguments
                String id = args[1].replaceAll("[^0-9]", "");

                // attempt to find channel
                TextChannel channel = null;
                try {
                    channel = event.getGuild().getTextChannelById(id);
                } catch (NumberFormatException e ) {
                    eb = EmbedCreator.newErrorEmbedMessage(bot, "Invalid channel provided.");

                    event.getChannel()
                            .sendMessage(eb.build())
                            .queue();
                    return;
                }

                // get message history in channel
                if (channel != null) {
                    MessageHistory history = channel.getHistory();
                    List<Message> messages = history.retrievePast(amount).complete();

                    event.getChannel().purgeMessages(messages);

                    eb = EmbedCreator.newCommandEmbedMessage(bot);
                    eb.setTitle("Purge")
                            .setDescription("Successfully purged " + args[0] + " messages.");

                    // send purge success message
                    Message m = event.getChannel().sendMessage(eb.build()).complete();

                    // delete purge success message
                    m.delete().completeAfter(3, TimeUnit.SECONDS);
                }
            }
        } else {
            // invalid number of messages selected
            eb = EmbedCreator.newErrorEmbedMessage(bot, "Invalid number of messages. Maximum message " +
                    "count is 100.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("prune", "delete", "remove");
    }

    @Override
    public String getDescription() {
        return "Removes up to 100 of the most recent messages in this channel or mentioned channel";
    }

    @Override
    public String getUsage() {
        return "purge (0-100) (channel name)";
    }

    @Override
    public CommandType getType() {
        return CommandType.MODERATION;
    }
}
