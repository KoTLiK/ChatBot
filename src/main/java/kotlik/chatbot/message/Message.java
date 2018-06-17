package kotlik.chatbot.message;

import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private boolean colon = false;

    public Message() {}

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

    public Message setTags(final List<String> tags) {
        this.tags = tags;
        return this;
    }

    public Message setNick(final String nick) {
        this.nick = nick;
        return this;
    }

    public Message setUser(final String user) {
        this.user = user;
        return this;
    }

    public Message setHost(final String host) {
        this.host = host;
        return this;
    }

    public Message setCommand(final Command command) {
        this.command = command;
        return this;
    }

    public Message setParams(final List<String> params) {
        this.params = params;
        return this;
    }

    public Message setTrailing(boolean colon, final String trailing) {
        this.colon = colon;
        this.trailing = trailing;
        return this;
    }

    public Message setTrailing(final String trailing) {
        setTrailing(true, trailing);
        return this;
    }

    @Override
    public String toString() {
        if (command.equals(Command.UNKNOWN))
            return "";

        final StringBuilder builder = new StringBuilder();
        builder.append(command);

        for (String param : params)
            builder.append(" ").append(param);

        if (trailing != null) {
            if (colon) builder.append(" :").append(trailing);
            else builder.append(" ").append(trailing);
        }

        return builder.toString() + DELIMITER;
    }

    @NotNull
    public static Message parse(final String message) {
        final Matcher matcher = REGEX.matcher(message);
        if (!matcher.find()) return MessageBuilder.build(Command.UNKNOWN, "");

        return MessageBuilder.build(Command.fromString(matcher.group("CMD")),
                                    matcher.group("PARAMS"),
                                    matcher.group("TAGS"),
                                    matcher.group("NICK"),
                                    matcher.group("USER"),
                                    matcher.group("HOST"));
    }

    public static Message pass(final String token) {
        return MessageBuilder.build(Command.PASS, "")
                       .setTrailing(false, Environment.get("bot.client.oauth.prefix") + token);
    }

    public static Message nick(final String username) {
        return MessageBuilder.build(Command.NICK, "")
                       .setTrailing(false, username);
    }

    public static Message capabilities(final String capabilities) {
        return MessageBuilder.build(Command.CAP, "REQ")
                       .setTrailing(capabilities);
    }

    public static Message join(final String channel) {
        return MessageBuilder.build(Command.JOIN, "#" + channel);
    }
}
