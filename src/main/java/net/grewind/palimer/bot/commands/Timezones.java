package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.Sender;
import net.grewind.palimer.bot.Zones;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String fromZone = command.getCrownBranches()[0];
        int time;
        Matcher matcher = Pattern.compile("(-?\\d+)([PpAa])?[Mm]?").matcher(command.getCrownBranches()[1]);
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
            String toZone = command.getCrownBranches()[2];
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
            Sender.sendMessage(message.getChannel(),
                    String.format("%s: %d\n%s: %d",
                            fromZone, mod(time, 24),
                            toZone, mod(finalTime, 24)),
                    s -> message.getChannel().sendMessage(s));
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
}
