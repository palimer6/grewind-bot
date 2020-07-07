package net.grewind.palimer.bot;

import com.google.gson.*;
import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TimerTask;

public class LogTimerTask extends TimerTask {
    public static final String THREAD_NAME = "log_timer_task";
    private static final Path LOG_PATH = Paths.get(System.getProperty("user.dir") + "\\grewindBotLogs\\log.json");

    @Override
    public void run() {
        Thread thread = new Thread(THREAD_NAME) {
            @Override
            public void run() {
                Main.MESSAGE_LIST_HANDLER.syncedRunnable(() -> {
                    Gson gson = new GsonBuilder()
                            .serializeNulls()
                            .setPrettyPrinting()
                            .create();
                    String oldLogsRaw;
                    JsonArray logsJson;
                    if (Files.notExists(LOG_PATH)) {
                        try {
                            Files.createDirectory(LOG_PATH);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        try {
                            Files.createFile(LOG_PATH);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        try {
                            Files.writeString(LOG_PATH, "[]");
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    try {
                        oldLogsRaw = Files.readString(LOG_PATH);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    try {
                        logsJson = gson.fromJson(oldLogsRaw, JsonArray.class);
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        return;
                    }
                    for (Message message : Main.MESSAGE_LIST_HANDLER.MESSAGE_LIST) {
                        JsonElement jsonTree = gson.toJsonTree(message);
                        logsJson.add(jsonTree);
                    }
                    try {
                        Files.delete(LOG_PATH);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    try {
                        Files.createFile(LOG_PATH);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    try {
                        Files.writeString(LOG_PATH, gson.toJson(logsJson));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    Main.MESSAGE_LIST_HANDLER.MESSAGE_LIST.clear();
                });
            }
        };
    }
}
