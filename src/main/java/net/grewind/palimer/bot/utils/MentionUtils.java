package net.grewind.palimer.bot.utils;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Nullable;

public class MentionUtils {
    public static Message.@Nullable MentionType getMentionType(String text) {
        for (Message.MentionType mentionType : Message.MentionType.values()) {
            if (mentionType.getPattern().matcher(text).find()) {
                return mentionType;
            }
        }
        return null;
    }
}
