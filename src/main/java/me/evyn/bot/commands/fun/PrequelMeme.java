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

package me.evyn.bot.commands.fun;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.RESTCollector;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PrequelMeme implements Command {

    private List<String> memes;

    /**
     * Constructor calls the RestCollector method and pulls a list of the top images in r/prequelmemes
     */
    public PrequelMeme() {
        this.memes = RESTCollector.getPrequelMemes();
    }

    /**
     * Pulls a random image from the List<String> instance variable and posts in channel
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {
        Random r = new Random();
        int num = r.nextInt(this.memes.size());

        event.getTextChannel()
                .sendMessage(this.memes.get(num))
                .queue();
    }

    @Override
    public String getName() {
        return "prequelmeme";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public String getDescription() {
        return "Provides a prequel meme taken from r/prequelmemes";
    }

    @Override
    public String getUsage() {
        return "prequelmeme";
    }

    @Override
    public CommandType getType() {
        return CommandType.FUN;
    }
}
