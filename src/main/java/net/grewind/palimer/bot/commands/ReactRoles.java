package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.grewind.palimer.bot.config.ConfigLoader;
import net.grewind.palimer.bot.config.GuildConfig;
import net.grewind.palimer.bot.config.RoleConfig;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ReactRoles extends CommandExecutor {
    public static final String ROOT = "reactroles";
    private static final String ROLE_CHANNEL_CONFIG = "roleChannel";

    protected ReactRoles(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        Set<GuildConfig> guildConfigs = ConfigLoader.loadGuildConfig();
        if (guildConfigs == null) {
            return false;
        }
        Set<RoleConfig> roleConfigs = ConfigLoader.loadRoleConfig();
        if (roleConfigs == null) {
            return false;
        }
        Set<GuildConfig> filteredGuildConfigs = guildConfigs.stream()
                .filter(guildConfig -> guildConfig.getGuildId() == message.getGuild().getIdLong())
                .collect(Collectors.toSet());

        if (filteredGuildConfigs.isEmpty()) {
            return false;
        }
        GuildConfig guildConfig = new ArrayList<>(filteredGuildConfigs).get(0);
        Set<GuildConfig.Config> filteredConfigs = guildConfig.getConfigs().stream()
                .filter(config -> config.getName().equals(ROLE_CHANNEL_CONFIG)).collect(Collectors.toSet());
        if (filteredConfigs.isEmpty()) {
            return false;
        }

        Set<RoleConfig> filteredRoleConfigs = roleConfigs.stream()
                .filter(roleConfig -> message.getGuild().getIdLong() == roleConfig.getGuildId())
                .collect(Collectors.toSet());
        if (filteredRoleConfigs.isEmpty()) {
            return false;
        }
        RoleConfig roleConfig = new ArrayList<>(filteredRoleConfigs).get(0);
        TextChannel roleChannel = message.getJDA().getTextChannelById(new ArrayList<>(filteredConfigs).get(0).getId());
        if (roleConfig.getMessageId() == 0) {
            Objects.requireNonNull(roleChannel)
                    .sendMessage("```There's going to be something cool here soon```")
                    .queue(message -> roleConfig.setMessageId(message.getIdLong()));
        }

        // TODO: done retrieving configs. Retrieve wanted role and react emote now

        return false;
    }
}
