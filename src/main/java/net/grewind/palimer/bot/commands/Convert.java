package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;

public class Convert extends CommandExecutor {
    public static final String ROOT = "convert";
    protected Convert(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
