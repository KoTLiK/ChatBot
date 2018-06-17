package kotlik.chatbot.controller;

import kotlik.chatbot.message.Message;
import kotlik.chatbot.utils.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


final public class FreeMessageService extends RunnableService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public FreeMessageService() {
        LOGGER.info("Service is prepared.");
    }

    @Override
    public void run() {
        stop = false;
        Environment userEnvironment = new Environment("user.properties");
        LOGGER.info("Service is running.");
        try {
            client.start();

            final String nickname = userEnvironment.getValue("user.client.username");
            client.send(Message.nick(nickname).toString());
            client.send("USER " + nickname + " " + nickname + " " + nickname + " :" + nickname + Message.DELIMITER);

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
