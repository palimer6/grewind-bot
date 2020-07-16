package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class EnumSerializers {
    public static class Simple implements JsonSerializer<Enum<?>> {
        @Override
        public JsonElement serialize(@NotNull Enum<?> anEnum, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonEnum = new JsonObject();
            jsonEnum.addProperty("name", anEnum.name());
            return jsonEnum;
        }
    }

    public static class Full implements JsonSerializer<Enum<?>> {
        @Override
        public JsonElement serialize(@NotNull Enum<?> anEnum, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonEnum = new JsonObject();
            jsonEnum.addProperty("name", anEnum.name());
            jsonEnum.addProperty("ordinal", anEnum.ordinal());
            return jsonEnum;
        }
    }
}
