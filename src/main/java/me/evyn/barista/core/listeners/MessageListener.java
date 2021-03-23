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
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String messageContent = message.getContentRaw();
        String prefix;
        boolean embeds;

        if (event.isFromGuild()) {
            prefix = DatasourceCollector.getGuildPrefix(event.getGuild().getId());
            if (DatasourceCollector.getGuildEmbeds(event.getGuild().getId()) &&
                    event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                embeds = true;
            } else {
                embeds = false;
            }
        } else {
            prefix = Config.DEFAULT_PREFIX;
            embeds = Config.DEFAULT_EMBEDS;
        }

        if(messageContent.startsWith("<@!" + event.getJDA().getSelfUser().getId())) {
            event.getChannel()
                    .sendMessage("The current prefix is `" + prefix + "`").queue();
            return;
        }

        if (!messageContent.startsWith(prefix)) return;

        String[] msg = messageContent.replaceFirst(prefix, "")
                .trim()
                .split(" ");

        String cmd = msg[0];

        String[] args;

        if (msg.length > 1) {
            args = Arrays.copyOfRange(msg, 1, msg.length);
        } else {
            args = null;
        }

        CommandHandler.run(event, prefix, embeds, cmd, args);
    }
}
