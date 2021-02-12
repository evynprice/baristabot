package me.evyn.bot.commands.moderation;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.Arrays;
import java.util.List;

public class Kick implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User botUser = event.getJDA().getSelfUser();
        Member botMember = event.getGuild().getSelfMember();

        if (botMember.hasPermission(Permission.KICK_MEMBERS)) {

            if (event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                if (args.length > 0) {
                    String Id = args[0].replaceAll("[^0-9.]", "");

                    try {
                        RestAction<Member> tempMember = event.getGuild().retrieveMemberById(Id);
                        Member providedMember = tempMember.complete();

                        if (args.length > 1) {
                            StringBuilder sb = new StringBuilder();

                            for (int i=1; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            event.getGuild()
                                    .kick(providedMember, sb.toString())
                                    .queue();

                            EmbedBuilder eb = EmbedCreator.newCommandEmbedMessage(botUser);
                            eb.setTitle("Kick")
                                    .setDescription("User `" + providedMember.getUser().getAsTag() + "` was kicked " +
                                            "successfully.")
                                    .addField("Reason", sb.toString(), false);

                            event.getChannel()
                                    .sendMessage(eb.build())
                                    .queue();

                        } else {
                            event.getGuild()
                                    .kick(providedMember)
                                    .queue();

                            EmbedBuilder eb = EmbedCreator.newCommandEmbedMessage(botUser);
                            eb.setTitle("Kick")
                                    .setDescription("User " + providedMember.getUser().getAsTag() + " was kicked " +
                                            "successfully.");

                            event.getChannel()
                                    .sendMessage(eb.build())
                                    .queue();
                        }
                    } catch (Exception e) {
                        EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "That Id is either invalid or " +
                                "the user provided is not in the server.");
                        event.getChannel()
                                .sendMessage(eb.build())
                                .queue();
                    }
                } else {
                    EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "No user was provided " +
                            "For more information run the command `" + prefix + "usage kick`.");
                    event.getChannel()
                            .sendMessage(eb.build())
                            .queue();
                }

            } else {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "User is missing required " +
                        "permission `Kick Members`.");
                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            }
        } else {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "The bot is missing required " +
                    "permission `Kick Members`.");
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("");
    }

    @Override
    public String getDescription() {
        return "Kicks the provided user with an optional reason";
    }

    @Override
    public String getUsage() {
        return "kick [userMention/userId] (reason)";
    }

    @Override
    public CommandType getType() {
        return CommandType.MODERATION;
    }
}
