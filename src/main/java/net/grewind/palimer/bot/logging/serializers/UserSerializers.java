package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.dv8tion.jda.api.entities.IFakeable;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.User;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class UserSerializers implements Serializers<User> {
    @Override
    public Serializers.Simple<User> getSimpleSerializer() {
        return new Simple();
    }

    @Deprecated
    @Override
    public Serializers.Full<User> getFullSerializer() {
        throw new UnsupportedOperationException();
    }

    private static class Simple implements Serializers.Simple<User> {
        @Override
        public JsonElement serialize(@NotNull User user, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonUser = new JsonObject();
            jsonUser.addProperty("name", user.getName());
            jsonUser.addProperty("discriminator", Short.parseShort(user.getDiscriminator()));
            jsonUser.addProperty("asTag", user.getAsTag());
            jsonUser.addProperty("bot", user.isBot());
            Serial.addParent(jsonUser, user, IMentionable.class, context);
            Serial.addParent(jsonUser, user, IFakeable.class, context);
            return jsonUser;
        }
    }
}
