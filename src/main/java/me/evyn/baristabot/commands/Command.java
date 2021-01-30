package me.evyn.baristabot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public interface Command {

    void run(MessageReceivedEvent event, List<String> args);

    String getName();

    List<String> getAliases();

    String getDescription();

    String getUsage();

    CommandType getType();
}
