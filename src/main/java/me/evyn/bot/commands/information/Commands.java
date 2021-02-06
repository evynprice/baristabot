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

package me.evyn.bot.commands.information;

import me.evyn.bot.commands.CommandWithCmds;
import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.EasyEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands implements CommandWithCmds {

    private List<Command> cmds;

    // These hold all of the command names sorted by type
    private List<String> funCommands = new ArrayList<>();
    private List<String> infoCommands = new ArrayList<>();

    // Formatted strings of command names
    private String fun;
    private String info;

    /**
     * This is used to pass the MessageListener commands list to the Aliases object. This has to be done after all of
     * the commands are initialized so that it includes the full list.
     * @param cmds bot commands
     */
    @Override
    public void addCommands(List<Command> cmds) {
        this.cmds = cmds;

        // Extra logic is here to sort commands by their type and add their names to the appropriate ArrayLists
        this.cmds.stream()
                .forEach(cmd -> {
                    if (cmd.getType().equals(CommandType.FUN)) {
                        this.funCommands.add(cmd.getName());
                    }
                    else if (cmd.getType().equals(CommandType.INFORMATION)) {
                        this.infoCommands.add(cmd.getName());
                    }
                });

        // Extra logic to format commands as string
        this.fun = this.commandFormatter(this.funCommands);
        this.info = this.commandFormatter(this.infoCommands);
    }

    /**
     * Sends a list of available bot commands
     * @param event Discord API message event
     * @param prefix Specific guild bot prefix
     * @param args Command arguments
     */
    @Override
    public void run(MessageReceivedEvent event, String prefix, List<String> args) {

        User bot = event.getJDA().getSelfUser();

        // Create the embed with the commands as fields
        EasyEmbed easyEmbed = new EasyEmbed();
        EmbedBuilder eb = easyEmbed.newCommandEmbedMessage(bot);

        eb.setTitle(bot.getName() + " commands")
                .setDescription(String.format("Use `%shelp <command>` for information on a specific command", prefix))
                .addField("Fun", this.fun,false)
                .addField("Information", this.info, false);

        // send embed
        event.getChannel()
                .sendMessage(eb.build())
                .queue();
    }

    /**
     * Takes in list of command names and returns formatted version
     * @param commandType commands
     * @return String formatted
     */
    private String commandFormatter(List<String> commandType) {
        StringBuilder sb = new StringBuilder();
        commandType.stream()
                .forEach(cmd -> {
                    sb.append(cmd).append(", ");
                });

        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

    @Override
    public String getName() {
        return "commands";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("cmds");
    }

    @Override
    public String getDescription() {
        return "Returns a list of valid bot commands";
    }

    @Override
    public String getUsage() {
        return "commands";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFORMATION;
    }
}
