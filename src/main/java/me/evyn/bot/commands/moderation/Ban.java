package me.evyn.bot.commands.moderation;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.Arrays;
import java.util.List;

public class Ban implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User botUser = event.getJDA().getSelfUser();
        Member botMember = event.getGuild().getSelfMember();

        EmbedBuilder eb;

        // if no arguments are present send error message
        if (args.length == 0) {
            eb = EmbedCreator.newErrorEmbedMessage(botUser, "Invalid command usage. Please run `" +
                    prefix + "usage ban` for more information.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // fetch User id
        String Id = args[0].replaceAll("[^0-9.]", "");

        Member providedMember;

        try {
            RestAction<Member> tempMember = event.getGuild().retrieveMemberById(Id);
            providedMember = tempMember.complete();

            // member was not found
        } catch (Exception e) {

            eb = EmbedCreator.newErrorEmbedMessage(botUser, "Provided member was not found.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // check if bot has required permissions
        if (!botMember.canInteract(providedMember)) {
            eb = EmbedCreator.newErrorEmbedMessage(botUser, "The bot does not have the required " +
                    "permissions for this command.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // check if user has required permissions
        if (!event.getMember().canInteract(providedMember)) {
            eb = EmbedCreator.newErrorEmbedMessage(botUser, "You do not have the required " +
                    "permissions for this command.");

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        eb = EmbedCreator.newCommandEmbedMessage(botUser);
        eb.setTitle("Ban");
        eb.setDescription("User `" + providedMember.getUser().getAsTag() + "` was banned successfully.");

        // Run command if no time or reason is specified
        if (args.length == 1) {
            providedMember.ban(0).queue();

            eb.addField("Deleted message history (days)", "0", true);

            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
            return;
        }

        // if the first argument is a number
        if (args[1].matches("[0-9]*")) {

            // if the number is in proper delDay format
            if (args[1].matches("[0-7]")) {

                // no reason provided
                if (args.length == 2) {
                    providedMember.ban(Integer.valueOf(args[1])).queue();

                    eb.addField("Deleted message history (days)", args[1], true);

                    event.getChannel()
                            .sendMessage(eb.build())
                            .queue();
                    return;
                } else {
                    // reason provided

                    StringBuilder sb = new StringBuilder();

                    for (int i=2; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }

                    providedMember.ban(Integer.valueOf(args[1]), sb.toString()).queue();

                    eb.addField("Deleted message history (days)", args[1], true)
                            .addField("Reason", sb.toString(), false);

                    event.getChannel()
                            .sendMessage(eb.build())
                            .queue();
                    return;
                }

            } else {
                eb = EmbedCreator.newErrorEmbedMessage(botUser, "Invalid arguments. Please run `" + prefix +
                        "usage ban` for more information.");

                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
                return;
            }
        }

        // No time was provided but args were provided
        StringBuilder sb = new StringBuilder();

        for (int i=1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        providedMember.ban(0, sb.toString()).queue();

        eb.addField("Deleted message history (days)", "0", true);
        eb.addField("Reason", sb.toString(), false);

        event.getChannel()
                .sendMessage(eb.build())
                .queue();
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("");
    }

    @Override
    public String getDescription() {
        return "Bans the provided user and deletes messages sent by user based on days with optional reason";
    }

    @Override
    public String getUsage() {
        return "ban [userMention/userId] (delDays 0-7) (reason)";
    }

    @Override
    public CommandType getType() {
        return CommandType.MODERATION;
    }
}
