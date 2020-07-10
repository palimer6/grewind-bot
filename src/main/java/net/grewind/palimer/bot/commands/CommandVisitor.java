package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class CommandVisitor {
    public static CommandExecutor visit(@NotNull Message message, @NotNull Command command) {
        String root = command.getRoot();
        System.out.printf("command from %#s in %s channel %#s: %s%n",
                message.getAuthor(),
                message.getChannelType(),
                message.getChannel(),
                command.getTree());
        return switch (root) {
            case Botinfo.ROOT -> new Botinfo(message, command);
            case Convert.ROOT -> new Convert(message, command);
            case Ping.ROOT -> new Ping(message, command);
            case Say.ROOT -> new Say(message, command);
            case Timezones.ROOT -> new Timezones(message, command);
            case Help.ROOT -> new Help(message, command);
            default -> new Help(message, command);
        };
    }
}
