package kotlik.chatbot.service;

import kotlik.chatbot.client.Client;
import kotlik.chatbot.parser.Command;
import kotlik.chatbot.parser.Message;
import kotlik.chatbot.parser.MessageBuilder;
import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class Service implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(Service.class);
    private final Client client;
    private boolean stop;

    public Service() {
        this.client = new Client(Environment.get("bot.twitch.url"),
                Integer.parseInt(Environment.get("bot.twitch.port")));
        LOGGER.info("Service is prepared.");
    }

    @Override
    public void run() {
        stop = false;
        Environment userEnvironment = new Environment("user.properties");
        LOGGER.info("Service is running.");
        try {
            client.start();
/*
            // Login
            client.send(Message._pass(userEnvironment.getValue("user.client.oauth.token")).toString());
            client.send(Message._nick(userEnvironment.getValue("user.client.username")).toString());

            // Request Twitch capabilities
            client.send(Message._capabilities("twitch.tv/membership").toString());
            client.send(Message._capabilities("twitch.tv/tags").toString());
            client.send(Message._capabilities("twitch.tv/commands").toString());
*/

            final String nickname = userEnvironment.getValue("user.client.username");
            client.send(Message._nick(nickname).toString());
            client.send("USER " + nickname + " " + nickname + " " + nickname + " :" + nickname + Message.DELIMITER);

            // Join channel
            client.send(Message._join(userEnvironment.getValue("user.client.channel")).toString());

            loop();

            client.stop();
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
        LOGGER.info("Service has been stopped.");
    }

    public void stop() {
        this.stop = true;
    }

    private void loop() throws IOException {
        Message message;
        String rawMessage;
        while (!stop) {
            rawMessage = client.receive();
            if (rawMessage == null) {
                // TODO handle 'End of stream' ???
                break;
            }
            message = Message.parse(rawMessage);
            message = serve(message);
            client.send(message.toString());
        }
    }

    // TODO serving method
    private Message serve(@NotNull Message message) {
        switch (message.getCommand()) {
            case PING:
                return MessageBuilder.build(Command.PONG, "").setTrailing(message.getTrailing());
            default:
                return MessageBuilder.build(Command.UNKNOWN, "");
        }
    }
}
