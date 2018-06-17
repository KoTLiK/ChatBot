package kotlik.chatbot.controller;

import kotlik.chatbot.Bot;
import kotlik.chatbot.annotations.Commander;
import kotlik.chatbot.annotations.TargetCommand;
import kotlik.chatbot.client.Client;
import kotlik.chatbot.parser.Command;
import kotlik.chatbot.parser.Message;
import kotlik.chatbot.parser.MessageBuilder;
import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


public class MessageService implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final Client client;
    private boolean stop;
    private Map<Command, Method> commandMethods;

    public MessageService() {
        this.client = new Client(Environment.get("bot.twitch.url"),
                Integer.parseInt(Environment.get("bot.twitch.port")));
        LOGGER.info("Service is prepared.");
    }

    private void setup() {
        Class<CommandController> commander = CommandController.class;
        if (commander.isAnnotationPresent(Commander.class)) {
            for (Method method : commander.getDeclaredMethods()) {
                if (method.isAnnotationPresent(TargetCommand.class)) {
                    TargetCommand annotatedCommand = method.getAnnotation(TargetCommand.class);
                    commandMethods.put(annotatedCommand.value(), method);
                }
            }
        } else throw new RuntimeException("Commander is not found.");
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
            client.send(Message.pass(userEnvironment.getValue("user.client.oauth.token")).toString());
            client.send(Message.nick(userEnvironment.getValue("user.client.username")).toString());

            // Request Twitch capabilities
            client.send(Message.capabilities("twitch.tv/membership").toString());
            client.send(Message.capabilities("twitch.tv/tags").toString());
            client.send(Message.capabilities("twitch.tv/commands").toString());
*/

            final String nickname = userEnvironment.getValue("user.client.username");
            client.send(Message.nick(nickname).toString());
            client.send("USER " + nickname + " " + nickname + " " + nickname + " :" + nickname + Message.DELIMITER);

            // Join channel
            client.send(Message.join(userEnvironment.getValue("user.client.channel")).toString());

            loop();

            client.stop();
        } catch (IOException e) {
            LOGGER.error(Bot.exceptionToString(e));
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
        try {
            return (Message) commandMethods.get(message.getCommand()).invoke(CommandController.class.newInstance(), message);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
