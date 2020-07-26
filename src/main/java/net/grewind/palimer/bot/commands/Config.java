package net.grewind.palimer.bot.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.Message;
import net.grewind.palimer.bot.config.GuildConfig;
import net.grewind.palimer.bot.utils.FilePaths;
import net.grewind.palimer.bot.utils.MentionUtils;
import net.grewind.palimer.bot.utils.UserUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

public class Config extends CommandExecutor {
    // TODO: make remove work without a mention attached
    public static final String ROOT = "config";
    private static final Path GUILD_CONFIG_PATH = FilePaths.CONFIG_DIR_PATH.resolve("guildConfig.json");

    protected Config(Message message, Command command) {
        super(message, command);
    }

    @Override
    public boolean execute() {
        if (!UserUtils.isBotAdmin(message.getAuthor())) {
            return false;
        }
        if (!message.isFromGuild()) {
            return false;
        }
        if (command.getCrownBranches().length < 3) {
            return false;
        }
        String configName = command.getCrownBranch(0);
        Action action = Action.valueOf(command.getCrownBranch(1));
        String mentionString = command.getCrownBranch(2);
        Message.MentionType mentionType = MentionUtils.getMentionType(mentionString);
        if (mentionType == null) {
            return false;
        }
        if ("everyone".equals(mentionType.getParseKey())) {
            return false;
        }
        String mentionTypeString = mentionType.toString();
        long mentionId;
        Matcher matcher = mentionType.getPattern().matcher(mentionString);
        if (matcher.find()) {
            switch (mentionType) {
                case USER:
                case ROLE:
                case CHANNEL:
                    mentionId = Long.parseUnsignedLong(matcher.group(1));
                    break;
                case EMOTE:
                    mentionId = Long.parseUnsignedLong(matcher.group(2));
                    break;
                default:
                    return false;
            }
        } else {
            return false;
        }
        if (!Files.exists(GUILD_CONFIG_PATH)) {
            try {
                Files.createDirectories(GUILD_CONFIG_PATH.getParent());
                Files.createFile(GUILD_CONFIG_PATH);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return writeConfigToFile(new GuildConfig.Config(configName, GuildConfig.Config.Type.valueOf(mentionTypeString),
                mentionId), action);
    }

    private boolean writeConfigToFile(GuildConfig.Config config, Action action) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(GuildConfig.class, new GuildConfig.GuildConfigDeserializer())
                .registerTypeAdapter(GuildConfig.class, new GuildConfig.GuildConfigSerializer())
                .create();
        FileReader reader;
        try {
            reader = new FileReader(GUILD_CONFIG_PATH.toFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        Set<GuildConfig> guildConfigs = gson.fromJson(reader, new TypeToken<Set<GuildConfig>>() {
        }.getType());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (guildConfigs == null) {
            guildConfigs = new HashSet<>();
        }

        GuildConfig guildConfig = null;
        for (GuildConfig guildConfigIteration : guildConfigs) {
            if (guildConfigIteration.getGuildId() == message.getGuild().getIdLong()) {
                guildConfig = guildConfigIteration;
            }
        }
        Set<GuildConfig.Config> configs = new HashSet<>();
        if (guildConfig == null) {
            guildConfig = new GuildConfig(message.getGuild().getIdLong(), configs);
            guildConfigs.add(guildConfig);
        }
        switch (action) {
            case add -> guildConfig.getConfigs().add(config);
            case remove -> guildConfig.getConfigs().remove(config);
            case replace -> {
                guildConfig.getConfigs().remove(config);
                guildConfig.getConfigs().add(config);
            }
        }
        FileWriter writer;
        try {
            writer = new FileWriter(GUILD_CONFIG_PATH.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            gson.toJson(guildConfigs, new TypeToken<Set<GuildConfig>>() {
            }.getType(), writer);
        } catch (JsonIOException e) {
            e.printStackTrace();
            try {
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
            return false;
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private enum Action {
        add,
        remove,
        replace
    }
}
