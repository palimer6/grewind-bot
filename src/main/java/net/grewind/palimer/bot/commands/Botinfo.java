package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;

public class Botinfo extends CommandExecutor {
    public static final String ROOT = "botinfo";
    protected Botinfo(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
