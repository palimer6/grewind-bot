import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import sensitiveinfo.ApiKeys;

import javax.security.auth.login.LoginException;
import java.awt.Color;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main extends ListenerAdapter {
    private static User grewindBot;
    private static long upDate;

    public static void main(String[] args) {
        JDABuilder builder = new JDABuilder(AccountType.BOT)
                .setToken(ApiKeys.TOKEN)
                .setGame(Game.watching("I can't !help you yet. Pali is too lazy because STUPID INTELLIJ DOESN'T WANT TO HAVE BLOCK STRINGS WORK YET"))
                .addEventListener(new Main());
        try {
            grewindBot = builder.buildBlocking().getSelfUser();
            upDate = System.currentTimeMillis();
        } catch (InterruptedException | LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!isCommand(event)) {
            return;
        }
        if (botCheck(event)) {
            return;
        }
        parseCommand(event);
    }

    private void parseCommand(MessageReceivedEvent event, @NotNull String rawCommand) {
        String commandContent = rawCommand.replaceFirst("!", "");
        List<String> commandParts = Arrays.stream(commandContent.split("\\s"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        String commandSubContent = commandContent.replaceFirst(commandParts.get(0) + "\\s", "");
        String commandRoot = commandParts.get(0);
        List<String> commandModifiers;
        if (commandParts.size() > 1) {
            commandModifiers = commandParts.subList(1, commandParts.size());
        } else {
            commandModifiers = Collections.emptyList();
        }
        System.out.printf("command from %#s in %s channel %#s: %s%n",
                event.getAuthor(),
                event.getChannelType(),
                event.getChannel(),
                rawCommand);
        boolean commandRecognized = true;
        switch (commandRoot) {
            case "ping" -> commandRecognized = ping(event);
            case "say" -> commandRecognized = say(event, commandSubContent);
            case "timezones" -> commandRecognized = timezones(event, commandModifiers);
            case "botinfo" -> commandRecognized = botInfo(event);
            case "convert" -> commandRecognized = convert(event, commandModifiers);
            case "help" -> commandRecognized = help(event, commandModifiers);
            default -> System.out.printf("invalid comment: !%s", commandRoot);
        }
        if (!commandRecognized) {
            parseCommand(event, String.format("!help %s", commandRoot));
        }
    }

    private void parseCommand(MessageReceivedEvent event) {
        parseCommand(event, event.getMessage().getContentRaw());
    }

    private boolean ping(MessageReceivedEvent event) {
        sendMessage(event, "Pong!", s -> event.getChannel().sendMessage(s));
        return true;
    }

    private boolean say(@NotNull MessageReceivedEvent event, String text) {
        if (event.getChannelType() == ChannelType.TEXT) {
            event.getMessage().delete().queue();
        }
        sendMessage(event, text, s -> event.getChannel().sendMessage(s));
        return true;
    }

    private boolean convert(MessageReceivedEvent event, List<String> commandModifiers) {
        return false;
    }

    private boolean timezones(MessageReceivedEvent event, @NotNull List<String> commandModifiers) {
        int modifierCount = commandModifiers.size();
        if (modifierCount < 2) {
            return false;
        }
        String fromZone = commandModifiers.get(0);
        int time;
        Matcher matcher = Pattern.compile("(-?\\d+)([PpAa])?[Mm]?").matcher(commandModifiers.get(1));
        if (matcher.find()) {
            time = Integer.parseInt(matcher.group(1));
            if (matcher.group(2) != null) {
                time = switch (matcher.group(2)) {
                    case "A", "a" -> mod(time, 12);
                    case "P", "p" -> mod(time, 12) + 12;
                    default -> time;
                };
            }
        } else {
            return false;
        }
        int fromUtcOffset = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> zone : Zones.ZONES_MAP) {
            if (fromZone.matches(zone.getKey())) {
                fromUtcOffset = zone.getValue();
            }
        }
        if (fromUtcOffset == Integer.MAX_VALUE) {
            return false;
        }
        if (modifierCount > 2) {
            String toZone = commandModifiers.get(2);
            int toUtcOffset = Integer.MAX_VALUE;
            for (Map.Entry<String, Integer> zone : Zones.ZONES_MAP) {
                if (toZone.matches(zone.getKey())) {
                    toUtcOffset = zone.getValue();
                }
            }
            if (toUtcOffset == Integer.MAX_VALUE) {
                return false;
            }
            int finalTime = calculateTime(time, fromUtcOffset, toUtcOffset);
            sendMessage(event,
                    String.format("%s: %d\n%s: %d",
                            fromZone, mod(time, 24),
                            toZone, mod(finalTime, 24)),
                    s -> event.getChannel().sendMessage(s));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, Integer> zone : Zones.ZONES_MAP) {
                if (!zone.getKey().matches(".*\\d.*")) {
                    int finalTime = calculateTime(time, fromUtcOffset, zone.getValue());
                    stringBuilder.append(String.format("%s: %d\n",
                            zone.getKey(), mod(finalTime, 24)));
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            sendMessage(event, stringBuilder.toString(), s -> event.getChannel().sendMessage(s));
        }
        return true;
    }

    private int calculateTime(int time, int fromUtcOffset, int toUtcOffset) {
        return time - fromUtcOffset + toUtcOffset;
    }

    private boolean botInfo(MessageReceivedEvent event) {
        long upTime = System.currentTimeMillis() - upDate;
        LocalDateTime creationTime = grewindBot.getCreationTime().toLocalDateTime();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setDescription("Bot Information")
                .setColor(new Color(0xeb8c3f))
                .setThumbnail(grewindBot.getAvatarUrl())
                .addField("Bot Name", grewindBot.getName(), false)
                .addField("Created on",
                        String.format("%04d-%02d-%02d %02d:%02d",
                                creationTime.getYear(),
                                creationTime.getMonthValue(),
                                creationTime.getDayOfMonth(),
                                creationTime.getHour(),
                                creationTime.getMinute()), false)
                .addField("Current Up-Time",
                        String.format("%d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(upTime),
                                TimeUnit.MILLISECONDS.toMinutes(upTime) % 60,
                                TimeUnit.MILLISECONDS.toSeconds(upTime) & 60), false);
        sendMessage(event, embedBuilder.build(), e -> event.getChannel().sendMessage(e));
        return true;
    }

    private boolean help(MessageReceivedEvent event, List<String> commandModifiers) {
        return false;
    }

    private boolean isCommand(@NotNull MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().matches("!\\S.*");
    }

    private <T> void sendMessage(@NotNull MessageReceivedEvent event, @NotNull T message,
                                 @NotNull Function<T, MessageAction> method) {
        System.out.printf("sending message in channel %s: %s%n",
                event.getChannel().getName(), message.toString());
        method.apply(message).queue();
    }

    private boolean botCheck(@NotNull MessageReceivedEvent event) {
        boolean isBot = event.getAuthor().isBot();
        if (!event.getAuthor().equals(grewindBot)
                && isBot) {
            System.err.printf("command from bot %#s: %s%n",
                    event.getAuthor(),
                    event.getMessage().getContentRaw());
        }
        return isBot;
    }

    private int mod(int a, int b) {
        int ret = a % b;
        if (ret < 0) {
            ret += b;
        }
        return ret;
    }
}
