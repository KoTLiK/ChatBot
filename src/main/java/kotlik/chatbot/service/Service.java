package kotlik.chatbot.service;

import kotlik.chatbot.client.Client;
import kotlik.chatbot.parser.Command;
import kotlik.chatbot.parser.Message;
import kotlik.chatbot.utils.Environment;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Service implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(Service.class.getName());
    private final Client client;
    private final Environment userEnvironment;
    private final boolean authentication;
    private boolean stop = false;

    public Service(boolean authentication) {
        this.client = new Client(Environment.get("bot.twitch.url"),
                Integer.parseInt(Environment.get("bot.twitch.port")));
        this.userEnvironment = new Environment("user.properties");
        this.authentication = authentication;
        LOGGER.log(Level.INFO, "Service is prepared.");
    }

    @Override
    public void run() {
        try {
            LOGGER.log(Level.INFO, "Service is running.");
            client.start();

            if (authentication) {
                client.send(Message.prepare(Message._pass(userEnvironment.getValue("user.client.oauth.token"))));
                client.send(Message.prepare(Message._nick(userEnvironment.getValue("user.client.username"))));
            }

            /**
             * Testing lines
             */
            final String nickname = userEnvironment.getValue("user.client.username");
            client.send(Message.prepare(Message._nick(nickname)));
            client.send("USER " + nickname + " " + nickname + " " + nickname + " :" + nickname + Message.DELIMITER);
//            client.send(Message.prepare(Message._capabilities("twitch.tv/membership")));
//            client.send(Message.prepare(Message._capabilities("twitch.tv/tags")));
//            client.send(Message.prepare(Message._capabilities("twitch.tv/commands")));

            client.send(Message.prepare(Message._join(userEnvironment.getValue("user.client.channel"))));

            Message message;
            while (!stop) {
                message = Message.parse(client.receive());
                message = serve(message);
                client.send(Message.prepare(message));
            }

            client.stop();
            LOGGER.log(Level.INFO, "Service has been stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stop = true;
    }

    // TODO serving method
    private Message serve(Message message) {
        switch (message.getCommand()) {
            case PING:
                return new Message.Builder(Command.PONG).trailing(message.getTrailing()).build();
            default:
                return new Message.Builder(Command.UNKNOWN).build();
        }
    }
}
