package me.evyn.baristabot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public interface Command {
    public void run(MessageReceivedEvent event, List<String> args);
    public String getName();
    public List<String> getAliases();
    public String getDescription();
    public String getUsage();
    public CommandType getType();
}
