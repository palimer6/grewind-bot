package net.grewind.palimer.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.grewind.palimer.bot.utils.Sender;
import org.jetbrains.annotations.NotNull;

public class Help extends CommandExecutor {
    private static final String CODE_FORMAT = "```%s```";
    private static final String PREEMPT =
            "EVERYTHING IS CASE SENSITIVE\n" +
                    "The brackets {}[] are not to be used in the final command.\n" +
                    "Square brackets [] indicate a REQUIRED modifier.\n" +
                    "Curly brackets {} indicate an OPTIONAL modifier.\n";
    public static final String ROOT = "help";

    public Help(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        StringBuilder stringBuilder = new StringBuilder(PREEMPT);
        if (this.command.getCrownBranches().length == 0) {
            stringBuilder.append(ForHelp.INSTANCE.getGeneral());
            stringBuilder.append(ForBotInfo.INSTANCE.getGeneral());
            stringBuilder.append(ForPing.INSTANCE.getGeneral());
            stringBuilder.append(ForSay.INSTANCE.getGeneral());
            stringBuilder.append(ForTimezones.INSTANCE.getGeneral());
            stringBuilder.append(ForConvert.INSTANCE.getGeneral());
        } else {
            switch (this.command.getCrownBranch(0)) {
                case Help.ROOT -> stringBuilder.append(ForHelp.INSTANCE.getSpecific());
                case BotInfo.ROOT -> stringBuilder.append(ForBotInfo.INSTANCE.getSpecific());
                case Ping.ROOT -> stringBuilder.append(ForPing.INSTANCE.getSpecific());
                case Say.ROOT -> stringBuilder.append(ForSay.INSTANCE.getSpecific());
                case Timezones.ROOT -> stringBuilder.append(ForTimezones.INSTANCE.getSpecific());
                // TODO: add convert help?
                case Convert.ROOT -> stringBuilder.append(ForConvert.INSTANCE.getSpecific());
                default -> {
                    return false;
                }
            }
        }
        String text = String.format(CODE_FORMAT, stringBuilder);
        MessageChannel privateMessage = message.getAuthor().openPrivateChannel().complete();
        Sender.sendMessage(privateMessage,
                String.format(CODE_FORMAT, stringBuilder.toString()),
                s -> privateMessage.sendMessage(text));
        return true;
    }

    interface Text<T extends CommandExecutor> {
        @NotNull
        Class<T> getExecutor();

        default String getGeneral() {
            return getCommand() + getBasic();
        }

        default String getSpecific() {
            return getGeneral() + getVerbose();
        }

        @NotNull
        String getCommand();

        @NotNull
        String getBasic();

        @NotNull
        String getVerbose();
    }

    public static class ForHelp implements Text<Help> {

        public static final ForHelp INSTANCE = new ForHelp();

        @Override
        public @NotNull Class<Help> getExecutor() {
            return Help.class;
        }

        @Override
        public @NotNull String getCommand() {
            return "\n!help {command root}\n";
        }

        @Override
        public @NotNull String getBasic() {
            return "\tPMs you a list of basic help for every command.\n" +
                    "\tSpecify a command for more help.\n";
        }

        @Override
        public @NotNull String getVerbose() {
            return "\tYou may write the root of a command after it to get more help for that command.\n" +
                    "\t\t(e.g. \"!help ping\" for !ping)\n";
        }
    }

    public static class ForBotInfo implements Text<BotInfo> {

        public static final ForBotInfo INSTANCE = new ForBotInfo();

        @Override
        public @NotNull Class<BotInfo> getExecutor() {
            return BotInfo.class;
        }

        @Override
        public @NotNull String getCommand() {
            return "\n!botinfo\n";
        }

        @Override
        public @NotNull String getBasic() {
            return "\treturns an embed with info about Grewind Bot.\n";
        }

        @Override
        public @NotNull String getVerbose() {
            return "\tThis embed will include:\n" +
                    "\t\tGrewind Bot's Icon\n" +
                    "\t\tGrewind Bot's Name (take a wild guess)\n" +
                    "\t\tGrewind Bot's creation date\n" +
                    "\t\tThe time since Grewind Bot was last started up\n";
        }
    }

    public static class ForPing implements Text<Ping> {

        public static final ForPing INSTANCE = new ForPing();

        @Override
        public @NotNull Class<Ping> getExecutor() {
            return Ping.class;
        }

        @Override
        public @NotNull String getCommand() {
            return "\n!ping\n";
        }

        @Override
        public @NotNull String getBasic() {
            return "\treturns a message to let you know the bot is up.\n";
        }

        @Override
        public @NotNull String getVerbose() {
            return "\tGrewind Bot says \"Pong!\". What else is there to say?\n";
        }
    }

    public static class ForSay implements Text<Say> {

        public static final ForSay INSTANCE = new ForSay();

        @Override
        public @NotNull Class<Say> getExecutor() {
            return Say.class;
        }

        @Override
        public @NotNull String getCommand() {
            return "\n!say [message]\n";
        }

        @Override
        public @NotNull String getBasic() {
            return "\tdeletes your command and makes Grewind Bot parrot [message].\n";
        }

        @Override
        public @NotNull String getVerbose() {
            return "\tGrewind Bot will still parrot your message if he does not have permissions to manage messages, " +
                    "but won't delete your command.\n" +
                    "\tThis is most notably the case in Private Messages.\n";
        }
    }

    public static class ForTimezones implements Text<Timezones> {

        public static final ForTimezones INSTANCE = new ForTimezones();

        @Override
        public @NotNull Class<Timezones> getExecutor() {
            return Timezones.class;
        }

        @Override
        public @NotNull String getCommand() {
            return "\n!timezones [fromZone] [hour] {toZone}\n";
        }

        @Override
        public @NotNull String getBasic() {
            return "\treturns the corresponding 24-hour time for [fromZone] and {toZone}.\n" +
                    "\t\tIf no {toZone} is specified, it will list the time in 6 Grewind-relevant timezones.\n";
        }

        @Override
        public @NotNull String getVerbose() {
            return "\tYou may also write a or p after [hour].\n" +
                    "\t\t(without a space e.g. \"4p\")\n" +
                    "\tEvery full-hour UTC-offset is included, you may use them by typing the offset.\n" +
                    "\t\t(e.g. \"+09\")\n" +
                    "\tValid abbreviation-based offsets are:\n" +
                    "\t\tUTC-08:\tPST\n" +
                    "\t\tUTC-07:\tMST\n" +
                    "\t\tUTC-06:\tCST\n" +
                    "\t\tUTC-05:\tEST\n" +
                    "\t\tUTC\u00b100:\tGMT\n" +
                    "\t\tUTC+01:\tCET\n";
        }
    }

    public static class ForConvert implements Text<Convert> {

        public static final ForConvert INSTANCE = new ForConvert();

        @Override
        public @NotNull Class<Convert> getExecutor() {
            return Convert.class;
        }

        @Override
        public @NotNull String getCommand() {
            return "\n!convert [fromUnit] [toUnit] [value]\n";
        }

        @Override
        public @NotNull String getBasic() {
            return "\tIN BETA\u2122\n" +
                    "\t!convert help for more info.\n";
        }

        @Override
        public @NotNull String getVerbose() {
            return "\tOk, here's the thing.\n" +
                    "\tI imported a full library that has kiiind of shitty documentation.\n" +
                    "\tAt least for the user side.\n" +
                    "\tI'm not sure if I'll ever put real documentation here, cause it's just SO MANY UNITS.\n" +
                    "\tFeel free to try your best to grasp the javadoc:\n" +
                    "\thttps://javadoc.io/static/org.jscience/jscience/4.3.1/javax/measure/unit/SI.html\n" +
                    "\thttps://javadoc.io/static/org.jscience/jscience/4.3.1/javax/measure/unit/NonSI.html\n" +
                    "\tI also added some QoL improvements, like you can use C and F for Celsius and Fahrenheit\n" +
                    "\tand the units and prefixes that use Non-ASCII characters have ASCII alternatives\n" +
                    "\t\t(e.g. \u00c5 -> Angstrom, \u00b5 -> micro)\n" +
                    "\tYou can also use operators to make product units\n" +
                    "\t\t(e.g. m/s, mi^2, N*m, (m*kg)/s^2)\n" +
                    "\tThis library can do a lot, but man it might be too much lol.\n\n" +
                    "\tOh yeah. Currency probably still not happening anytime soon.\n" +
                    "\tThat would require live APIs that would either cost money or have a pretty low rate limit.\n";
        }
    }
}
