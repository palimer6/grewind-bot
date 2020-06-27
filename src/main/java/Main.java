import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import sensitiveinfo.ApiKeys;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT)
                .setToken(ApiKeys.TOKEN)
                .addEventListener(new Main());
        builder.buildAsync();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!isCommand(event)) {
            return;
        }
        if (botCheck(event)) {
            return;
        }
        String rawCommand = event.getMessage().getContentRaw();
        String commandContent = rawCommand.replaceFirst("!", "");
        List<String> commandParts = Arrays.stream(commandContent.split("\\s"))
                .filter(s -> !s.equals(""))
                .collect(Collectors.toList());
        String commandSubContent = commandContent.replaceFirst(commandParts.get(0) + "\\s", "");
        System.out.printf("command from %#s: %s%n",
                event.getAuthor(),
                rawCommand);
        if (commandParts.get(0).equals("ping")) {
            sendMessage(event, "Pong!");
        }
        if (commandParts.get(0).equals("say")) {
            sendMessage(event, commandSubContent);
        }
    }

    private boolean isCommand(@NotNull MessageReceivedEvent event) {
        return event.getMessage().getContentRaw().matches("!.*");
    }

    private void sendMessage(@NotNull MessageReceivedEvent event, String message) {
        System.out.printf("sending message in channel %s: %s%n",
                event.getChannel().getName(), message);
        event.getChannel().sendMessage(message).queue();
    }

    private boolean botCheck(@NotNull MessageReceivedEvent event) {
        boolean isBot = event.getAuthor().isBot();
        if ((!event.getAuthor().getName().equals("Grewind Bot") ||
                !event.getAuthor().getDiscriminator().equals("6790"))
                && isBot) {
            System.err.printf("command from bot %#s: %s%n",
                    event.getAuthor(),
                    event.getMessage().getContentRaw());
        }
        return isBot;
    }
}
