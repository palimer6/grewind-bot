package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;

public abstract class CommandExecutor {
    protected Message message;
    protected Command command;

    protected CommandExecutor(Message message, Command command) {
        this.message = message;
        this.command = command;
    }

    public abstract boolean execute();
}
