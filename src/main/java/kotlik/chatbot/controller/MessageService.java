package kotlik.chatbot.controller;

import kotlik.chatbot.message.Message;
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
        Environment userEnvironment = new Environment("user.properties");
        LOGGER.info("Service is running.");
        try {
            client.start();

            // Login
            client.send(Message.pass(userEnvironment.getValue("user.client.oauth.token")).toString());
            client.send(Message.nick(userEnvironment.getValue("user.client.username")).toString());

            // Request Twitch capabilities
            client.send(Message.capabilities("twitch.tv/membership").toString());
            client.send(Message.capabilities("twitch.tv/tags").toString());
            client.send(Message.capabilities("twitch.tv/commands").toString());

            // Join channel
            client.send(Message.join(userEnvironment.getValue("user.client.channel")).toString());

            loop();

            client.stop();
        } catch (IOException e) {
            LOGGER.error("Network IO error!", e);
        }
        LOGGER.info("Service has been stopped.");
    }
}
