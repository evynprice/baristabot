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

package me.evyn.bot.commands;

import me.evyn.bot.commands.fun.Say;
import me.evyn.bot.commands.info.*;
import me.evyn.bot.commands.moderation.Ban;
import me.evyn.bot.commands.moderation.Kick;
import me.evyn.bot.commands.moderation.Purge;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandHandler {

    public static final List<Command> commands;

    static {
        commands = new ArrayList<>();
        CommandHandler.addCommand(new Help());
        CommandHandler.addCommand(new Commands());
        CommandHandler.addCommand(new Usage());
        CommandHandler.addCommand(new Stats());
        CommandHandler.addCommand(new UserInfo());
        CommandHandler.addCommand(new ServerInfo());
        CommandHandler.addCommand(new Invite());
        CommandHandler.addCommand(new Support());
        CommandHandler.addCommand(new Ping());
        CommandHandler.addCommand(new Say());
        CommandHandler.addCommand(new Kick());
        CommandHandler.addCommand(new Ban());
        CommandHandler.addCommand(new Purge());
    }

    private static void addCommand(Command cmd) {
        if(!commands.contains(cmd)) {
            commands.add(cmd);
        }
    }

    public static Command findCommand(String cmdName) {


        Optional<Command> matching = commands.stream()
                .filter(command -> command.getName().equals(cmdName) || command.getAliases().contains(cmdName))
                .findFirst();

        return matching.orElse(null);
    }

    public void run(MessageReceivedEvent event, String prefix, String cmd, String[] args) {

        Command command = CommandHandler.findCommand(cmd);

        if (command == null) {
            event.getChannel()
                    .sendMessage("Error: That command was not found.")
                    .queue();
        } else {
            command.run(event, prefix, args);
        }
    }
}
