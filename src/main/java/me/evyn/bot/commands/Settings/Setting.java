package me.evyn.bot.commands.Settings;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Setting {

    void edit(MessageReceivedEvent event, String prefix, String[] args);

    void view(MessageReceivedEvent event, String prefix, String[] args);

    String getName();

    String getDescription();

    String getUsage();

    String getExample();
}
