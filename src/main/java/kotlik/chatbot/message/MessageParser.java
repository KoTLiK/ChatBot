package kotlik.chatbot.message;

import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    private final static Pattern REGEX = Pattern.compile(Environment.get("bot.message.regexp"));
    private final static Pattern REGEX_NO_TAGS = Pattern.compile(Environment.get("bot.message.no.tags.regexp"));

    public static Message parse(final String message) {
        final Matcher matcher = REGEX.matcher(message);
        if (!matcher.find()) return MessageBuilder.command(Command.UNKNOWN).build();

        MessageBuilder builder = MessageBuilder.command(Command.fromString(matcher.group("CMD")));
        addParams(builder, matcher.group("PARAMS"));
        addTags(builder, matcher.group("TAGS"));
        return builder
                .withNick(matcher.group("NICK"))
                .withUser(matcher.group("USER"))
                .withHost(matcher.group("HOST"))
                .build();
    }

    public static Message parseShorterRegexp(@NotNull String message) {
        String rawTags = null;
        if (message.startsWith("@")) {
            final int index = message.indexOf(" ");
            rawTags = message.substring(1, index);
            message = message.substring(index + 1);
        }

        final Matcher matcher = REGEX_NO_TAGS.matcher(message);
        if (!matcher.find()) return MessageBuilder.command(Command.UNKNOWN).build();

        MessageBuilder builder = MessageBuilder.command(Command.fromString(matcher.group("CMD")));
        addParams(builder, matcher.group("PARAMS"));
        addTags(builder, rawTags);
        return builder
                .withNick(matcher.group("NICK"))
                .withUser(matcher.group("USER"))
                .withHost(matcher.group("HOST"))
                .build();
    }

    private static MessageBuilder addParams(MessageBuilder builder, String paramsMessagePart) {
        if (paramsMessagePart == null)
            return builder;

        if (paramsMessagePart.contains(":")) {
            final int index = paramsMessagePart.indexOf(":");
            builder.withTrailing(paramsMessagePart.substring(index + 1));
            paramsMessagePart = paramsMessagePart.substring(0, index);
        }

        return builder.withParams(Arrays.stream(paramsMessagePart.split("\\s+"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new));
    }

    private static MessageBuilder addTags(MessageBuilder builder, final String tags) {
        if (tags == null)
            return builder;

        return builder.withTags(tags.split("[; ]"));
    }
}
