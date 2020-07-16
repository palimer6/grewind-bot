package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.dv8tion.jda.api.entities.IFakeable;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class SnowflakeSerializers {
    public static class Simple implements JsonSerializer<ISnowflake> {
        @Override
        public JsonElement serialize(@NotNull ISnowflake snowflake, Type typeOfSrc, @NotNull JsonSerializationContext context) {
            JsonObject jsonSnowflake = new JsonObject();
            jsonSnowflake.addProperty("idLong", snowflake.getIdLong());
            jsonSnowflake.add("timeCreated", context.serialize(snowflake.getTimeCreated()));
            return jsonSnowflake;
        }
    }

    public static class Full implements JsonSerializer<ISnowflake> {
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
