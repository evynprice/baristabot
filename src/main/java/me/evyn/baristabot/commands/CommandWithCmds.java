package me.evyn.baristabot.commands;

import java.util.List;

public interface CommandWithCmds extends Command {

    void addCommands(List<Command> cmds);
}
