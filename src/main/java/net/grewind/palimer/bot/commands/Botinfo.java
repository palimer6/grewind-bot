package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;
import net.grewind.palimer.bot.Bot;
import net.grewind.palimer.bot.Sender;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Botinfo extends CommandExecutor {
    public static final String ROOT = "botinfo";

    protected Botinfo(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        long startTime = System.currentTimeMillis() - Bot.startDate;
        SelfUser botUser = message.getJDA().getSelfUser();
        LocalDateTime creationTime = botUser.getTimeCreated().toLocalDateTime();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setDescription("Bot Information")
                .setColor(new Color(0xeb8c3f))
                .setThumbnail(botUser.getAvatarUrl())
                .addField("Bot Name", botUser.getName(), false)
                .addField("Created on",
                        String.format("%04d-%02d-%02d %02d:%02d",
                                creationTime.getYear(),
                                creationTime.getMonthValue(),
                                creationTime.getDayOfMonth(),
                                creationTime.getHour(),
                                creationTime.getMinute()), false)
                .addField("Time since last start-up",
                        String.format("%d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(startTime),
                                TimeUnit.MILLISECONDS.toMinutes(startTime) % 60,
                                TimeUnit.MILLISECONDS.toSeconds(startTime) & 60), false);
        Sender.sendMessage(message.getChannel(),
                embedBuilder.build(),
                e -> message.getChannel().sendMessage(e));
        return true;
    }
}
