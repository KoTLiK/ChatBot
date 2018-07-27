package kotlik.chatbot.message;

import kotlik.chatbot.utils.ParametricString;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

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

        if (!message.getTags().isEmpty())
            builder.append(sortAndFormatTags(message.getTags()));

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

    @NotNull
    private static String sortAndFormatTags(@NotNull Map<String, String[]> tagMap) {
        final StringBuilder builder = new StringBuilder("@");
        String[] keyArray = (String[]) tagMap.keySet().toArray();
        Arrays.sort(keyArray);
        for (final String key : keyArray) {
            final StringBuilder tagBuilder = new StringBuilder();
            for (final String value : tagMap.get(key))
                tagBuilder.append(value).append(",");
            tagBuilder.deleteCharAt(tagBuilder.lastIndexOf(","));

            builder.append(ParametricString.resolve("{0}={1}", key, tagBuilder.toString())).append(";");
        }
        builder.deleteCharAt(builder.lastIndexOf(";"));
        builder.append(" ");
        return builder.toString();
    }
}
