package net.grewind.palimer.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.grewind.palimer.bot.commands.Command;
import net.grewind.palimer.bot.commands.CommandVisitor;
import net.grewind.palimer.bot.commands.Help;
import net.grewind.palimer.bot.commands.ReactRoles;
import net.grewind.palimer.bot.config.ConfigLoader;
import net.grewind.palimer.bot.config.RoleConfig;
import net.grewind.palimer.bot.logging.ListHandler;
import net.grewind.palimer.bot.logging.LogTimerTask;
import net.grewind.palimer.bot.settings.Settings;
import net.grewind.palimer.bot.utils.UserUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Bot {
    public static final ListHandler<Message> MESSAGE_LIST_HANDLER = new ListHandler<>();
    private static final long LOG_DELAY = TimeUnit.MILLISECONDS.toMillis(0);
    private static final long LOG_PERIOD = TimeUnit.HOURS.toMillis(1);
    private static final TimerTask TIMER_TASK = new LogTimerTask();

    public static long startDate;
    private static Timer logTimer = null;

    public static void main(String[] args) {
        Settings.build();
    }

    public static class Listeners extends ListenerAdapter {
        public static boolean isCommand(@NotNull Message message) {
            return message.getContentRaw().matches("!\\S.*");
        }

        @Override
        public void onReady(@Nonnull ReadyEvent event) {
            startDate = System.currentTimeMillis();
            if (Settings.isDebug()) {
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
            if (Settings.isLogging()) {
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
        public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
            MyClass myClass = new MyClass(event.getReactionEmote());
            if (ReactRoles.getMessageIds().contains(event.getMessageIdLong())) {
                if (UserUtils.isBotAdmin(event.getUserIdLong())) {
                    event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(myClass::action);
                }
            } else {
                // TODO: check and apply roles from roles message
            }
        }

        public static class MyClass {
            private final MessageReaction.ReactionEmote reactionEmote;

            private MyClass(MessageReaction.ReactionEmote reactionEmote) {
                this.reactionEmote = reactionEmote;
            }

            private void action(Message message) {
                RoleConfig.Association association = null;
                List<Role> mentionedRoles = message.getMentionedRoles();
                if (mentionedRoles.isEmpty()) {
                    return;
                }
                if (reactionEmote.isEmoji()) {
                    association = new RoleConfig.Association(reactionEmote.getEmoji(), mentionedRoles.get(0).getIdLong());
                } else if (reactionEmote.isEmote()) {
                    association = new RoleConfig.Association(reactionEmote.getEmote().getIdLong(), mentionedRoles.get(0).getIdLong());
                } else {
                    return;
                }
                RoleConfig roleConfig = ConfigLoader.loadRoleConfigByGuildId(message.getGuild().getIdLong());
                if (roleConfig == null) {
                    roleConfig = new RoleConfig(message.getGuild().getIdLong());
                }
                roleConfig.getAssociations().add(association);
            }
        }

        @Override
        public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
            super.onGuildMessageReactionRemove(event);
        }

        @Override
        public void onRoleUpdateName(@Nonnull RoleUpdateNameEvent event) {
            super.onRoleUpdateName(event);
        }

        @Override
        public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
            super.onRoleDelete(event);
        }

        @Override
        public void onEmoteUpdateName(@Nonnull EmoteUpdateNameEvent event) {
            super.onEmoteUpdateName(event);
        }

        @Override
        public void onEmoteRemoved(@Nonnull EmoteRemovedEvent event) {
            super.onEmoteRemoved(event);
        }

        @Override
        public void onShutdown(@Nonnull ShutdownEvent event) {
            if (Settings.isLogging()) {
                TIMER_TASK.cancel();
                logTimer.cancel();
                TIMER_TASK.run();
            }
        }

        private boolean isAuthorBot(@NotNull Message message) {
            boolean isBot = message.getAuthor().isBot();
            if (message.getAuthor().getIdLong() != message.getJDA().getSelfUser().getIdLong() && isBot) {
                System.err.printf("command from bot %#s: %s%n",
                        message.getAuthor(),
                        message.getContentRaw());
            }
            return isBot;
        }
    }

}
