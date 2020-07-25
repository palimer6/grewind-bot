package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeSerializers implements Serializers<OffsetDateTime> {
    @Override
    public Serializers.Simple<OffsetDateTime> getSimpleSerializer() {
        return new Simple();
    }

    @Deprecated
    @Override
    public Serializers.Full<OffsetDateTime> getFullSerializer() {
        throw new UnsupportedOperationException();
    }

    private static class Simple implements Serializers.Simple<OffsetDateTime> {
        @Override
        public JsonElement serialize(@NotNull OffsetDateTime offsetDateTime, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonOffsetDateTime = new JsonObject();
            jsonOffsetDateTime.addProperty("date", offsetDateTime.toString());
            jsonOffsetDateTime.addProperty("timestamp", Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()).getTime());
            return jsonOffsetDateTime;
        }
    }
}
