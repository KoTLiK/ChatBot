package kotlik.chatbot.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Message {
    public final static String DELIMITER = "\r\n";
    private final static Logger LOGGER = Logger.getLogger(Message.class.getName());
    private final static Pattern REGEX = Pattern.compile(Environment.get("bot.message.regexp"));

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

    @Override
    public String toString() {
        final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, "Unable to serialize message!");
            return "{\n\t\"error\": \"Message serialization failed!\"\n}\n";
        }
    }

    @NotNull
    public static Message parse(final String message) {
        Matcher matcher = REGEX.matcher(message);
        if (!matcher.find()) return new Message.Builder(Command.UNKNOWN).build();

        Message.Builder builder = new Builder(Command.fromString(matcher.group("CMD")))
                  .params(matcher.group("PARAMS"))
                  .tags(matcher.group("TAGS"))
                  .nick(matcher.group("NICK"))
                  .user(matcher.group("USER"))
                  .host(matcher.group("HOST"));

        return builder.build();
    }

    @NotNull
    public static String prepare(@NotNull final Message message) {
        final BuildString messageBuilder = new BuildString();
        switch (message.getCommand()) {
            case PONG:
                messageBuilder.command(message.getCommand())
                        .trailing(true, message.getTrailing());
                break;
            case PASS:
                messageBuilder.command(message.getCommand())
                        .trailing(false, message.getTrailing());
                break;
            case NICK:
                messageBuilder.command(message.getCommand())
                        .trailing(false, message.getTrailing());
                break;
            case CAP:
                messageBuilder.command(message.getCommand())
                        .param(message.getParams().get(0))
                        .trailing(true, message.getTrailing());
                break;
            case JOIN:
                messageBuilder.command(message.getCommand())
                        .param(message.getParams().get(0));
                break;
            default:
                return "";
        }
        return messageBuilder.toString() + DELIMITER;
    }

    public static Message _pass(final String token) {
        return new Message.Builder(Command.PASS).trailing(Environment.get("bot.client.oauth.prefix") + token).build();
    }

    public static Message _nick(final String username) {
        return new Message.Builder(Command.NICK).trailing(username).build();
    }

    public static Message _capabilities(final String capabilities) {
        return new Message.Builder(Command.CAP).params("REQ").trailing(capabilities).build();
    }

    public static Message _join(final String channel) {
        return new Message.Builder(Command.JOIN).params("#" + channel).build();
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
                final int index = params.indexOf(":");
                this.trailing = params.substring(index + 1);
                params = params.substring(0, index);
            }

            List<String> middles = Arrays.asList(params.split("\\s+"));
            this.params = middles.stream().filter(middle -> middle.length() > 0).collect(Collectors.toList());

            return this;
        }

        public Builder trailing(final String trailing) {
            this.trailing = trailing;
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

    public static class BuildString {
        private final StringBuilder message = new StringBuilder();

        public BuildString() {}

        public BuildString command(final Command command) {
            message.append(command);
            return this;
        }

        public BuildString param(final String param) {
            message.append(" ");
            message.append(param);
            return this;
        }

        public BuildString trailing(boolean colon, final String trailing) {
            if (colon)
                message.append(" :");
            else message.append(" ");
            message.append(trailing);
            return this;
        }

        @Override
        public String toString() {
            return message.toString();
        }
    }
}
