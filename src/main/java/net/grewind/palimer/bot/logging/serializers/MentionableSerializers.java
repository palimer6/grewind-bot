package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class MentionableSerializers implements Serializers<IMentionable> {
    @Override
    public Serializers.Simple<IMentionable> getSimpleSerializer() {
        return new Simple();
    }

    @Override
    public Serializers.Full<IMentionable> getFullSerializer() {
        return new Full();
    }

    private static class Simple implements Serializers.Simple<IMentionable> {
        @Override
        public JsonElement serialize(IMentionable mentionable, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            return context.serialize(mentionable, ISnowflake.class).getAsJsonObject();
        }
    }

    private static class Full implements Serializers.Full<IMentionable> {
        @Override
        public JsonElement serialize(@NotNull IMentionable mentionable, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            JsonObject jsonMentionable = new JsonObject();
            jsonMentionable.addProperty("asMention", mentionable.getAsMention());
            Serial.addParent(jsonMentionable, mentionable, ISnowflake.class, context);
            return jsonMentionable;
        }
    }
}
