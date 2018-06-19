package kotlik.chatbot.message;

import org.jetbrains.annotations.NotNull;

public class MessageFormatter {

    @NotNull
    public static String format(@NotNull Message message) {
        if (message.getCommand().equals(Command.UNKNOWN))
            return "";

        final StringBuilder builder = new StringBuilder();
        builder.append(message.getCommand());

        for (String param : message.getParams())
            builder.append(" ").append(param);

        if (message.getTrailing() != null)
            builder.append(" :").append(message.getTrailing());

        return builder.toString() + Message.DELIMITER;
    }
}
