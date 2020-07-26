package net.grewind.palimer.bot.utils;

import java.nio.file.Path;

public class FilePaths {
    public static final Path MAIN_DIR_PATH = Path.of(System.getProperty("user.dir"));
    public static final Path JSON_DIR_PATH = MAIN_DIR_PATH.resolve("json");
    public static final Path LOG_DIR_PATH = JSON_DIR_PATH.resolve("logs");
    public static final Path CONFIG_DIR_PATH = JSON_DIR_PATH.resolve("config");
}
