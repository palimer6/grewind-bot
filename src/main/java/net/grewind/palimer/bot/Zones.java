package net.grewind.palimer.bot;

import java.util.*;

public class Zones {
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
