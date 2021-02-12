package me.evyn.bot.commands.fun;

import me.evyn.bot.commands.Command;
import me.evyn.bot.commands.CommandType;
import me.evyn.bot.util.EmbedCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Say implements Command {

    @Override
    public void run(MessageReceivedEvent event, String prefix, String[] args) {

        User botUser = event.getJDA().getSelfUser();
        Member botMember = event.getGuild().getSelfMember();

        if (botMember.hasPermission(Permission.MESSAGE_MANAGE)) {

            if (args.length > 0) {
                StringBuilder sb = new StringBuilder();

                Arrays.stream(args)
                        .forEach(arg -> {
                            arg = arg.replace("@", "");
                            sb.append(arg).append(" ");
                        });

                event.getChannel()
                        .sendMessage(sb.toString())
                        .queue();

                event.getMessage()
                        .delete()
                        .queue();
            } else {
                EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "Missing message. " +
                        "Try running `" + prefix + "usage say` for proper command usage.");
                event.getChannel()
                        .sendMessage(eb.build())
                        .queue();
            }
        } else {
            EmbedBuilder eb = EmbedCreator.newErrorEmbedMessage(botUser, "The bot is missing the " +
                            "required permission `Manage Messages`.");
            event.getChannel()
                    .sendMessage(eb.build())
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "say";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("speak");
    }

    @Override
    public String getDescription() {
        return "Says the provided content and deletes the original message";
    }

    @Override
    public String getUsage() {
        return "say [content]";
    }

    @Override
    public CommandType getType() {
        return CommandType.FUN;
    }
}
