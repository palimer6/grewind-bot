package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.dv8tion.jda.api.entities.ChannelType;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class ChannelTypeSerializers {
    public static class Simple implements JsonSerializer<ChannelType> {
        @Override
        public JsonElement serialize(@NotNull ChannelType channelType, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonChannelType = new JsonObject();
            jsonChannelType.addProperty("id", channelType.getId());
            Serial.addParent(jsonChannelType, channelType, Enum.class, context);
            return jsonChannelType;
        }
    }

    public static class Full implements JsonSerializer<ChannelType> {
        @Override
        public JsonElement serialize(@NotNull ChannelType channelType, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonChannelType = new JsonObject();
            jsonChannelType.addProperty("sortBucket", channelType.getSortBucket());
            jsonChannelType.addProperty("id", channelType.getId());
            jsonChannelType.addProperty("guild", channelType.isGuild());
            Serial.addParent(jsonChannelType, channelType, Enum.class, context);
            return jsonChannelType;
        }
    }
}
