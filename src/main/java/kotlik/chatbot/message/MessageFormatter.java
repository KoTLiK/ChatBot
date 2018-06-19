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

    @NotNull
    public static String fullFormat(@NotNull Message message) {
        if (message.getCommand().equals(Command.UNKNOWN))
            return "";

        final StringBuilder builder = new StringBuilder();

        if (!message.getTags().isEmpty()) {
            builder.append("@");
            for (String tag : message.getTags())
                builder.append(tag).append(";");
            builder.deleteCharAt(builder.lastIndexOf(";"));
            builder.append(" ");
        }

        if (message.getNick() != null) {
            builder.append(":").append(message.getNick());

            if (message.getUser() != null)
                builder.append("!").append(message.getUser());

            if (message.getHost() != null)
                builder.append("@").append(message.getHost());

            builder.append(" ");
        }

        return builder.toString() + format(message);
    }
}
