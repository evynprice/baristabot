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

import me.evyn.bot.commands.CommandHandler;
import me.evyn.bot.commands.fun.Counting;
import me.evyn.bot.resources.Config;
import me.evyn.bot.util.DataSourceCollector;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class MessageListener extends ListenerAdapter {

    private CommandHandler ch = new CommandHandler();

    /**
     * Runs when the bot receives a message. Does initial parsing and sends cleaned command and arguments to the
     * command handler.
     * @param event Discord API message event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // return if sender is bot
        if (event.getAuthor().isBot()) return;

        // gather message content
        Message message = event.getMessage();
        String content = message.getContentRaw();

        // initialize prefix and counting variables
        String prefix;
        String counting = null;

        /* set local prefix variable to default prefix if message is not sent in a guild channel, otherwise set local
        prefix variable to be database prefix
         */
        if (event.isFromType(ChannelType.TEXT)) {
            long guildId = event.getGuild().getIdLong();
            prefix = DataSourceCollector.getGuildPrefix(guildId);
            counting = DataSourceCollector.getCountingChannel(guildId);
        } else {
            prefix = Config.prefix;
        }

        // If message starts with bot mention, direct user to send using prefix and return
        if (content.startsWith("<@!" + event.getJDA().getSelfUser().getId())) {
            event.getChannel()
                    .sendMessage(String.format("My prefix is currently `%s`%n" +
                            "Try running `%shelp` for more information",prefix, prefix))
                    .queue();
            return;
        }

        // if content starts with number and counting game is enabled
        if (counting != null) {
            long guildId = event.getGuild().getIdLong();
            // fetch counting channel
            TextChannel countingChannel = Counting.getCountingChannel(event, guildId);

            // counting channel exits and message contains number
            if (countingChannel != null && content.matches(".*[0-9]+.*")) {
                // if current channel is counting channel
                if (event.getChannel().getIdLong() == countingChannel.getIdLong()) {

                    // replace all non-numbers and fetch integer value
                    String msg = content.replaceAll("[^0-9]", "");

                    int msgCount;
                    try {
                        msgCount = Integer.valueOf(msg);
                    } catch (NumberFormatException e) {
                        /* number was most likely a channel mention or other large number so instead of sending
                        error message we will just return
                         */
                        return;
                    }

                    // get current count and the last user ID
                    int currentCount = DataSourceCollector.getCountingCurrentGuildScore(guildId);
                    String lastUserId = DataSourceCollector.getCountingGuildLastUserId(guildId);

                    // if provided count is the next value and member ID was not the most recent counter
                    if ((currentCount == 0) && msgCount == (currentCount + 1) ||
                            msgCount == currentCount + 1 && !event.getMember().getId().equals(lastUserId)) {

                        // set guild score to the new count
                        DataSourceCollector.setCountingCurrentGuildScore(guildId, msgCount);

                        // update the last user to be the most recent counter
                        DataSourceCollector.setCountingGuildLastUserId(guildId, event.getAuthor().getId());

                        // get and possibly set top score
                        int topScore = DataSourceCollector.getCountingGuildTopScore(guildId);

                        boolean newTopScore = false;

                        if (msgCount > topScore) {
                            DataSourceCollector.setCountingGuildTopScore(guildId, msgCount);
                            newTopScore = true;
                        }

                        // fetch the user's total count
                        int usrTotalCount = DataSourceCollector.getCountingUserTotalCorrect(guildId,
                                event.getMember().getIdLong());

                        // update the user's total count
                        DataSourceCollector.setCountingUserTotalCorrect(guildId, event.getMember().getIdLong(),
                                (usrTotalCount+1));

                        // count update was successful so react with checkmark
                        event.getMessage().addReaction("\u2705").queue();

                        // send extra reaction if new guild top score
                        if (newTopScore) {
                            event.getMessage().addReaction("\uD83C\uDF89").queue();
                        }
                    } else {
                        // count was invalid so send error reaction
                        DataSourceCollector.setCountingCurrentGuildScore(event.getGuild().getIdLong(), 0);
                        int totalIncorrect = DataSourceCollector.getCountingUserTotalIncorrect(guildId,
                                event.getMember().getIdLong());
                        DataSourceCollector.setCountingUserTotalIncorrect(guildId, event.getMember().getIdLong(),
                                (totalIncorrect+1));
                        event.getMessage().addReaction("\u274C").queue();
                        event.getChannel()
                                .sendMessage("\u274C Score lost! The highest number was: " + currentCount)
                                .queue();
                    }
                    return;
                }
            }

        }


        // If message does not start with prefix, return
        if (!content.startsWith(prefix)) return;

        // Remove prefix from message, trim whitespace and split by space
        String[] msg = content.replace(prefix, "")
                .trim()
                .split(" ");

        // First cleaned message argument is the command
        String cmd = msg[0];

        // Add arguments if there were any
        String[] args;

        if (msg.length > 1) {
            args = Arrays.copyOfRange(msg, 1, msg.length);
        } else {
            // args is empty array
            args = new String[]{};
        }

        // Take all of the information that was gathered and pass it to the CommandHandler
        ch.run(event, prefix, cmd, args);
    }
}
