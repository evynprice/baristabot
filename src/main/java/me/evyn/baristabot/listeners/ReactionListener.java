package me.evyn.baristabot.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * This listener is implemented for a specific Discord server, but the concepts can be applied to any server.
 */
public class ReactionListener extends ListenerAdapter {

    private final String publicRolesID = "728325099438080141";
    private final String colorRolesID = "728344851103613048";

    private final String greenEmoji = "\uD83D\uDFE2";
    private final String redEmoji = "\uD83D\uDD34";
    private final String purpleEmoji = "\uD83D\uDFE3";
    private final String yellowEmoji = "\uD83D\uDFE1";
    private final String blueEmoji = "\uD83D\uDD35";
    private final String brownEmoji = "\uD83D\uDFE4";
    private final String orangeEmoji = "\uD83D\uDFE0";

    private Guild myGuild;

    private Role minecraftRole;
    private Role amongRole;
    private Role programmingRole;
    private Role artRole;

    private Map<String, Role> publicRoles;

    private Role greenRole;
    private Role purpleRole;
    private Role blueRole;
    private Role brownRole;
    private Role orangeRole;
    private Role redRole;
    private Role yellowRole;

    private Map<String, Role> colorRoles;

    public void init(Guild myGuild) {
        this.myGuild = myGuild;
        if (myGuild == null) {
            return;
        }
        this.minecraftRole = this.myGuild.getRoleById("727556140900286574");
        this.amongRole = this.myGuild.getRoleById("753024472805671002");
        this.programmingRole = this.myGuild.getRoleById("728062189402980413");
        this.artRole = this.myGuild.getRoleById("728397783928799262");

        this.publicRoles = new HashMap<>();
        this.publicRoles.put(this.greenEmoji, this.minecraftRole);
        this.publicRoles.put(this.redEmoji, amongRole);
        this.publicRoles.put(this.purpleEmoji, this.programmingRole);
        this.publicRoles.put(this.yellowEmoji, this.artRole);

        this.greenRole = this.myGuild.getRoleById("728341643828265100");
        this.purpleRole = this.myGuild.getRoleById("728341709590757457");
        this.blueRole = this.myGuild.getRoleById("728341674266329220");
        this.brownRole = this.myGuild.getRoleById("728341745535811674");
        this.orangeRole = this.myGuild.getRoleById("728341801861382155");
        this.redRole = this.myGuild.getRoleById("728341843590381628");
        this.yellowRole = this.myGuild.getRoleById("728341772190875760");

        this.colorRoles = new HashMap<>();
        this.colorRoles.put(this.greenEmoji, this.greenRole);
        this.colorRoles.put(this.purpleEmoji, this.purpleRole);
        this.colorRoles.put(this.blueEmoji, this.blueRole);
        this.colorRoles.put(this.brownEmoji, this.brownRole);
        this.colorRoles.put(this.orangeEmoji, this.orangeRole);
        this.colorRoles.put(this.redEmoji, this.redRole);
        this.colorRoles.put(this.yellowEmoji, this.yellowRole);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        if (this.myGuild == null) {
            return;
        }

        Guild reactionGuild = event.getGuild();

        if (reactionGuild.equals(myGuild)) {
            String messageID = event.getMessageId();
            String emoji = event.getReaction().getReactionEmote().getEmoji();

            String userId = event.getUserId();

            if (messageID.equals(this.publicRolesID)) {
                if (this.publicRoles.containsKey(emoji)) {
                    Role roleToAdd = this.publicRoles.get(emoji);
                    this.myGuild.addRoleToMember(userId, roleToAdd).queue();
                }
            }

            if (messageID.equals(this.colorRolesID)) {
                if (this.colorRoles.containsKey(emoji)) {
                    Role roleToAdd = this.colorRoles.get(emoji);
                    this.myGuild.addRoleToMember(userId, roleToAdd).queue();
                }
            }
        }
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        Guild reactionGuild = event.getGuild();

        if (reactionGuild.equals(myGuild)) {
            String messageID = event.getMessageId();
            String emoji = event.getReaction().getReactionEmote().getEmoji();

            String userId = event.getUserId();

            if (messageID.equals(this.publicRolesID)) {
                if (this.publicRoles.containsKey(emoji)) {
                    Role roleToRemove = this.publicRoles.get(emoji);
                    this.myGuild.removeRoleFromMember(userId, roleToRemove).queue();
                }
            }

            if (messageID.equals(this.colorRolesID)) {
                if (this.colorRoles.containsKey(emoji)) {
                    Role roleToRemove = this.colorRoles.get(emoji);
                    this.myGuild.removeRoleFromMember(userId, roleToRemove).queue();
                }
            }
        }
    }
}
