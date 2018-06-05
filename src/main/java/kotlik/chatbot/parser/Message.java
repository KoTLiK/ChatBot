package kotlik.chatbot.parser;

import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Message {
    public final static String DELIMITER = "\r\n";
    private final static Logger LOGGER = Logger.getLogger(Message.class.getName());
    private final static Pattern regex = Pattern.compile(Environment.getProperty("bot.message.regexp"));

    private List<String> tags;
    private Command command;
    private String nick;
    private String user;
    private String host;
    private List<String> params;
    private String trailing;

    private Message(@NotNull Message.Builder builder) {
        this.tags = builder.tags;
        this.command = builder.command;
        this.nick = builder.nick;
        this.user = builder.user;
        this.host = builder.host;
        this.params = builder.params;
        this.trailing = builder.trailing;
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

    @NotNull
    public static Message parse(final String message) {
        Matcher matcher = regex.matcher(message);
        if (!matcher.find()) return new Message.Builder(Command.UNKNOWN).build();

        Message.Builder builder = new Message.Builder();
        builder.command(Command.fromString(matcher.group("CMD")))
                .params(matcher.group("PARAMS"))
                .tags(matcher.group("TAGS"))
                .nick(matcher.group("NICK"))
                .user(matcher.group("USER"))
                .host(matcher.group("HOST"));

        return builder.build();
    }

    public static String prepare(final Message message) {
        return null;
    }

    public static class Builder {
        private List<String> tags;
        private Command command;
        private String nick;
        private String user;
        private String host;
        private List<String> params;
        private String trailing;

        public Builder() {}

        public Builder(Command command) {
            this.command = command;
        }

        public Builder(Command command, String params) {
            this.command = command;
            // TODO process params and trailing
        }

        public Builder command(Command command) {
            this.command = command;
            return this;
        }

        public Builder params(String params) {
            // TODO process params and trailing
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder tags(String tags) {
            // TODO process tags
            return this;
        }

        public Builder nick(String nick) {
            this.nick = nick;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
