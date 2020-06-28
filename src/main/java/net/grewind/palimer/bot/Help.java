package net.grewind.palimer.bot;

public class Help {
    public static final String CODE_FORMAT = "```%s```";

    public static final String PREEMPT =
            "EVERYTHING IS CASE SENSITIVE\n" +
                    "The brackets {}[] are not to be used in the final command.\n" +
                    "Square brackets [] indicate a REQUIRED modifier.\n" +
                    "Curly brackets {} indicate an OPTIONAL modifier.\n";
    public static final String HELP_COMMAND =
            "\n!help {command root}\n";
    public static final String HELP_BASIC =
            "\tPMs you a list of basic help for every command.\n";
    public static final String HELP_VERBOSE =
            "\tYou may also write the root of a command after it to get more help for that command.\n" +
                    "\t\t(e.g. \"!help ping\" for !ping)\n";
    public static final String BOTINFO_COMMAND =
            "\n!botinfo\n";
    public static final String BOTINFO_BASIC =
            "\treturns an embed with info about Grewind Bot.\n";
    public static final String BOTINFO_VERBOSE =
            "\tThis embed will include:\n" +
                    "\t\tGrewind Bot's Icon\n" +
                    "\t\tGrewind Bot's Name (take a wild guess)\n" +
                    "\t\tGrewind Bot's creation date\n" +
                    "\t\tThe time since Grewind Bot was last started up\n";
    public static final String PING_COMMAND =
            "\n!ping\n";
    public static final String PING_BASIC =
            "\treturns a message to let you know the bot is up.\n";
    public static final String PING_VERBOSE =
            "\tGrewind Bot says \"Pong!\". What else is there to say?\n";
    public static final String SAY_COMMAND =
            "\n!say [message]\n";
    public static final String SAY_BASIC =
            "\tdeletes your command and makes Grewind Bot parrot [message].\n";
    public static final String SAY_VERBOSE =
            "\tGrewind Bot will still parrot your message if he does not have permissions to manage messages, " +
                    "but won't delete your command.\n" +
                    "\tThis is most notably the case in Private Messages.\n";
    public static final String TIMEZONES_COMMAND =
            "\n!timezones [fromZone] [hour] {toZone}\n";
    public static final String TIMEZONES_BASIC =
            "\treturns the corresponding 24-hour time for [fromZone] and {toZone}.\n" +
                    "\t\tIf no {toZone} is specified, it will list the time in 6 Grewind-relevant timezones.\n";
    public static final String TIMEZONES_VERBOSE =
            "\tYou may also write a or p after [hour].\n" +
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
    public static final String CONVERT_COMMAND =
            "\n!convert\n";
    public static final String CONVERT_BASIC =
            "\tComing soon\u2122\n";
    public static final String CONVERT_VERBOSE =
            "\tThis one will be the most work, ok? Give me time.\n";


}