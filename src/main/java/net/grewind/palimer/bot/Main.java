package net.grewind.palimer.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import net.grewind.palimer.bot.sensitiveinfo.ApiKeys;
import org.jetbrains.annotations.NotNull;

import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.grewind.palimer.bot.Help.*;
import static net.grewind.palimer.bot.TempMap.TEMP_MAP;

public class Main extends ListenerAdapter {
    private static User grewindBot;
    private static long startDate;

    public static void main(String[] args) {
        JDABuilder builder = new JDABuilder(AccountType.BOT)
                .setToken(ApiKeys.TOKEN)
                .setGame(Game.watching("type !help for commands"))
                .addEventListener(new Main());
        try {
            grewindBot = builder.buildBlocking().getSelfUser();
            startDate = System.currentTimeMillis();
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
            default -> System.out.printf("invalid comment: !%s%n", commandRoot);
        }
        if (!commandRecognized) {
            parseCommand(event, String.format("!help %s", commandRoot));
        }
    }

    private void parseCommand(MessageReceivedEvent event) {
        parseCommand(event, event.getMessage().getContentRaw());
    }

    private boolean ping(@NotNull MessageReceivedEvent event) {
        Random random = new Random();
        int randomInt = random.nextInt(1000);
        sendMessage(event.getChannel(),
                randomInt == 0 ? "I don't even like ping pong \uD83D\uDE14" : "Pong!",
                s -> event.getChannel().sendMessage(s));
        return true;
    }

    private boolean say(@NotNull MessageReceivedEvent event, String text) {
        if (event.getChannelType() == ChannelType.TEXT) {
            try {
                event.getMessage().delete().queue();
            } catch (InsufficientPermissionException e) {
                System.err.println("Permission to manage messages not given.");
            }
        }
        sendMessage(event.getChannel(),
                text,
                s -> event.getChannel().sendMessage(s));
        return true;
    }

    private boolean convert(MessageReceivedEvent event, @NotNull List<String> commandModifiers) {
        if (commandModifiers.size() < 3) {
            return false;
        }
        UnitFormat format = UnitFormat.getUCUMInstance();
        String fromStr = commandModifiers.get(0);
        String toStr = commandModifiers.get(1);
        String valStr = commandModifiers.get(2);
        Unit<?> from;
        Unit<?> to;
        UnitConverter converter;
        if (TEMP_MAP.containsKey(fromStr) && TEMP_MAP.containsKey(toStr)) {
            fromStr = TEMP_MAP.get(fromStr);
            toStr = TEMP_MAP.get(toStr);
        }
        double oldVal;
        double newVal;
        try {
            from = format.parseProductUnit(fromStr, new ParsePosition(0));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        try {
            to = format.parseProductUnit(toStr, new ParsePosition(0));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        try {
            converter = from.getConverterTo(to);
        } catch (ConversionException e) {
            e.printStackTrace();
            return false;
        }
        try {
            oldVal = Double.parseDouble(valStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        try {
            newVal = converter.convert(oldVal);
        } catch (ConversionException e) {
            e.printStackTrace();
            return false;
        }
        String message = String.format("%s%s is %s%s", oldVal, from, newVal, to);
        sendMessage(event.getChannel(),
                message,
                s -> event.getChannel().sendMessage(s));
        return true;
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
            sendMessage(event.getChannel(),
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
            sendMessage(event.getChannel(),
                    stringBuilder.toString(),
                    s -> event.getChannel().sendMessage(s));
        }
        return true;
    }

    private int calculateTime(int time, int fromUtcOffset, int toUtcOffset) {
        return time - fromUtcOffset + toUtcOffset;
    }

    private boolean botInfo(@NotNull MessageReceivedEvent event) {
        long startTime = System.currentTimeMillis() - startDate;
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
                .addField("Time since last start-up",
                        String.format("%d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(startTime),
                                TimeUnit.MILLISECONDS.toMinutes(startTime) % 60,
                                TimeUnit.MILLISECONDS.toSeconds(startTime) & 60), false);
        sendMessage(event.getChannel(),
                embedBuilder.build(),
                e -> event.getChannel().sendMessage(e));
        return true;
    }

    private boolean help(MessageReceivedEvent event, @NotNull List<String> commandModifiers) {
        StringBuilder stringBuilder = new StringBuilder(PREEMPT);
        if (commandModifiers.size() == 0) {
            stringBuilder.append(HELP_COMMAND).append(HELP_BASIC).append(HELP_VERBOSE);
            stringBuilder.append(BOTINFO_COMMAND).append(BOTINFO_BASIC);
            stringBuilder.append(PING_COMMAND).append(PING_BASIC);
            stringBuilder.append(SAY_COMMAND).append(SAY_BASIC);
            stringBuilder.append(TIMEZONES_COMMAND).append(TIMEZONES_BASIC);
            stringBuilder.append(CONVERT_COMMAND).append(CONVERT_BASIC);
        } else {
            switch (commandModifiers.get(0)) {
                case "help" -> stringBuilder.append(HELP_COMMAND).append(HELP_BASIC).append(HELP_VERBOSE);
                case "botinfo" -> stringBuilder.append(BOTINFO_COMMAND).append(BOTINFO_BASIC).append(BOTINFO_VERBOSE);
                case "ping" -> stringBuilder.append(PING_COMMAND).append(PING_BASIC).append(PING_VERBOSE);
                case "say" -> stringBuilder.append(SAY_COMMAND).append(SAY_BASIC).append(SAY_VERBOSE);
                case "timezones" -> stringBuilder.append(TIMEZONES_COMMAND).append(TIMEZONES_BASIC).append(TIMEZONES_VERBOSE);
                // TODO: add convert help?
                case "convert" -> stringBuilder.append(CONVERT_COMMAND).append(CONVERT_BASIC).append(CONVERT_VERBOSE);
                default -> {
                    return false;
                }
            }
        }
        String message = String.format(CODE_FORMAT, stringBuilder);
        MessageChannel privateMessage = event.getAuthor().openPrivateChannel().complete();
        sendMessage(privateMessage,
                String.format(CODE_FORMAT, stringBuilder.toString()),
                s -> privateMessage.sendMessage(message));
        return true;
    }

    private boolean isCommand(@NotNull MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().matches("!\\S.*");
    }

    private <T> void sendMessage(@NotNull MessageChannel channel, @NotNull T message,
                                 @NotNull Function<T, MessageAction> method) {
        System.out.printf("sending message in channel %s: %s%n",
                channel.getName(), message.toString());
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
