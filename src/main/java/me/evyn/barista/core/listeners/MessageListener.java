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

package me.evyn.barista.core.listeners;

import me.evyn.barista.core.utils.Config;
import me.evyn.barista.core.utils.CommandHandler;
import me.evyn.barista.core.utils.DatasourceCollector;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class MessageListener extends ListenerAdapter {

    /**
     * Event triggers when bot receives a message, parses data and hands off to CommandHandler
     * @param event Message Event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // return if author is a bot
        if (event.getAuthor().isBot()) return;

        // fetch message object and message content
        Message message = event.getMessage();
        String messageContent = message.getContentRaw();

        // declare prefix and embeds variables that will be provided from DatasourceCollector
        String prefix;
        boolean embedsEnabled;

        // fetch guild / channel prefix and if embeds are enabled
        if (event.isFromGuild()) {
            prefix = DatasourceCollector.getGuildPrefix(event.getGuild().getId());

            // if embeds setting is enabled and bot has permission to send embed messages
            if (DatasourceCollector.getGuildEmbedsEnabled(event.getGuild().getId()) &&
                    event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                embedsEnabled = true;
            } else {
                embedsEnabled = false;
            }
        } else {
            // event was not from guild, set prefix and embeds to default values
            prefix = Config.DEFAULT_PREFIX;
            embedsEnabled = Config.DEFAULT_EMBEDS_ENABLED;
        }

        // if message starts with bot mention send current bot prefix
        if(messageContent.startsWith("<@!" + event.getJDA().getSelfUser().getId())) {
            event.getChannel()
                    .sendMessage("The current prefix is `" + prefix + "`").queue();
            return;
        }

        // return if message does not start with prefix
        if (!messageContent.startsWith(prefix)) return;

        // remove prefix from message content and split into array by space
        String[] msg = messageContent.replaceFirst(prefix, "")
                .trim()
                .split(" ");

        // set the command to be the first element in the msg array
        String cmd = msg[0];

        // declare the arguments array
        String[] args;

        // if message contains arguments, include them in the args array. Otherwise set the args array to null
        if (msg.length > 1) {
            args = Arrays.copyOfRange(msg, 1, msg.length);
        } else {
            args = null;
        }

        // pass information to command handler
        CommandHandler.run(event, prefix, embedsEnabled, cmd, args);
    }
}
