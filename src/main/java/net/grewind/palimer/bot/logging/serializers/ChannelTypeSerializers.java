package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.dv8tion.jda.api.entities.ChannelType;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class ChannelTypeSerializers implements Serializers<ChannelType> {
    @Override
    public Serializers.Simple<ChannelType> getSimpleSerializer() {
        return new Simple();
    }

    @Override
    public Serializers.Full<ChannelType> getFullSerializer() {
        return new Full();
    }

    private static class Simple implements Serializers.Simple<ChannelType> {
        @Override
        public JsonElement serialize(@NotNull ChannelType channelType, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonChannelType = new JsonObject();
            jsonChannelType.addProperty("id", channelType.getId());
            Serial.addParent(jsonChannelType, channelType, Enum.class, context);
            return jsonChannelType;
        }
    }

    private static class Full implements Serializers.Full<ChannelType> {
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
