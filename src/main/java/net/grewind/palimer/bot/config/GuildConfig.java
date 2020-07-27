package net.grewind.palimer.bot.config;

import com.google.gson.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.*;

public class GuildConfig {
    private final long guildId;
    private final Set<Config> configs;

    public GuildConfig(long guildId, Set<Config> configs) {
        this.guildId = guildId;
        this.configs = configs;
    }

    public long getGuildId() {
        return guildId;
    }

    public Set<Config> getConfigs() {
        return configs;
    }

    public Guild getGuild(@NotNull JDA jda) {
        return jda.getGuildById(guildId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuildConfig that = (GuildConfig) o;
        return guildId == that.guildId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId);
    }

    public static class Config implements Map.Entry<String, Map.Entry<Config.Type, Long>> {
        private final String name;
        private Type type;
        private long id;

        public Config(String name, Type type, long id) {
            this.name = name;
            this.type = type;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }

        public long getId() {
            return id;
        }

        public ISnowflake getEntity(JDA jda) {
            switch (type) {
                case USER -> {
                    return getUser(jda);
                }
                case ROLE -> {
                    return getRole(jda);
                }
                case CHANNEL -> {
                    return getChannel(jda);
                }
                case EMOTE -> {
                    return getEmote(jda);
                }
            }
            return null;
        }

        public User getUser(@NotNull JDA jda) {
            return jda.getUserById(id);
        }

        public Role getRole(@NotNull JDA jda) {
            return jda.getRoleById(id);
        }

        public TextChannel getChannel(@NotNull JDA jda) {
            return jda.getTextChannelById(id);
        }

        public Emote getEmote(@NotNull JDA jda) {
            return jda.getEmoteById(id);
        }

        @Override
        public String getKey() {
            return name;
        }

        @Override
        public Map.Entry<Type, Long> getValue() {
            return new AbstractMap.SimpleEntry<>(type, id);
        }

        @Override
        public Map.Entry<Type, Long> setValue(Map.@NotNull Entry<Type, Long> value) {
            Type oldType = type;
            long oldId = id;
            this.type = value.getKey();
            this.id = value.getValue();
            return new AbstractMap.SimpleEntry<>(oldType, oldId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Config config = (Config) o;
            return name.equals(config.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        public enum Type {
            USER,
            ROLE,
            CHANNEL,
            EMOTE
        }
    }

    public static class GuildConfigDeserializer implements JsonDeserializer<GuildConfig> {
        @Override
        public GuildConfig deserialize(@NotNull JsonElement jsonGuildConfigE, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonGuildConfig = jsonGuildConfigE.getAsJsonObject();
            JsonArray jsonConfigs = jsonGuildConfig.get("configs").getAsJsonArray();
            long guildId = jsonGuildConfig.get("guildId").getAsLong();
            Set<Config> configs = new HashSet<>();
            for (JsonElement jsonConfigE : jsonConfigs) {
                JsonObject jsonConfig = jsonConfigE.getAsJsonObject();
                String name = jsonConfig.get("name").getAsString();
                Config.Type type = Config.Type.valueOf(jsonConfig.get("type").getAsString());
                long id = jsonConfig.get("id").getAsLong();
                Config config = new Config(name, type, id);
                configs.add(config);
            }
            return new GuildConfig(guildId, configs);
        }
    }

    public static class GuildConfigSerializer implements JsonSerializer<GuildConfig> {
        @Override
        public JsonElement serialize(@NotNull GuildConfig guildConfig, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonGuildConfig = new JsonObject();
            jsonGuildConfig.addProperty("guildId", guildConfig.guildId);
            JsonArray jsonConfigs = new JsonArray();
            for (Config config : guildConfig.configs) {
                JsonObject jsonConfig = new JsonObject();
                jsonConfig.addProperty("name", config.name);
                jsonConfig.addProperty("type", config.type.name());
                jsonConfig.addProperty("id", config.id);
                jsonConfigs.add(jsonConfig);
            }
            jsonGuildConfig.add("configs", jsonConfigs);
            return jsonGuildConfig;
        }
    }
}
