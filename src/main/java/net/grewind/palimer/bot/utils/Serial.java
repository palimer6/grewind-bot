package net.grewind.palimer.bot.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

public class Serial {
    public static void addParent(JsonObject jsonSrc, Object src, Type typeOfParent, @NotNull JsonSerializationContext context) {
        for (Map.Entry<String, JsonElement> snowflakeEntry :
                context.serialize(src, typeOfParent).getAsJsonObject().entrySet()) {
            jsonSrc.add(snowflakeEntry.getKey(), snowflakeEntry.getValue());
        }
    }
}
