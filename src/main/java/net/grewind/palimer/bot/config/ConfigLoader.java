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
}
