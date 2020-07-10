package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.Sender;

import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;

import static net.grewind.palimer.bot.TempMap.TEMP_MAP;

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
}
