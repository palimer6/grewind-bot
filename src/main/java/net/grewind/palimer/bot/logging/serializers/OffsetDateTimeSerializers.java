package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeSerializers {
    public static class Simple implements JsonSerializer<OffsetDateTime> {
        @Override
        public JsonElement serialize(@NotNull OffsetDateTime offsetDateTime, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonOffsetDateTime = new JsonObject();
            jsonOffsetDateTime.addProperty("date", jsonOffsetDateTime.toString());
            jsonOffsetDateTime.addProperty("timestamp", Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()).getTime());
            return jsonOffsetDateTime;
        }
    }
}
