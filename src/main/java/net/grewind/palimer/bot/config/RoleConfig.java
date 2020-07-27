package net.grewind.palimer.bot.config;

import com.google.gson.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleConfig {
    private final long guildId;
    private final Set<Association> associations;
    private Long messageId = null;

    public RoleConfig(long guildId, Set<Association> associations) {
        this.guildId = guildId;
        this.associations = associations;
    }

    public long getGuildId() {
        return guildId;
    }

    public Guild getGuild(@NotNull JDA jda) {
        return jda.getGuildById(guildId);
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        if (messageId == null) {
            this.messageId = messageId;
        } else {
            throw new Error();
        }
    }

    public Message getMessage(@NotNull TextChannel textChannel) {
        return textChannel.getHistory().getMessageById(messageId);
    }

    public Set<Association> getAssociations() {
        return associations;
    }

    public static class Association {
        @Nullable
        private final Long emoteId;
        @Nullable
        private final String unicode;
        private final long roleId;
        private final Set<Long> userIds;

        public Association(@NotNull Long emoteId, long roleId) {
            this.emoteId = emoteId;
            this.unicode = null;
            this.roleId = roleId;
            this.userIds = new HashSet<>();
        }

        public Association(@NotNull String unicode, long roleId) {
            this.emoteId = null;
            this.unicode = unicode;
            this.roleId = roleId;
            this.userIds = new HashSet<>();
        }

        public boolean isEmote() {
            return emoteId != null;
        }

        public boolean isEmoji() {
            return unicode != null;
        }

        public long getEmoteId() {
            if (emoteId == null) {
                throw new IllegalArgumentException();
            }
            return emoteId;
        }

        public Emote getEmote(JDA jda) {
            if (emoteId == null) {
                throw new IllegalArgumentException();
            }
            return jda.getEmoteById(emoteId);
        }

        public String getEmoji() {
            if (unicode == null) {
                throw new IllegalArgumentException();
            }
            return unicode;
        }

        public MessageReaction.ReactionEmote getReactionEmote(JDA jda) {
            if (emoteId == null && unicode != null) {
                return MessageReaction.ReactionEmote.fromUnicode(Objects.requireNonNull(unicode), jda);
            } else if (unicode == null && emoteId != null) {
                return MessageReaction.ReactionEmote.fromCustom(Objects.requireNonNull(jda.getEmoteById(emoteId)));
            }
            throw new IllegalStateException();
        }

        public long getRoleId() {
            return roleId;
        }

        public Role getRole(@NotNull JDA jda) {
            return jda.getRoleById(roleId);
        }

        public Set<Long> getUserIds() {
            return userIds;
        }

        public Set<User> getUsers(@NotNull JDA jda) {
            return userIds.stream().map(jda::getUserById).collect(Collectors.toSet());
        }

        public static class Serializer implements JsonSerializer<Association> {
            @Override
            public JsonElement serialize(Association src, Type typeOfSrc, JsonSerializationContext context) {
                return null;
            }
        }

        public static class Deserializer implements JsonDeserializer<Association> {
            @Override
            public Association deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return null;
            }
        }
    }

    public static class Serializer implements JsonSerializer<RoleConfig> {
        @Override
        public JsonElement serialize(RoleConfig src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }
    }

    public static class Deserializer implements JsonDeserializer<RoleConfig> {
        @Override
        public RoleConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }
    }
}
