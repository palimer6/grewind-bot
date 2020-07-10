package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.sensitiveinfo.SecretStuff;

public class Shutdown extends CommandExecutor {
    public static final String ROOT = "shutdown";

    protected Shutdown(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        if (message.getAuthor().getIdLong() == SecretStuff.CREATOR_ID) {
            message.getJDA().shutdown();
            return true;
        }
        return false;
    }
}
