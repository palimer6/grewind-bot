package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.grewind.palimer.bot.utils.Sender;

public class Say extends CommandExecutor {
    public static final String ROOT = "say";

    protected Say(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        if (message.getChannelType() == ChannelType.TEXT) {
            try {
                message.delete().queue();
            } catch (InsufficientPermissionException e) {
                System.err.println("Permission to manage messages not given.");
            } catch (IllegalStateException e) {
                System.err.println("Message was not sent in a text channel.");
            }
        }
        Sender.sendMessage(message.getChannel(),
                command.getCrown(),
                s -> message.getChannel().sendMessage(s));
        return true;
    }
}
