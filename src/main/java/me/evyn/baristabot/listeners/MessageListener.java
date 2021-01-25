package me.evyn.baristabot.listeners;

import me.evyn.baristabot.commands.CommandHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs when the bot receives a message. Does initial parsing and sends cleaned command and arguments to the
 * command handler.
 */
public class MessageListener extends ListenerAdapter {

    private final String prefix;
    private final CommandHandler ch;

    public MessageListener(String prefix, CommandHandler ch) {
        this.prefix = prefix;
        this.ch = ch;
    }

    /**
     * Method that runs when the bot receives a message
     * @param event Discord message event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // return if sender is bot
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();

        // If message contains bot mention, direct user to send using prefix and return
        if (content.contains(event.getJDA().getSelfUser().getId())) {
            event.getChannel()
                    .sendMessage(String.format("My prefix is currently `%s`%nTry running `%shelp` for more information",this.prefix, this.prefix))
                    .queue();
        }

        // If message does not start with prefix, return
        if (!content.startsWith(this.prefix)) return;

        // Remove prefix from message, trim whitespace and split by space
        String[] msg = content.replace(this.prefix, "")
                .trim()
                .split(" ");

        // First cleaned message argument is the command
        String cmd = msg[0];

        // If there were any other arguments, add them to a new ArrayList
        List<String> args = new ArrayList<>();
        if (msg.length > 1) {
            // start with 1 because we want the arguments after the command
            for (int i = 1; i < msg.length; i++) {
                args.add(msg[i]);
            }
        }

        // Take all of the information that was gathered and pass it to the CommandHandler
        ch.run(event, cmd, args);
    }
}
