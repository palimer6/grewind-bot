package net.grewind.palimer.bot.utils;

import net.dv8tion.jda.api.entities.User;
import net.grewind.palimer.bot.sensitiveinfo.SecretStuff;
import org.jetbrains.annotations.NotNull;

public class UserUtils {
    public static boolean isBotAdmin(@NotNull User user) {
        return SecretStuff.BOT_ADMIN_IDS.contains(user.getIdLong());
    }
}
