package net.grewind.palimer.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.grewind.palimer.bot.commands.Command;
import net.grewind.palimer.bot.commands.CommandVisitor;
import net.grewind.palimer.bot.commands.Help;
import net.grewind.palimer.bot.logging.ListHandler;
import net.grewind.palimer.bot.logging.LogTimerTask;
import net.grewind.palimer.bot.sensitiveinfo.SecretStuff;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Bot {
    public static long botId;
    public static long startDate;
    public static final ListHandler<Message> MESSAGE_LIST_HANDLER = new ListHandler<>();
    private static final long LOG_DELAY = TimeUnit.MILLISECONDS.toMillis(0);
    private static final long LOG_PERIOD = TimeUnit.HOURS.toMillis(1);
    private static Timer logTimer = null;
    private static final TimerTask timerTask = new LogTimerTask();

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
            Message message = event.getMessage();
            if (!isCommand(message)) {
                return;
            }
            if (isAuthorBot(message)) {
                return;
            }
            Command command = new Command(message.getContentRaw());
            if (!CommandVisitor.visit(message, command).execute()) {
                //noinspection ResultOfMethodCallIgnored
                CommandVisitor.visit(message,
                        new Command(String.format("%s%s %s", Command.SOIL, Help.ROOT, command.getRoot()))).execute();
            }
            if (logTimer == null) {
                MESSAGE_LIST_HANDLER.syncedFunction(message,
                        MESSAGE_LIST_HANDLER.MESSAGE_LIST::add);
                logTimer = new Timer();
                logTimer.schedule(timerTask, LOG_DELAY, LOG_PERIOD);
            } else {
                new Thread(() -> MESSAGE_LIST_HANDLER.syncedFunction(message,
                        MESSAGE_LIST_HANDLER.MESSAGE_LIST::add)).start();
            }
        }

        @Override
        public void onShutdown(@Nonnull ShutdownEvent event) {
            timerTask.cancel();
            logTimer.cancel();
            timerTask.run();
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
