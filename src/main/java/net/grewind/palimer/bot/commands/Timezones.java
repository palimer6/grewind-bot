package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.utils.Sender;

import java.util.AbstractMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.grewind.palimer.bot.commands.Timezones.Zones.ZONES_MAP;

public class Timezones extends CommandExecutor {
    public static final String ROOT = "timezones";

    protected Timezones(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        int modifierCount = command.getCrownBranches().length;
        if (modifierCount < 2) {
            return false;
        }
        String fromZone = command.getCrownBranch(0);
        int time;
        Matcher matcher = Pattern.compile("(-?\\d+)([PpAa])?[Mm]?").matcher(command.getCrownBranch(1));
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
        for (Map.Entry<String, Integer> zone : ZONES_MAP) {
            if (fromZone.matches(zone.getKey())) {
                fromUtcOffset = zone.getValue();
            }
        }
        if (fromUtcOffset == Integer.MAX_VALUE) {
            return false;
        }
        if (modifierCount > 2) {
            String toZone = command.getCrownBranch(2);
            int toUtcOffset = Integer.MAX_VALUE;
            for (Map.Entry<String, Integer> zone : ZONES_MAP) {
                if (toZone.matches(zone.getKey())) {
                    toUtcOffset = zone.getValue();
                }
            }
            if (toUtcOffset == Integer.MAX_VALUE) {
                return false;
            }
            int finalTime = calculateTime(time, fromUtcOffset, toUtcOffset);
            Sender.sendMessage(message.getChannel(),
                    String.format("%s: %d\n%s: %d",
                            fromZone, mod(time, 24),
                            toZone, mod(finalTime, 24)),
                    s -> message.getChannel().sendMessage(s));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, Integer> zone : ZONES_MAP) {
                if (!zone.getKey().matches(".*\\d.*")) {
                    int finalTime = calculateTime(time, fromUtcOffset, zone.getValue());
                    stringBuilder.append(String.format("%s: %d\n",
                            zone.getKey(), mod(finalTime, 24)));
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            Sender.sendMessage(message.getChannel(),
                    stringBuilder.toString(),
                    s -> message.getChannel().sendMessage(s));
        }
        return true;
    }

    private int mod(int a, int b) {
        int ret = a % b;
        if (ret < 0) {
            ret += b;
        }
        return ret;
    }

    private int calculateTime(int time, int fromUtcOffset, int toUtcOffset) {
        return time - fromUtcOffset + toUtcOffset;
    }

    public static class Zones {
        public static final SortedSet<Map.Entry<String, Integer>> ZONES_MAP =
                new TreeSet<>((o1, o2) -> {
                    int compareVal = Integer.compare(o1.getValue(), o2.getValue());
                    if (compareVal == 0) {
                        return o1.getKey().compareTo(o2.getKey());
                    } else {
                        return compareVal;
                    }
                });

        static {
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*12", -12));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*11", -11));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*10", -10));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*9", -9));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*8", -8));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("PST", -8));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*7", -7));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("MST", -7));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*6", -6));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("CST", -6));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*5", -5));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("EST", -5));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*4", -4));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*3", -3));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*2", -2));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("-0*1", -1));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("GMT", 0));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("[\\+-±]0+", 0));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("CET", 1));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*1", 1));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*2", 2));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*3", 3));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*4", 4));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*5", 5));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*6", 6));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*7", 7));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*8", 8));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*9", 9));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*10", 10));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*11", 11));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*12", 12));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*13", 13));
            ZONES_MAP.add(new AbstractMap.SimpleEntry<>("\\+0*14", 14));

        }
    }
}
