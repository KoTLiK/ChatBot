package kotlik.chatbot.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    public final static String DELIMITER = "\r\n";
    private final static Logger LOGGER = LoggerFactory.getLogger(Message.class);
    private final static Pattern REGEX = Pattern.compile(Environment.get("bot.message.regexp"));

    private List<String> tags;
    private String nick;
    private String user;
    private String host;
    private Command command;
    private List<String> params;
    private String trailing;

    public Message(List<String> tags, String nick, String user, String host, Command command, List<String> params, String trailing) {
        this.tags = tags;
        this.nick = nick;
        this.user = user;
        this.host = host;
        this.command = command;
        this.params = params;
        this.trailing = trailing;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getNick() {
        return nick;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public Command getCommand() {
        return command;
    }

    public List<String> getParams() {
        return params;
    }

    public String getTrailing() {
        return trailing;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Unable to serialize message!", e);
        }
        return "{\n\t\"error:\" \"Unable to serialize message!\"}\n";
    }

    // TODO: move next 3 methods to extra parser class

    @NotNull
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

    private static MessageBuilder addParams(@NotNull MessageBuilder builder, String paramsMessagePart) {
        if (paramsMessagePart == null) return null;

        if (paramsMessagePart.contains(":")) {
            final int index = paramsMessagePart.indexOf(":");
            builder.withTrailing(paramsMessagePart.substring(index + 1));
            paramsMessagePart = paramsMessagePart.substring(0, index);
        }

        return builder.withParams(Arrays.stream(paramsMessagePart.split("\\s+"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new));
    }

    private static MessageBuilder addTags(@NotNull MessageBuilder builder, final String tags) {
        if (tags == null) return null;

        return builder.withTags(tags.split("[; ]"));
    }
}
