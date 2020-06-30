package net.grewind.palimer.bot;

import javax.measure.quantity.Temperature;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import java.util.*;
import java.util.function.UnaryOperator;

public class TempMap {
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
        for(String s: FAHRENHEIT){
            TEMP_MAP.put(s, new AbstractMap.SimpleEntry<>(E0, NonSI.FAHRENHEIT));
        }
    }
}
