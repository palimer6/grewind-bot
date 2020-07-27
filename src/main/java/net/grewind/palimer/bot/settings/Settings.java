package net.grewind.palimer.bot.settings;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.grewind.palimer.bot.Bot;
import net.grewind.palimer.bot.commands.Command;
import net.grewind.palimer.bot.commands.Help;
import net.grewind.palimer.bot.sensitiveinfo.SecretStuff;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

public class Settings {
    private static final boolean DEBUG = true;
    private static final boolean LOGGING = false;
    private static final UnaryOperator<JDABuilder> DEBUG_SETUP =
            jdaBuilder -> jdaBuilder
                    .setActivity(Activity.of(Activity.ActivityType.DEFAULT, "around with the code"))
                    .setStatus(OnlineStatus.DO_NOT_DISTURB);
    private static final UnaryOperator<JDABuilder> RELEASE_SETUP =
            jdaBuilder -> jdaBuilder
                    .setActivity(Activity.of(Activity.ActivityType.DEFAULT,
                            String.format("type %s%s for commands", Command.SOIL, Help.ROOT)))
                    .setStatus(OnlineStatus.ONLINE);

    public static boolean isDebug() {
        return DEBUG;
    }

    public static boolean isLogging() {
        return LOGGING;
    }

    public static void build() {
        JDABuilder jdaBuilder = JDABuilder.create(SecretStuff.TOKEN, getGatewayIntents());
        if (DEBUG) {
            DEBUG_SETUP.apply(jdaBuilder);
        } else {
            RELEASE_SETUP.apply(jdaBuilder);
        }
        jdaBuilder.addEventListeners(new Bot.Listeners());
        try {
            jdaBuilder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static @NotNull Collection<GatewayIntent> getGatewayIntents() {
        Set<GatewayIntent> intents = new HashSet<>();
        intents.add(GUILD_MESSAGES);
        intents.add(DIRECT_MESSAGES);
        intents.add(GUILD_MESSAGE_REACTIONS);
        return intents;
    }
}
