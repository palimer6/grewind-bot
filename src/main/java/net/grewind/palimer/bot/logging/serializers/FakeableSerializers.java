package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.dv8tion.jda.api.entities.IFakeable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class FakeableSerializers {
    public static class Simple implements JsonSerializer<IFakeable> {
        @Override
        public JsonElement serialize(IFakeable fakeable, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonObject();
        }
    }

    public static class Full implements JsonSerializer<IFakeable> {
        @Override
        public JsonElement serialize(@NotNull IFakeable fakeable, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonFakeable = new JsonObject();
            jsonFakeable.addProperty("fake", fakeable.isFake());
            return jsonFakeable;
        }
    }
}
