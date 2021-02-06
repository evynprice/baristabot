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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.List;

public interface Command {

    /**
     * Runs the command given from the Command Handler
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    void run(MessageReceivedEvent event, String prefix, List<String> args);

    /**
     * Gives the command name
     * @return String command name
     */
    String getName();

    /**
     * Gives the command aliases as a List<String> object
     * @return List<String> command aliases
     */
    List<String> getAliases();

    /**
     * Gives a description of what the command does
     * @return String command description
     */
    String getDescription();

    /**
     * Gives an example usage. () should be used for optional arguments, [] for required arguments
     * @return String command usage
     */
    String getUsage();

    /**
     * Returns the enum value of the command type
     * @return CommandType command type
     */
    CommandType getType();
}
