package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.grewind.palimer.bot.config.ConfigLoader;
import net.grewind.palimer.bot.config.GuildConfig;
import net.grewind.palimer.bot.config.RoleConfig;
import net.grewind.palimer.bot.utils.MentionUtils;
import net.grewind.palimer.bot.utils.UserUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ReactRoles extends CommandExecutor {
    public static final String ROOT = "reactroles";
    private static final String ROLE_CHANNEL_CONFIG = "roleChannel";

    private static final Set<Long> MESSAGE_IDS = new HashSet<>();

    protected ReactRoles(Message message, Command command) {
        super(message, command);
    }

    public static boolean addMessageId(Long messageId) {
        return alterMessageIdSet(messageId, true);
    }

    public static boolean removeMessageId(Long messageId) {
        return alterMessageIdSet(messageId, false);
    }

    private static synchronized boolean alterMessageIdSet(Long messageId, boolean add) {
        if (add) {
            return MESSAGE_IDS.add(messageId);
        } else {
            return MESSAGE_IDS.remove(messageId);
        }
    }

    public static Set<Long> getMessageIds() {
        return new HashSet<>(MESSAGE_IDS);
    }

    @Override
    public boolean execute() {
        if (!UserUtils.isBotAdmin(message.getAuthor())) {
            return false;
        }
        GuildConfig guildConfig = ConfigLoader.loadGuildConfigByGuildId(message.getGuild().getIdLong());
        if (guildConfig == null) {
            return false;
        }
        RoleConfig roleConfig = ConfigLoader.loadRoleConfigByGuildId(message.getGuild().getIdLong());
        if (roleConfig == null) {
            return false;
        }
        Set<GuildConfig.Config> filteredConfigs = guildConfig.getConfigs().stream()
                .filter(config -> config.getName().equals(ROLE_CHANNEL_CONFIG)).collect(Collectors.toSet());
        if (filteredConfigs.isEmpty()) {
            return false;
        }

        TextChannel roleChannel = message.getJDA().getTextChannelById(new ArrayList<>(filteredConfigs).get(0).getId());
        if (roleConfig.getMessageId() == 0) {
            Objects.requireNonNull(roleChannel)
                    .sendMessage("```There's going to be something cool here soon```")
                    .queue(message -> roleConfig.setMessageId(message.getIdLong()));
        }

        // TODO: done retrieving configs. Retrieve wanted role and react emote now
        String roleMention = command.getCrownBranch(0);
        Message.MentionType mentionType = MentionUtils.getMentionType(roleMention);
        if (mentionType != Message.MentionType.ROLE) {
            return false;
        }
        addMessageId(message.getIdLong());

        return true;
    }
}
