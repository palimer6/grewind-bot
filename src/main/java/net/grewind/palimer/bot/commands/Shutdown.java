package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.utils.Sender;
import net.grewind.palimer.bot.utils.UserUtils;

public class Shutdown extends CommandExecutor {
    public static final String ROOT = "shutdown";

    protected Shutdown(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        if (UserUtils.isBotAdmin(message.getAuthor())) {
            Sender.sendMessage(message.getChannel(), "Ok, bye!",
                    s -> message.getChannel().sendMessage(s));
            message.getJDA().shutdown();
            return true;
        } else {
            Sender.sendMessage(message.getChannel(), "My dad told me not to take advice from strangers.",
                    s -> message.getChannel().sendMessage(s));
            return false;
        }

    }
}
