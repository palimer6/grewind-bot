package net.grewind.palimer.bot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.grewind.palimer.bot.utils.FilePaths;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigLoader {
    private static final Path GUILD_CONFIG_PATH = FilePaths.CONFIG_DIR_PATH.resolve("guildConfig.json");
    private static final Path ROLE_CONFIG_PATH = FilePaths.CONFIG_DIR_PATH.resolve("roleConfig.json");


    public static @Nullable Set<GuildConfig> loadGuildConfig() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GuildConfig.class, new GuildConfig.GuildConfigDeserializer()).create();

        FileReader reader;
        try {
            reader = new FileReader(GUILD_CONFIG_PATH.toFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Set<GuildConfig> guildConfigs = gson.fromJson(reader, new TypeToken<Set<GuildConfig>>() {
        }.getType());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return guildConfigs;
    }

    public static GuildConfig loadGuildConfigByGuildId(long guildId) {
        Set<GuildConfig> guildConfigs = loadGuildConfig();
        if (guildConfigs == null) {
            return null;
        }
        guildConfigs = guildConfigs.stream().filter(r -> r.getGuildId() == guildId).collect(Collectors.toSet());
        if (guildConfigs.size() == 1) {
            return guildConfigs.iterator().next();
        } else if (guildConfigs.isEmpty()) {
            return null;
        } else {
            throw new IllegalStateException("There were multiple configs for guildId " + guildId);
        }
    }

    public static @Nullable Set<RoleConfig> loadRoleConfig() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RoleConfig.class, new RoleConfig.Deserializer())
                .registerTypeAdapter(RoleConfig.Association.class, new RoleConfig.Association.Deserializer()).create();
        FileReader reader;
        try {
            reader = new FileReader(ROLE_CONFIG_PATH.toFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Set<RoleConfig> roleConfigs = gson.fromJson(reader, new TypeToken<Set<RoleConfig>>() {
        }.getType());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return roleConfigs;
    }

    public static RoleConfig loadRoleConfigByGuildId(long guildId) {
        Set<RoleConfig> roleConfigs = loadRoleConfig();
        if (roleConfigs == null) {
            return null;
        }
        roleConfigs = roleConfigs.stream().filter(r -> r.getGuildId() == guildId).collect(Collectors.toSet());
        if (roleConfigs.size() == 1) {
            return roleConfigs.iterator().next();
        } else if (roleConfigs.isEmpty()) {
            return null;
        } else {
            throw new IllegalStateException("There were multiple configs for guildId " + guildId);
        }
    }
}
