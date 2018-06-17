package kotlik.chatbot.controller;

import kotlik.chatbot.annotations.Commander;
import kotlik.chatbot.annotations.TargetCommand;
import kotlik.chatbot.message.Command;
import kotlik.chatbot.message.Message;
import kotlik.chatbot.message.MessageBuilder;
import kotlik.chatbot.network.client.Client;
import kotlik.chatbot.network.client.TcpClient;
import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public abstract class RunnableService implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(RunnableService.class);
    protected final Client client;
    protected boolean stop;
    protected final Map<Command, Method> commandMethods = new HashMap<>();

    public RunnableService() {
        this.client = new TcpClient(Environment.get("bot.twitch.url"),
                Integer.parseInt(Environment.get("bot.twitch.port")));

        initialization();
    }

    protected void initialization() {
        final Class<CommandController> commander = CommandController.class;
        if (commander.isAnnotationPresent(Commander.class)) {
            for (Method method : commander.getDeclaredMethods()) {
                if (method.isAnnotationPresent(TargetCommand.class)) {
                    final TargetCommand annotatedCommand = method.getAnnotation(TargetCommand.class);
                    commandMethods.put(annotatedCommand.value(), method);
                }
            }
        } else throw new RuntimeException("Commander is not found.");
        LOGGER.info("Initialization finished.");
    }

    public void stop() {
        this.stop = true;
    }

    protected void loop() throws IOException {
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

    protected Message serve(@NotNull Message message) {
        Object response = MessageBuilder.build(Command.UNKNOWN, "");
        try {
            final Method method = commandMethods.get(message.getCommand());
            if (method != null)
                response = method.invoke(CommandController.class.newInstance(), message);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            LOGGER.warn("Command invocation error!", e);
        }
        return (Message) response;
    }
}
