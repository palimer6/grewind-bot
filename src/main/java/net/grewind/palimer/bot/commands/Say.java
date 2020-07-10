package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;

public class Say extends CommandExecutor {
    public static final String ROOT = "say";
    protected Say(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        return false;
    }
}
