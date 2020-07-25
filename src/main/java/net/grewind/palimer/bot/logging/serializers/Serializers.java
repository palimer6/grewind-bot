package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface Serializers<T> {
    Simple<T> getSimpleSerializer();

    Full<T> getFullSerializer();

    default JsonSerializer<T> getSerializer(@NotNull SubType subType) {
        return switch (subType) {
            case SIMPLE -> getSimpleSerializer();
            case FULL -> getFullSerializer();
        };
    }

    interface Simple<T> extends JsonSerializer<T> {
        @Override
        JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context);
    }

    interface Full<T> extends JsonSerializer<T> {
        @Override
        JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context);
    }

    enum SubType {
        SIMPLE,
        FULL
    }
}
