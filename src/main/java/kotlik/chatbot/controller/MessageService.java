package kotlik.chatbot.controller;

import kotlik.chatbot.message.MessageBuilder;
import kotlik.chatbot.message.MessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageService extends RunnableService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public MessageService() {}

    @Override
    protected void login() throws IOException {
        // Login
        client.send(MessageFormatter.format(MessageBuilder.pass(userEnvironment.getValue("user.client.oauth.token"))));
        client.send(MessageFormatter.format(MessageBuilder.nick(userEnvironment.getValue("user.client.username"))));

        // Request Twitch capabilities
        client.send(MessageFormatter.format(MessageBuilder.capabilities("twitch.tv/membership")));
        client.send(MessageFormatter.format(MessageBuilder.capabilities("twitch.tv/tags")));
        client.send(MessageFormatter.format(MessageBuilder.capabilities("twitch.tv/commands")));

        // Join channel
        client.send(MessageFormatter.format(MessageBuilder.join(userEnvironment.getValue("user.client.channel"))));
    }
}
