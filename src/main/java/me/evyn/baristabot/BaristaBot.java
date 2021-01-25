package me.evyn.baristabot;

import me.evyn.baristabot.commands.CommandHandler;
import me.evyn.baristabot.data.Config;
import me.evyn.baristabot.listeners.MessageListener;
import me.evyn.baristabot.listeners.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 * BaristaBot is a simple Discord bot written in JDA.
 *
 * @author  Evyn
 * @version 1.0
 * @since   2021-01-24
 */
public class BaristaBot {

    public static void main(String[] args) throws Exception {

        Config config = new Config();
        CommandHandler ch = new CommandHandler(config.getPrefix());

        JDA api = JDABuilder
                .createDefault(config.getToken())
                .addEventListeners(new ReadyListener(config.getPrefix()))
                .addEventListeners(new MessageListener(config.getPrefix(), ch))
                .build();
    }
}
