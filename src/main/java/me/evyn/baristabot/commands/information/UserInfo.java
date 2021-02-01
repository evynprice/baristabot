package me.evyn.baristabot.commands.information;

import me.evyn.baristabot.commands.Command;
import me.evyn.baristabot.commands.CommandType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UserInfo implements Command {

    @Override
    public void run(MessageReceivedEvent event, List<String> args) {
        Member member;
        User user;

        // If there are no arguments, collect information on user that runs the command
        if (args.isEmpty()) {
            member = event.getMember();
            user = event.getAuthor();
        }
        // There were arguments, attempt to find user based on ID
        else {
            // Strip argument down to ID
            String ID = args.get(0).replaceAll("[^0-9.]", "");

            // Attempt to find member by ID
            try {
                RestAction<Member> tempMember = event.getGuild().retrieveMemberById(ID);
                member = tempMember.complete();
                user = member.getUser();
            } catch (Exception e) {
                event.getChannel()
                        .sendMessage("Error: That ID is either invalid or the user is not in the server")
                        .queue();
                return;
            }
        }

        // Format join and register dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        LocalDateTime registerTimeCreated = user.getTimeCreated().toLocalDateTime();
        LocalDateTime joinTimeCreated = member.getTimeJoined().toLocalDateTime();

        String registerDate = formatter.format(registerTimeCreated);
        String joinDate = formatter.format(joinTimeCreated);

        // format roles
        StringBuilder roles = new StringBuilder();

        if (member.getRoles().size() > 0) {
            member.getRoles().stream()
                    .map(role -> role.getName())
                    .forEach(role -> roles.append(role).append(", "));
            roles.delete(roles.length() - 2, roles.length() - 1);
        } else {
            roles.append("none");
        }

        // create embed
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(user.getAsTag())
                .setColor(0x386895)
                .setThumbnail(user.getAvatarUrl())
                .addField("ID", user.getId(), true)
                .addField("Bot", String.valueOf(user.isBot()), true)
                .addField("Joined", joinDate, true)
                .addField("Registered", registerDate, false)
                .addField("Roles (" + member.getRoles().size() + ")", roles.toString(), true);

        // send embed
        event.getTextChannel()
                .sendMessage(eb.build())
                .queue();
        return;
    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("");
    }

    @Override
    public String getDescription() {
        return "Provides information on the user that runs the command or mentioned user";
    }

    @Override
    public String getUsage() {
        return "userinfo (user mention / ID)";
    }

    @Override
    public CommandType getType() {
        return CommandType.INFORMATION;
    }
}
