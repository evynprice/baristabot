package me.evyn.baristabot;

import me.evyn.baristabot.commands.Command;

import java.util.List;

public interface CommandWithCmds extends Command {
    public void addCommands(List<Command> cmds);
}
