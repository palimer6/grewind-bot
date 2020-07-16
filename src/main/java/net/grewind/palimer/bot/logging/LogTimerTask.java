package net.grewind.palimer.bot.logging;

import com.google.gson.*;
import net.dv8tion.jda.api.entities.*;
import net.grewind.palimer.bot.Bot;
import net.grewind.palimer.bot.logging.serializers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.TimerTask;

public class LogTimerTask extends TimerTask {
    public static final String THREAD_NAME = "log_timer_task";
    private static final Path LOG_PATH = Paths.get(System.getProperty("user.dir") + "\\grewindBotLogs\\log.json");
    private static final Gson GSON =
            new GsonBuilder()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .registerTypeHierarchyAdapter(Enum.class,
                            new EnumSerializers().getFullSerializer())
                    .registerTypeHierarchyAdapter(OffsetDateTime.class,
                            new OffsetDateTimeSerializers().getSimpleSerializer())
                    .registerTypeHierarchyAdapter(ISnowflake.class,
                            new SnowflakeSerializers().getFullSerializer())
                    .registerTypeHierarchyAdapter(IMentionable.class,
                            new MentionableSerializers().getFullSerializer())
                    .registerTypeHierarchyAdapter(IFakeable.class,
                            new FakeableSerializers().getFullSerializer())
                    .registerTypeHierarchyAdapter(ChannelType.class,
                            new ChannelTypeSerializers().getFullSerializer())
                    .registerTypeHierarchyAdapter(User.class,
                            new UserSerializers().getSimpleSerializer())
                    .registerTypeHierarchyAdapter(MessageChannel.class,
                            new MessageChannelSerializers().getSimpleSerializer())
                    .registerTypeHierarchyAdapter(Guild.class,
                            new GuildSerializers().getSimpleSerializer())
                    .registerTypeHierarchyAdapter(Message.class,
                            new MessageSerializers().getSimpleSerializer())
                    .create();

    @Override
    public void run() {
        Thread thread = new Thread(THREAD_NAME) {
            @Override
            public void run() {
                Bot.MESSAGE_LIST_HANDLER.syncedRunnable(() -> {
                    String oldLogsRaw;
                    JsonArray logsJson;
                    if (Files.notExists(LOG_PATH)) {
                        try {
                            Files.createDirectories(LOG_PATH.getParent());
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
                        logsJson = GSON.fromJson(oldLogsRaw, JsonArray.class);
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                        return;
                    }
                    for (Message message : Bot.MESSAGE_LIST_HANDLER.MESSAGE_LIST) {
                        JsonElement jsonTree = GSON.toJsonTree(message);
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
                        Files.writeString(LOG_PATH, GSON.toJson(logsJson));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    Bot.MESSAGE_LIST_HANDLER.MESSAGE_LIST.clear();
                });
            }
        };
        thread.start();
    }
}
