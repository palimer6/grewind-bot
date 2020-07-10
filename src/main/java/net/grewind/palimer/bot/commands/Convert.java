package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.utils.Sender;

import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Temperature;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.*;
import java.util.function.UnaryOperator;

import static net.grewind.palimer.bot.commands.Convert.TempMap.TEMP_MAP;

public class Convert extends CommandExecutor {
    public static final String ROOT = "convert";

    protected Convert(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        if (command.getCrownBranches().length < 3) {
            return false;
        }
        UnitFormat format = UnitFormat.getUCUMInstance();
        String fromStr = command.getCrownBranches()[0];
        String toStr = command.getCrownBranches()[1];
        String valStr = command.getCrownBranches()[2];
        Unit<?> from;
        Unit<?> to;
        UnitConverter converter;
        double oldVal;
        double newVal;
        if (TEMP_MAP.keySet().containsAll(Arrays.asList(fromStr, toStr))) {
            from = TEMP_MAP.get(fromStr).getKey().apply(TEMP_MAP.get(fromStr).getValue());
            to = TEMP_MAP.get(toStr).getKey().apply(TEMP_MAP.get(toStr).getValue());
        } else {
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
        String response = String.format("%s%s is %s%s", oldVal, from, (float) newVal, to).replaceAll("℃", "°C");
        Sender.sendMessage(message.getChannel(),
                response,
                s -> message.getChannel().sendMessage(s));
        return true;
    }

    public static class TempMap {
        public static final Map<String, Map.Entry<UnaryOperator<Unit<Temperature>>, Unit<Temperature>>> TEMP_MAP = new HashMap<>();

        private static final Map<String, UnaryOperator<Unit<Temperature>>> METRIC_PREFIX_MAP = new HashMap<>();

        private static final Set<String> KELVIN = new HashSet<>();

        private static final Set<String> CELSIUS = new HashSet<>();

        private static final Set<String> RANKINE = new HashSet<>();

        private static final Set<String> FAHRENHEIT = new HashSet<>();

        static {
            KELVIN.addAll(Arrays.asList("K", "Kelvin", "kelvin"));
            CELSIUS.addAll(Arrays.asList("C", "\u2103", "°C", "Celsius", "celsius", "degree_celsius"));
            RANKINE.addAll(Arrays.asList("R", "°R", "Rankine", "rankine", "degree_rankine"));
            FAHRENHEIT.addAll(Arrays.asList("F", "\u2109", "°F", "Fahrenheit", "fahrenheit", "degree_fahrenheit"));
            METRIC_PREFIX_MAP.put("Y", SI::YOTTA);
            METRIC_PREFIX_MAP.put("Z", SI::ZETTA);
            METRIC_PREFIX_MAP.put("E", SI::EXA);
            METRIC_PREFIX_MAP.put("P", SI::PETA);
            METRIC_PREFIX_MAP.put("T", SI::TERA);
            METRIC_PREFIX_MAP.put("G", SI::GIGA);
            METRIC_PREFIX_MAP.put("M", SI::MEGA);
            METRIC_PREFIX_MAP.put("k", SI::KILO);
            METRIC_PREFIX_MAP.put("h", SI::HECTO);
            METRIC_PREFIX_MAP.put("da", SI::DEKA);
            UnaryOperator<Unit<Temperature>> E0 = temperatureUnit -> temperatureUnit;
            METRIC_PREFIX_MAP.put("", E0);
            METRIC_PREFIX_MAP.put("d", SI::DECI);
            METRIC_PREFIX_MAP.put("c", SI::CENTI);
            METRIC_PREFIX_MAP.put("m", SI::MILLI);
            METRIC_PREFIX_MAP.put("\u00b5", SI::MICRO);
            METRIC_PREFIX_MAP.put("micro", SI::MICRO);
            METRIC_PREFIX_MAP.put("n", SI::NANO);
            METRIC_PREFIX_MAP.put("p", SI::PICO);
            METRIC_PREFIX_MAP.put("f", SI::FEMTO);
            METRIC_PREFIX_MAP.put("a", SI::ATTO);
            METRIC_PREFIX_MAP.put("z", SI::ZEPTO);
            METRIC_PREFIX_MAP.put("y", SI::YOCTO);
            for (Map.Entry<String, UnaryOperator<Unit<Temperature>>> p : METRIC_PREFIX_MAP.entrySet()) {
                for (String s : KELVIN) {
                    TEMP_MAP.put(p.getKey() + s, new AbstractMap.SimpleEntry<>(p.getValue(), SI.KELVIN));
                }
                for (String s : CELSIUS) {
                    TEMP_MAP.put(p.getKey() + s, new AbstractMap.SimpleEntry<>(p.getValue(), SI.CELSIUS));
                }
            }
            for (String s : RANKINE) {
                TEMP_MAP.put(s, new AbstractMap.SimpleEntry<>(E0, NonSI.RANKINE));
            }
            for (String s : FAHRENHEIT) {
                TEMP_MAP.put(s, new AbstractMap.SimpleEntry<>(E0, NonSI.FAHRENHEIT));
            }
        }
    }
}
