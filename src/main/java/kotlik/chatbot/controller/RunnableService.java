package kotlik.chatbot.controller;

import kotlik.chatbot.annotations.TargetCommand;
import kotlik.chatbot.message.Command;
import kotlik.chatbot.message.Message;
import kotlik.chatbot.message.MessageBuilder;
import kotlik.chatbot.message.MessageFormatter;
import kotlik.chatbot.message.MessageParser;
import kotlik.chatbot.network.client.Client;
import kotlik.chatbot.network.client.TcpClient;
import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class RunnableService implements Service {
    private final static Logger LOGGER = LoggerFactory.getLogger(RunnableService.class);
    protected final Client client;
    protected AtomicBoolean stop;
    protected AtomicBoolean reconnect;
    protected final Map<Command, Method> commandMethods = new HashMap<>();
    private CommandController commanderInstance;

    public RunnableService() {
        this.client = new TcpClient(Environment.get("bot.twitch.url"),
                Integer.parseInt(Environment.get("bot.twitch.port")));

        this.initialization();
    }

    protected void initialization() {
//        final Reflections reflections = new Reflections("kotlik.chatbot.controller");
//        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Commander.class);

        final Class<CommandController> commander = CommandController.class;
        for (Method method : commander.getDeclaredMethods()) {
            if (method.isAnnotationPresent(TargetCommand.class)) {
                final TargetCommand annotatedCommand = method.getAnnotation(TargetCommand.class);
                commandMethods.put(annotatedCommand.value(), method);
            }
        }

        try {
            commanderInstance = commander.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate the Commander.", e);
        }

        LOGGER.info("Initialization finished.");
    }

    @Override
    public void stop() throws IOException {
        stop.set(true);
        client.send(MessageFormatter.format(MessageBuilder.command(Command.QUIT)
                        .withTrailing("I am shutting down, bye!")
                        .build()
                )
        );
    }

    @Override
    public void reconnect() throws IOException {
        reconnect.set(true);
        client.send(MessageFormatter.format(MessageBuilder.command(Command.QUIT)
                        .withTrailing("I will reconnect, see you soon!")
                        .build()
                )
        );
    }

    protected void loop() throws IOException {
        Message message;
        String rawMessage;
        while (!stop.get() && !reconnect.get()) {
            rawMessage = client.receive();
            if (rawMessage == null) {
                // TODO handle 'End of stream' ???
                break;
            }
            message = MessageParser.parse(rawMessage);
            message = serve(message);
            client.send(MessageFormatter.format(message));
        }
    }

    protected Message serve(@NotNull Message message) {
        Object response = MessageBuilder.command(Command.UNKNOWN).build();
        try {
            final Method method = commandMethods.get(message.getCommand());
            if (method != null)
                response = method.invoke(commanderInstance, message);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.warn("Command invocation failed!", e);
        }
        return (Message) response;
    }
}
