package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class MessageChannelSerializers {
    public static class Simple implements JsonSerializer<MessageChannel> {

        @Override
        public JsonElement serialize(@NotNull MessageChannel messageChannel, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            JsonObject jsonMessageChannel = new JsonObject();
            jsonMessageChannel.addProperty("name", messageChannel.getName());
            jsonMessageChannel.add("type", context.serialize(messageChannel.getType()));
            Serial.addParent(jsonMessageChannel, messageChannel, ISnowflake.class, context);
            return jsonMessageChannel;
        }
    }
}
