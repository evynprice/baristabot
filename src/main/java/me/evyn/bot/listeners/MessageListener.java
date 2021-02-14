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
import me.evyn.bot.resources.Config;
import me.evyn.bot.util.DataSourceCollector;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
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

        Message message = event.getMessage();
        String content = message.getContentRaw();

        // get prefix
        String prefix;

        if (event.isFromType(ChannelType.TEXT)) {
            prefix = DataSourceCollector.getPrefix(event.getGuild().getIdLong());
        } else {
            prefix = Config.prefix;
        }

        // If message starts with bot mention, direct user to send using prefix and return
        if (content.startsWith("<@!" + event.getJDA().getSelfUser().getId())) {
            event.getChannel()
                    .sendMessage(String.format("My prefix is currently `%s`" + "%n" + "" +
                            "Try running `%shelp` for more information",prefix, prefix))
                    .queue();
            return;
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
            args = new String[]{};
        }

        // Take all of the information that was gathered and pass it to the CommandHandler
        ch.run(event, prefix, cmd, args);
    }
}
