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
    private final Set<Association> associations = new HashSet<>();
    private Long messageId = null;

    public RoleConfig(long guildId) {
        this.guildId = guildId;
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
        private final String emoji;
        private final long roleId;
        private final Set<Long> userIds;

        public Association(@NotNull Long emoteId, long roleId) {
            this.emoteId = emoteId;
            this.emoji = null;
            this.roleId = roleId;
            this.userIds = new HashSet<>();
        }

        public Association(@NotNull String emoji, long roleId) {
            this.emoteId = null;
            this.emoji = emoji;
            this.roleId = roleId;
            this.userIds = new HashSet<>();
        }

        public boolean isEmote() {
            return emoteId != null;
        }

        public boolean isEmoji() {
            return emoji != null;
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
            if (emoji == null) {
                throw new IllegalArgumentException();
            }
            return emoji;
        }

        public MessageReaction.ReactionEmote getReactionEmote(JDA jda) {
            if (emoteId == null && emoji != null) {
                return MessageReaction.ReactionEmote.fromUnicode(Objects.requireNonNull(emoji), jda);
            } else if (emoji == null && emoteId != null) {
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
            public JsonElement serialize(Association association, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject jsonAssociation = new JsonObject();
                jsonAssociation.addProperty("roleId", association.getRoleId());
                String reactType;
                if (association.isEmoji()) {
                    reactType = "Emoji";
                    jsonAssociation.addProperty("emote", association.getEmoji());
                } else if (association.isEmote()) {
                    reactType = "Emote";
                    jsonAssociation.addProperty("emote", association.getEmoteId());
                } else {
                    throw new IllegalStateException("ReactEmote of Association was not either Emoji or Emote.");
                }
                jsonAssociation.addProperty("reactType", reactType);
                JsonArray jsonUserIds = new JsonArray();
                for (long userId : association.getUserIds()) {
                    jsonUserIds.add(userId);
                }
                jsonAssociation.add("userIds", jsonUserIds);
                return jsonAssociation;
            }
        }

        public static class Deserializer implements JsonDeserializer<Association> {
            @Override
            public Association deserialize(JsonElement jsonAssociation, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonAssociationObject = jsonAssociation.getAsJsonObject();
                long roleId = jsonAssociationObject.get("roleId").getAsLong();
                String reactType = jsonAssociationObject.get("reactType").getAsString();
                Set<Long> userIds = new HashSet<>();
                JsonArray jsonUserIds = jsonAssociationObject.get("userIds").getAsJsonArray();
                for (JsonElement jsonUserId : jsonUserIds) {
                    userIds.add(jsonUserId.getAsLong());
                }
                Association association = switch (reactType) {
                    case "Emoji" -> new Association(jsonAssociationObject.get("emote").getAsString(), roleId);
                    case "Emote" -> new Association(jsonAssociationObject.get("emote").getAsLong(), roleId);
                    default -> throw new IllegalStateException("Unexpected value in reactType: " + reactType);
                };
                association.getUserIds().addAll(userIds);

                return association;
            }
        }
    }

    public static class Serializer implements JsonSerializer<RoleConfig> {
        @Override
        public JsonElement serialize(RoleConfig roleConfig, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonRoleConfig = new JsonObject();
            jsonRoleConfig.addProperty("guildId", roleConfig.getGuildId());
            if (roleConfig.getMessageId() == null) {
                throw new IllegalStateException("messageId was not yet set");
            } else {
                jsonRoleConfig.addProperty("messageId", roleConfig.getMessageId());
            }
            JsonArray jsonAssociations = new JsonArray();
            for (Association association : roleConfig.getAssociations()) {
                jsonAssociations.add(context.serialize(association));
            }
            jsonRoleConfig.add("associations", jsonAssociations);
            return jsonRoleConfig;
        }
    }

    public static class Deserializer implements JsonDeserializer<RoleConfig> {
        @Override
        public RoleConfig deserialize(JsonElement jsonRoleConfig, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonRoleConfigObject = jsonRoleConfig.getAsJsonObject();
            long guildId = jsonRoleConfigObject.get("guildId").getAsLong();
            RoleConfig roleConfig = new RoleConfig(guildId);
            roleConfig.setMessageId(jsonRoleConfigObject.get("messageId").getAsLong());
            for (JsonElement element : jsonRoleConfigObject.get("associations").getAsJsonArray()) {
                roleConfig.getAssociations().add(context.deserialize(element, Association.class));
            }
            return roleConfig;
        }
    }
}
