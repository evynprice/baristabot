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

package me.evyn.bot.commands.Settings;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.resources.Config;
import me.evyn.bot.util.DataSourceCollector;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Settings implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {
        User bot = event.getJDA().getSelfUser();

        // If message is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "This command can only be ran in servers.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // If user does not have manage server permissions, send error and return
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "You do not have the required " +
                    "permissions to run this command. Missing permission `manage server`");
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // if no setting is listed, send usage error and return
        if (args.length == 0) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "Invalid arguments. " +
                    "Please run `" + prefix + "usage settings` for more information.");
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // prefix settings
        if (args[0].equals("prefix")) {
            long guildId = event.getGuild().getIdLong();

            // no arguments are present
            if (args.length == 1) {
               event.getChannel()
                       .sendMessage("Current prefix is: `"  + prefix + "`")
                       .queue();
            } else {
                // prefix reset
                boolean status;

                if (args[1].equals("reset")) {
                    status = DataSourceCollector.setPrefix(guildId, Config.prefix);
                } else {
                    // prefix [prefix]
                    status = DataSourceCollector.setPrefix(guildId, args[1]);
                }
                if (status) {
                    event.getChannel()
                            .sendMessage("Prefix updated successfully!")
                            .queue();
                } else {
                    event.getChannel()
                            .sendMessage("There was an error updating the prefix. Please try again later.")
                            .queue();
                }
            }
        }
    }

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("setting", "config", "conf");
    }

    @Override
    public String getDescription() {
        return "Changes per-guild settings";
    }

    @Override
    public String getUsage() {
        return "setting [setting] (value)";
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMIN;
    }
}
