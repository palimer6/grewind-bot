package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.*;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class MessageSerializers {
    public static class Simple implements JsonSerializer<Message> {
        @Override
        public JsonElement serialize(@NotNull Message message, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            JsonObject jsonMessage = new JsonObject();
            jsonMessage.add("author", context.serialize(message.getAuthor()));
            jsonMessage.addProperty("contentDisplay", message.getContentDisplay());
            jsonMessage.addProperty("contentRaw", message.getContentRaw());
            jsonMessage.addProperty("contentStripped", message.getContentStripped());
            jsonMessage.add("channel", context.serialize(message.getChannel()));
            try {
                jsonMessage.add("guild", context.serialize(message.getGuild()));
            } catch (UnsupportedOperationException | IllegalStateException e) {
                jsonMessage.add("guild", JsonNull.INSTANCE);
            }
            Serial.addParent(jsonMessage, message, ISnowflake.class, context);
            return jsonMessage;
        }
    }
}
