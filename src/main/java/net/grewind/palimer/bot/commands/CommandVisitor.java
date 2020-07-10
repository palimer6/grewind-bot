package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class CommandVisitor {
    public static CommandExecutor visit(Message message, @NotNull Command command) {
        String root = command.getRoot();
        return switch (root) {
            case "botinfo" -> new Botinfo(message, command);
            case "convert" -> new Convert(message, command);
            case "ping" -> new Ping(message, command);
            case "say" -> new Say(message, command);
            case "timezones" -> new Timezones(message, command);
            case "help" -> new Help(message, command);
            default -> new Help(message, command);
        };
    }
}
