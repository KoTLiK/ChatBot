package kotlik.chatbot.controller;

import kotlik.chatbot.message.MessageBuilder;
import kotlik.chatbot.message.MessageFormatter;
import kotlik.chatbot.utils.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MessageService extends RunnableService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private Environment userEnvironment;

    public MessageService() {}

    private void setup() {
        this.stop = false;
        this.reconnect = false;
        this.userEnvironment = new Environment("user.properties");
    }

    @Override
    public void run() {
        setup();
        LOGGER.info("Service is prepared and running.");
        try {
            while (!reconnect) {
                reconnect = false;
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
            }
        } catch (IOException e) {
            LOGGER.error("Network IO error!", e);
        }
        LOGGER.info("Service has been stopped.");
    }

    @Override
    public void reloadUserConfig() {
        userEnvironment = new Environment("user.properties");
    }
}
