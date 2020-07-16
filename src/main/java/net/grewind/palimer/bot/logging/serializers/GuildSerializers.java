package net.grewind.palimer.bot.logging.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.grewind.palimer.bot.utils.Serial;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class GuildSerializers {
    public static class Simple implements JsonSerializer<Guild> {
        @Override
        public JsonElement serialize(@NotNull Guild guild, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonGuild = new JsonObject();
            jsonGuild.addProperty("name", guild.getName());
            Serial.addParent(jsonGuild, guild, ISnowflake.class, context);
            return jsonGuild;
        }
    }
}
