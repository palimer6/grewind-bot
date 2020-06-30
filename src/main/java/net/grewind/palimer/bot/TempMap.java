package net.grewind.palimer.bot;

import java.util.*;

public class TempMap {
    public static final Map<String, String> TEMP_MAP = new HashMap<>();

    private static final Set<String> METRIC_PREFIXES = new HashSet<>();

    private static final Set<String> KELVIN = new HashSet<>();

    private static final Set<String> CELSIUS = new HashSet<>();

    private static final Set<String> RANKINE = new HashSet<>();

    private static final Set<String> FAHRENHEIT = new HashSet<>();

    static {
        METRIC_PREFIXES.addAll(Arrays.asList(
                "Y", "Z", "E", "P", "T", "G", "M", "k", "h", "da", "",
                "d", "c", "m", "µ", "micro", "n", "p", "f", "a", "z", "y"));
        KELVIN.addAll(Arrays.asList("K", "Kelvin", "kelvin"));
        CELSIUS.addAll(Arrays.asList("C", "℃", "°C", "Celsius", "celsius", "degree_celsius"));
        RANKINE.addAll(Arrays.asList("R", "°R", "Rankine", "rankine", "degree_rankine"));
        FAHRENHEIT.addAll(Arrays.asList("F", "℉", "°F", "Fahrenheit", "fahrenheit", "degree_fahrenheit"));
        for (String s : KELVIN) {
            for (String p : METRIC_PREFIXES) {
                TEMP_MAP.put(p + s, p + "K");
            }
        }
        for (String s : CELSIUS) {
            for (String p : METRIC_PREFIXES) {
                TEMP_MAP.put(p + s, p + "℃");
            }
        }
        for (String s : RANKINE) {
            TEMP_MAP.put(s, "°R");
        }
        for (String s : FAHRENHEIT) {
            TEMP_MAP.put(s, "°F");
        }
    }
}
