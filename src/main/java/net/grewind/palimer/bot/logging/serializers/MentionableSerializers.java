package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.dv8tion.jda.api.entities.IFakeable;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

public class MentionableSerializers {
    public static class Simple implements JsonSerializer<IMentionable> {
        @Override
        public JsonElement serialize(IMentionable mentionable, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            return context.serialize(mentionable, ISnowflake.class).getAsJsonObject();
        }
    }

    public static class Full implements JsonSerializer<IMentionable> {
        @Override
        public JsonElement serialize(@NotNull IMentionable mentionable, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            JsonObject jsonMentionable = new JsonObject();
            jsonMentionable.addProperty("asMention", mentionable.getAsMention());
            Serial.addParent(jsonMentionable, mentionable, ISnowflake.class, context);
            return jsonMentionable;
        }
    }
}
