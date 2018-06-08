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

            // TODO Log in
            if (authentication) {
                client.send(Message.prepare(_pass()));
                client.send(Message.prepare(_nick()));
            }

            // TODO infinite loop
            while (!stop) {

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

    private Message _pass() {
        return new Message.Builder(Command.PASS)
                       .trailing(Environment.get("bot.client.oauth.prefix")
                                 + userEnvironment.getValue("user.client.oauth.token"))
                       .build();
    }

    private Message _nick() {
        return new Message.Builder(Command.NICK)
                       .trailing(userEnvironment.getValue("user.client.username"))
                       .build();
    }

    // TODO serving method
}
