package kotlik.chatbot.message;

import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageBuilder {

    private Command command;
    private String[] params;
    private String[] tags;
    private String nick;
    private String username;
    private String host;
    private String trailing;

    @NotNull
    public static MessageBuilder command(Command command) {
        return new MessageBuilder(command);
    }

    private MessageBuilder(Command command) {
        this.command = command;
    }

    public MessageBuilder withParams(String... params) {
        this.params = params;
        return this;
    }

    public MessageBuilder withTags(String... tags) {
        this.tags = tags;
        return this;
    }

    public MessageBuilder withNick(String nick) {
        this.nick = nick;
        return this;
    }

    public MessageBuilder withUser(String username) {
        this.username = username;
        return this;
    }

    public MessageBuilder withHost(String host) {
        this.host = host;
        return this;
    }

    public MessageBuilder withTrailing(String trailing) {
        this.trailing = trailing;
        return this;
    }

    public Message build() {
        return new Message(
                tags == null ? new ArrayList<>() : Arrays.asList(tags),
                nick,
                username,
                host,
                command,
                params == null ? new ArrayList<>() : Arrays.asList(params),
                trailing
        );
    }

    public static Message pass(final String token) {
        return MessageBuilder.command(Command.PASS)
                .withParams(Environment.get("bot.client.oauth.prefix") + token)
                .build();
    }

    public static Message nick(final String username) {
        return MessageBuilder.command(Command.NICK)
                .withParams(username)
                .build();
    }

    public static Message capabilities(final String capabilities) {
        return MessageBuilder.command(Command.CAP)
                .withParams("REQ")
                .withTrailing(capabilities)
                .build();
    }

    public static Message join(final String channel) {
        return MessageBuilder.command(Command.JOIN)
                .withParams("#" + channel)
                .build();
    }
}
