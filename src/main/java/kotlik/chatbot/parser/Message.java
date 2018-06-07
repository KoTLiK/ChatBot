package kotlik.chatbot.parser;

import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Message {
    public final static String DELIMITER = "\r\n";
    private final static Logger LOGGER = Logger.getLogger(Message.class.getName());
    private final static Pattern regex = Pattern.compile(Environment.get("bot.message.regexp"));

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

        Message.Builder builder = new Builder(Command.fromString(matcher.group("CMD")))
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
        private List<String> tags = new ArrayList<>();
        private Command command;
        private String nick;
        private String user;
        private String host;
        private List<String> params;
        private String trailing;

        public Builder(final Command command) {
            this.command = command;
        }

        public Builder(final Command command, final String params) {
            this.command = command;
            this.params(params);
        }

        public Builder params(String params) {
            if (params.contains(":")) {
                String[] bigParams = params.split(":");
                this.trailing = bigParams[1];
                params = bigParams[0];
            }

            List<String> middles = Arrays.asList(params.split("\\s+"));
            this.params = middles.stream().filter(middle -> middle.length() > 0).collect(Collectors.toList());

            return this;
        }

        public Builder tags(final String tags) {
            if (tags != null)
                this.tags = new ArrayList<>(Arrays.asList(tags.split("[; ]")));
            return this;
        }

        public Builder nick(final String nick) {
            this.nick = nick;
            return this;
        }

        public Builder user(final String user) {
            this.user = user;
            return this;
        }

        public Builder host(final String host) {
            this.host = host;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
