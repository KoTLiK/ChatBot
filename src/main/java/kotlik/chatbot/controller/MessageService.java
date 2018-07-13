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
        stop = false;
        reconnect = false;
        userEnvironment = new Environment("user.properties");
        if (!userEnvironment.loadProperties())
            throw new RuntimeException("Unable to load property file!");
    }

    @Override
    public void run() {
        setup();
        LOGGER.info("Service is prepared and running.");
        try {
            do {
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
                if (stop) break;
            } while (reconnect);
        } catch (IOException e) {
            LOGGER.error("Network IO error!", e);
        }
        LOGGER.info("Service has been stopped.");
    }

    @Override
    public void reloadUserConfig() {
        final Environment environment = new Environment("user.properties");
        if (environment.loadProperties())
            userEnvironment = environment;
    }
}
