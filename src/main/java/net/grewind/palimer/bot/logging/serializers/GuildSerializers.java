package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class GuildSerializers implements Serializers<Guild> {
    @Override
    public Serializers.Simple<Guild> getSimpleSerializer() {
        return new Simple();
    }

    @Deprecated
    @Override
    public Serializers.Full<Guild> getFullSerializer() {
        throw new UnsupportedOperationException();
    }

    private static class Simple implements Serializers.Simple<Guild> {
        @Override
        public JsonElement serialize(@NotNull Guild guild, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonGuild = new JsonObject();
            jsonGuild.addProperty("name", guild.getName());
            Serial.addParent(jsonGuild, guild, ISnowflake.class, context);
            return jsonGuild;
        }
    }
}
