package kotlik.chatbot.controller;

import kotlik.chatbot.message.MessageBuilder;
import kotlik.chatbot.message.MessageFormatter;
import kotlik.chatbot.utils.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageService extends RunnableService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public MessageService() {
        LOGGER.info("Service is prepared.");
    }

    @Override
    public void run() {
        stop = false;
        userEnvironment = new Environment("user.properties");
        LOGGER.info("Service is running.");
        try {
            client.start();

            // Login
            client.send(MessageFormatter.format(MessageBuilder.pass(userEnvironment.getValue("user.client.oauth.token"))));
            client.send(MessageFormatter.format(MessageBuilder.nick(userEnvironment.getValue("user.client.username"))));

            // Request Twitch capabilities
            client.send(MessageFormatter.format(MessageBuilder.capabilities("twitch.tv/membership")));
            client.send(MessageFormatter.format(MessageBuilder.capabilities("twitch.tv/tags")));
            client.send(MessageFormatter.format(MessageBuilder.capabilities("twitch.tv/commands")));

            // Join channel
            client.send(MessageFormatter.format(MessageBuilder.join(userEnvironment.getValue("user.client.channel"))));

            loop();

            client.stop();
        } catch (IOException e) {
            LOGGER.error("Network IO error!", e);
        }
        LOGGER.info("Service has been stopped.");
    }
}
