package net.grewind.palimer.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
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
    private static final TimerTask TIMER_TASK = new LogTimerTask();
    private static final Activity RELEASE_ACTIVITY =
            Activity.of(Activity.ActivityType.DEFAULT,
                    String.format("type %s%s for commands", Command.SOIL, Help.ROOT));
    private static final Activity DEBUG_ACTIVITY =
            Activity.of(Activity.ActivityType.DEFAULT, "around with the code");
    private static final OnlineStatus RELEASE_STATUS = OnlineStatus.ONLINE;
    private static final OnlineStatus DEBUG_STATUS = OnlineStatus.DO_NOT_DISTURB;
    private static final boolean IS_DEBUG = true;
    private static final boolean IS_LOGGING = false;

    public static void main(String[] args) {
        @SuppressWarnings("RedundantCast")
        JDABuilder jdaBuilder = JDABuilder.create((String) SecretStuff.TOKEN,
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .setActivity(IS_DEBUG ? DEBUG_ACTIVITY : RELEASE_ACTIVITY)
                .setStatus(IS_DEBUG ? DEBUG_STATUS : RELEASE_STATUS)
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
            if (IS_DEBUG) {
                System.out.println("Bot is now ready.");
            }
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
            if (IS_LOGGING) {
                if (logTimer == null) {
                    MESSAGE_LIST_HANDLER.syncedFunction(message,
                            MESSAGE_LIST_HANDLER.MESSAGE_LIST::add);
                    logTimer = new Timer();
                    logTimer.schedule(TIMER_TASK, LOG_DELAY, LOG_PERIOD);
                } else {
                    new Thread(() -> MESSAGE_LIST_HANDLER.syncedFunction(message,
                            MESSAGE_LIST_HANDLER.MESSAGE_LIST::add)).start();
                }
            }
        }

        @Override
        public void onShutdown(@Nonnull ShutdownEvent event) {
            if (IS_LOGGING) {
                TIMER_TASK.cancel();
                logTimer.cancel();
                TIMER_TASK.run();
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
