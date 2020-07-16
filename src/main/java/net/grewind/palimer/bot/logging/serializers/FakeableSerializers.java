package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.dv8tion.jda.api.entities.IFakeable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class FakeableSerializers implements Serializers<IFakeable> {
    @Override
    public Serializers.Simple<IFakeable> getSimpleSerializer() {
        return new Simple();
    }

    @Override
    public Serializers.Full<IFakeable> getFullSerializer() {
        return new Full();
    }

    private static class Simple implements Serializers.Simple<IFakeable> {
        @Override
        public JsonElement serialize(IFakeable fakeable, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonObject();
        }
    }

    private static class Full implements Serializers.Full<IFakeable> {
        @Override
        public JsonElement serialize(@NotNull IFakeable fakeable, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonFakeable = new JsonObject();
            jsonFakeable.addProperty("fake", fakeable.isFake());
            return jsonFakeable;
        }
    }
}
