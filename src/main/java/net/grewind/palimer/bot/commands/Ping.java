package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.utils.Sender;

import java.util.Random;

public class Ping extends CommandExecutor {
    public static final String ROOT = "ping";

    protected Ping(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        Random random = new Random();
        int randomInt = random.nextInt(1000);
        Sender.sendMessage(message.getChannel(),
                randomInt == 0 ? "I don't even like ping pong \uD83D\uDE14" : "Pong!",
                s -> message.getChannel().sendMessage(s));
        return true;
    }
}
