package me.evyn.bot.commands.Settings;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Embeds implements Setting {

    @Override
    public void edit(MessageReceivedEvent event, String prefix, String[] args) {

    }

    @Override
    public void view(MessageReceivedEvent event, String prefix, String[] args) {

    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public String getExample() {
        return null;
    }
}
