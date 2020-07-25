package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class EnumSerializers implements Serializers<Enum<?>> {


    @Override
    public Serializers.Simple<Enum<?>> getSimpleSerializer() {
        return new Simple();
    }

    @Override
    public Serializers.Full<Enum<?>> getFullSerializer() {
        return new Full();
    }

    private static class Simple implements Serializers.Simple<Enum<?>> {
        @Override
        public JsonElement serialize(@NotNull Enum<?> anEnum, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonEnum = new JsonObject();
            jsonEnum.addProperty("name", anEnum.name());
            return jsonEnum;
        }
    }

    private static class Full implements Serializers.Full<Enum<?>> {
        @Override
        public JsonElement serialize(@NotNull Enum<?> anEnum, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonEnum = new JsonObject();
            jsonEnum.addProperty("name", anEnum.name());
            jsonEnum.addProperty("ordinal", anEnum.ordinal());
            return jsonEnum;
        }
    }
}
