package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class SnowflakeSerializers implements Serializers<ISnowflake>{
    @Override
    public Serializers.Simple<ISnowflake> getSimpleSerializer() {
        return new Simple();
    }

    @Override
    public Serializers.Full<ISnowflake> getFullSerializer() {
        return new Full();
    }

    private static class Simple implements Serializers.Simple<ISnowflake> {
        @Override
        public JsonElement serialize(@NotNull ISnowflake snowflake, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            JsonObject jsonSnowflake = new JsonObject();
            jsonSnowflake.addProperty("idLong", snowflake.getIdLong());
            jsonSnowflake.add("timeCreated", context.serialize(snowflake.getTimeCreated()));
            return jsonSnowflake;
        }
    }

    private static class Full implements Serializers.Full<ISnowflake> {
        @Override
        public JsonElement serialize(@NotNull ISnowflake snowflake, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            JsonObject jsonSnowflake = new JsonObject();
            jsonSnowflake.addProperty("id", snowflake.getId());
            jsonSnowflake.addProperty("idLong", snowflake.getIdLong());
            jsonSnowflake.add("timeCreated", context.serialize(snowflake.getTimeCreated()));
            return jsonSnowflake;
        }
    }
}
