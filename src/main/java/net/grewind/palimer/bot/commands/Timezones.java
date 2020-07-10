package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;

public class Timezones extends CommandExecutor {
    public static final String ROOT = "timezones";
    protected Timezones(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
