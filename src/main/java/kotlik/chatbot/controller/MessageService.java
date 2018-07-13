package kotlik.chatbot.controller;

import kotlik.chatbot.message.Command;
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
        try {
            client.send(MessageFormatter.format(MessageBuilder.command(Command.PART)
                    .withParams("#" + userEnvironment.getValue("user.client.channel")).build()));
            userEnvironment.setProperty("user.client.channel", channel);
            client.send(MessageFormatter.format(MessageBuilder.join(channel)));
        } catch (IOException e) {
            LOGGER.error("Network IO error!", e);
        }
    }
}
