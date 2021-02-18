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
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings implements Command {

    private static Map<String, Setting> settings;

    static {
        settings = new HashMap<>();
        Settings.add(new Prefix());
        Settings.add(new Embed());
    }

    private static void add(Setting setting) {
        settings.putIfAbsent(setting.getName(), setting);
    }

    public static Map<String, Setting> getSettings() {
        return settings;
    }

    @Override
    public void run(MessageReceivedEvent event, String prefix, boolean embed, String[] args) {
        User bot = event.getJDA().getSelfUser();

        // If message is not ran in guild, send error and return
        if (!event.isFromType(ChannelType.TEXT)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "This command can only be ran in servers.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // If user is missing permissions, send error and return
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "You do not have the required " +
                            " permissions to run this command. Missing permission `Manage  Server`.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // if command is [prefix] help and/or no arguments are present
        if (args.length == 0 || (args.length == 1 && args[0].equals("help"))) {
            EmbedBuilder eb = EmbedCreator.newCommandEmbedMessage(bot);

            StringBuilder sb = new StringBuilder();
            settings.keySet().stream()
                    .forEach(setting -> sb.append(setting).append(", "));

            sb.delete(sb.length() - 2, sb.length());

            eb.setTitle(event.getJDA().getSelfUser().getName() + " Settings")
                    .setDescription("Listed below are all of the customizable bot settings. To get more information " +
                            "on any command, run `" + prefix + "settings help [settingName]`")
                    .addField("Standard usage", prefix + "settings [setting] (new value)", false)
                    .addField("Settings", sb.toString(), false);

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // if setting is "help"
        if (args[0].equals("help")) {
            Setting setting = settings.getOrDefault(args[1], null);

            // setting exists
            if (setting != null) {
                EmbedBuilder eb = EmbedCreator.newCommandEmbedMessage(bot);
                eb.setTitle("Settings: " + setting.getName())
                        .setDescription("() Optional Argument" + "\n" + "[] Required Argument")
                        .addField("Description", setting.getDescription(), false)
                        .addField("Usage", prefix + setting.getUsage(), false)
                        .addField("Example", prefix + setting.getExample(), false);

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
                return;
                // setting does not exist
            } else {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "That setting does not exist.");

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
                return;
            }
        }

        Setting setting = settings.getOrDefault(args[0], null);

        // if the only arguments are the setting name, send the current value. Else, update the value
        if (setting != null) {
            if (args.length == 1) {
                setting.view(event, prefix, args);
            } else {
                setting.edit(event, prefix, args);
            }
            // setting does not exist
        } else {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(bot, "That setting does not exist try " +
                    "running `" + prefix + "settings help` for more information.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
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
        return "Changes per-guild settings. Run `settings help` for more information";
    }

    @Override
    public String getUsage() {
        return "settings [setting] (value)";
    }

    @Override
    public CommandType getType() {
        return CommandType.ADMIN;
    }
}
