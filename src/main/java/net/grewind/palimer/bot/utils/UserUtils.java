package net.grewind.palimer.bot.utils;

import net.dv8tion.jda.api.entities.User;
import net.grewind.palimer.bot.sensitiveinfo.SecretStuff;
import org.jetbrains.annotations.NotNull;

public class UserUtils {
    public static boolean isBotAdmin(@NotNull User user) {
        return isBotAdmin(user.getIdLong());
    }

    public static boolean isBotAdmin(long userId) {
        return SecretStuff.BOT_ADMID_IDS.contains(userId);
    }
}
