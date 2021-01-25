package me.evyn.baristabot.listeners;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * This event triggers when the bot starts. It sends information to the console and is a placeholder for future
 * functionality.
 */
public class ReadyListener extends ListenerAdapter {

    private final String prefix;

    public ReadyListener(String prefix) {
        this.prefix = prefix;
    }

    /**
     * When bot is ready, sends information to console and sets bot presence.
     * @param event Bot ready event
     */
    @Override
    public void onReady(ReadyEvent event) {
        System.out.printf("Bot is ready in %s guilds",event.getGuildTotalCount());
        event.getJDA().getPresence().setActivity(Activity.watching("you | " + this.prefix + "help"));
    }
}
