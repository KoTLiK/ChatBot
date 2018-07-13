package kotlik.chatbot.controller;

import kotlik.chatbot.message.Message;
import kotlik.chatbot.message.MessageBuilder;
import kotlik.chatbot.message.MessageFormatter;
import kotlik.chatbot.utils.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

final public class FreeMessageService extends RunnableService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private Environment userEnvironment;

    public FreeMessageService() {}

    private void setup() {
        stop.set(false);
        reconnect.set(false);
        userEnvironment = new Environment("user.properties");
    }

    @Override
    public void run() {
        setup();
        LOGGER.info("Service is prepared and running.");
        try {
            do {
                client.start();

                // Login
                final String nickname = userEnvironment.getValue("user.client.username");
                client.send(MessageFormatter.format(MessageBuilder.nick(nickname)));
                client.send("USER " + nickname + " " + nickname + " " + nickname + " :" + nickname + Message.DELIMITER);

                // Join channel
                client.send(MessageFormatter.format(MessageBuilder.join(userEnvironment.getValue("user.client.channel"))));

                loop();

                client.stop();
                if (stop.get()) break;
            } while (reconnect.getAndSet(false));
        } catch (IOException e) {
            LOGGER.error("Network IO error!", e);
        }
        LOGGER.info("Service has been stopped.");
    }

    @Deprecated
    @Override
    public void reloadUserConfig() {
        final Environment environment = new Environment("user.properties");
        if (environment.reloadProperties())
            userEnvironment = environment;
    }

    @Override
    public void changeChannel(String channel) {
        userEnvironment.setProperty("user.client.channel", channel);
    }
}

