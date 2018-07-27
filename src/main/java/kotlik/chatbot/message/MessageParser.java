package kotlik.chatbot.message;

import kotlik.chatbot.utils.Environment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    private final static Pattern REGEX = Pattern.compile(Environment.get("bot.message.regexp"));

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
                .rawMessage(message)
                .build();
    }

    public static Message parseNoRegexp(final String message) {
        String tmpMsg = message;

        // Twitch TAGS
        String rawTags = null;
        if (tmpMsg.startsWith("@")) {
            final int index = tmpMsg.indexOf(" ");
            rawTags = tmpMsg.substring(1, index);
            tmpMsg = tmpMsg.substring(index + 1);
        }

        // IRC Prefix
        String prefix = null;
        if (tmpMsg.startsWith(":")) {
            final int index = tmpMsg.indexOf(" ");
            prefix = tmpMsg.substring(1, index);
            tmpMsg = tmpMsg.substring(index + 1);
        }

        // Command & Params
        // TODO IRC Protocol says Params is at least SPACE character; Needs to be checked IRL
        // TODO Then we do not have to worry about non-existence of the SPACE delimiter for the Command and Params
        MessageBuilder builder;
        if (tmpMsg.contains(" ")) {
            final int index = tmpMsg.indexOf(" ");
            builder = MessageBuilder.command(Command.fromString(tmpMsg.substring(0, index)));
            addParams(builder, tmpMsg.substring(index + 1));
        } else builder = MessageBuilder.command(Command.fromString(tmpMsg));

        addTags(builder, rawTags);
        addPrefix(builder, prefix);

        return builder.rawMessage(message).build();
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

    public static MessageBuilder addTags(MessageBuilder builder, final String tags) {
        if (tags == null)
            return builder;

        final Map<String, String[]> tagMap = new HashMap<>();
        for (String tag : tags.split("[; ]")) {
            final String[] parts = tag.split("=");
            tagMap.put(parts[0], parts[1].split(","));
        }

        return builder.withTags(tagMap);
    }

    private static MessageBuilder addPrefix(MessageBuilder builder, String prefix) {
        // Parsing prefix -> Nick !User(optional) @Host(optional)
        if (prefix != null) {
            if (prefix.contains("!")) {
                final int indexNick = prefix.indexOf("!");
                builder.withNick(prefix.substring(0, indexNick));
                prefix = prefix.substring(indexNick + 1);
                if (prefix.contains("@")) {
                    final int index = prefix.indexOf("@");
                    builder.withUser(prefix.substring(0, index));
                    builder.withHost(prefix.substring(index + 1));
                }
            } else builder.withNick(prefix);
        }
        return builder;
    }
}
