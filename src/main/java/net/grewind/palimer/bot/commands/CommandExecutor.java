package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;

import java.util.function.BiPredicate;

@FunctionalInterface
interface CommandExecutor extends BiPredicate<Message, Command> {
    @Override
    default boolean test(Message message, Command command) {
        return execute(message, command);
    }

    boolean execute(Message message, Command command);
}
