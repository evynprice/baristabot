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

package me.evyn.barista.core.utils;

import me.evyn.barista.core.commands.Help;
import me.evyn.barista.core.commands.Invite;
import me.evyn.barista.core.commands.Ping;
import me.evyn.barista.core.commands.Stats;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandHandler {

    public static final List<Command> COMMANDS = new ArrayList<>();

    static {
        CommandHandler.addCommand(new Ping());
        CommandHandler.addCommand(new Help());
        CommandHandler.addCommand(new Stats());
        CommandHandler.addCommand(new Invite());
    }

    private static void addCommand(Command cmd) {
        if (!COMMANDS.contains(cmd)) {
            COMMANDS.add(cmd);
        }
    }

    public static Command findCommand(String cmdName) {
        Optional<Command> matching = COMMANDS.stream()
                .filter(cmd -> cmd.getName().equals(cmdName))
                .findFirst();

        return matching.orElse(null);
    }

    public static void run(MessageReceivedEvent event, String prefix, boolean embedsEnabled, String cmd, String[] args) {
        Command command = CommandHandler.findCommand(cmd);

        if (command == null) {
            event.getChannel()
                    .sendMessage("**Error:** That command does not exist.")
                    .queue();
        } else {
            command.run(event, prefix, embedsEnabled, args);
        }
    }
}
