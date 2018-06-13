package kotlik.chatbot.service;

import kotlik.chatbot.client.Client;
import kotlik.chatbot.parser.Command;
import kotlik.chatbot.parser.Message;
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

            // Login
//            client.send(Message.prepare(Message._pass(userEnvironment.getValue("user.client.oauth.token"))));
//            client.send(Message.prepare(Message._nick(userEnvironment.getValue("user.client.username"))));

            final String nickname = userEnvironment.getValue("user.client.username");
            client.send(Message.prepare(Message._nick(nickname)));
            client.send("USER " + nickname + " " + nickname + " " + nickname + " :" + nickname + Message.DELIMITER);

            // Request Twitch capabilities
//            client.send(Message.prepare(Message._capabilities("twitch.tv/membership")));
//            client.send(Message.prepare(Message._capabilities("twitch.tv/tags")));
//            client.send(Message.prepare(Message._capabilities("twitch.tv/commands")));

            // Join channel
            client.send(Message.prepare(Message._join(userEnvironment.getValue("user.client.channel"))));

            loop();

            client.stop();
        } catch (IOException e) {
            e.printStackTrace();
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
                break;
            }
            message = Message.parse(rawMessage);
            message = serve(message);
            client.send(Message.prepare(message));
        }
    }

    // TODO serving method
    private Message serve(@NotNull Message message) {
        switch (message.getCommand()) {
            case PING:
                return new Message.Builder(Command.PONG).trailing(message.getTrailing()).build();
            default:
                return new Message.Builder(Command.UNKNOWN).build();
        }
    }
}
