package net.grewind.palimer.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.grewind.palimer.bot.commands.Command;
import net.grewind.palimer.bot.commands.CommandVisitor;
import net.grewind.palimer.bot.commands.Help;
import net.grewind.palimer.bot.sensitiveinfo.SecretStuff;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class Bot {
    public static long botId;
    public static long startDate;

    public static void main(String[] args) {
        @SuppressWarnings("RedundantCast")
        JDABuilder jdaBuilder = JDABuilder.create((String) SecretStuff.TOKEN,
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .setActivity(Activity.of(Activity.ActivityType.DEFAULT,
                        String.format("type %s%s for commands", Command.SOIL, Help.ROOT)))
                .addEventListeners(new Bot.Listeners());
        try {
            jdaBuilder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static class Listeners extends ListenerAdapter {
        public static boolean isCommand(@NotNull Message message) {
            return message.getContentRaw().matches("!\\S.*");
        }

        @Override
        public void onReady(@Nonnull ReadyEvent event) {
            startDate = System.currentTimeMillis();
            botId = event.getJDA().getSelfUser().getIdLong();
        }

        @Override
        public void onMessageReceived(@NotNull MessageReceivedEvent event) {
            if (!isCommand(event.getMessage())) {
                return;
            }
            if (isAuthorBot(event.getMessage())) {
                return;
            }
            Command command = new Command(event.getMessage().getContentRaw());
            if (!CommandVisitor.visit(event.getMessage(), command).execute()) {
                //noinspection ResultOfMethodCallIgnored
                CommandVisitor.visit(event.getMessage(),
                        new Command(String.format("%s%s %s", Command.SOIL, Help.ROOT, command.getRoot()))).execute();
            }
        }

        private boolean isAuthorBot(@NotNull Message message) {
            boolean isBot = message.getAuthor().isBot();
            if (message.getAuthor().getIdLong() != botId && isBot) {
                System.err.printf("command from bot %#s: %s%n",
                        message.getAuthor(),
                        message.getContentRaw());
            }
            return isBot;
        }
    }

}
