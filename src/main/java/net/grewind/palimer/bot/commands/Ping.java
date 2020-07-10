package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;

public class Ping extends CommandExecutor {
    public static final String ROOT = "ping";
    protected Ping(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
