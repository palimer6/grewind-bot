package net.grewind.palimer.bot.utils;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Sender {
    public static <T> void sendMessage(@NotNull MessageChannel channel, @NotNull T message,
                                       @NotNull Function<T, MessageAction> method) {
        System.out.printf("sending message in channel %s: %s%n",
                channel.getName(), message.toString());
        method.apply(message).queue();
    }
}
