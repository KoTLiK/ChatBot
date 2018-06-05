package kotlik.chatbot.parser;

import kotlik.chatbot.utils.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Message {
    public final static String DELIMITER = "\r\n";
    private final static Logger LOGGER = Logger.getLogger(Message.class.getName());
    private final static Pattern regex = Pattern.compile(Environment.getProperty("bot.message.regexp"));

    private Command command;

    public Message() {}

    public Message(Command command) {
        this.command = command;
    }

    @NotNull
    public static Message parse(final String message) {
        Matcher matcher = regex.matcher(message);
        if (!matcher.find()) return new Message(Command.UNKNOWN);

        String cmd = matcher.group("CMD");

        return new Message(Command.fromString(cmd));
    }

    public static String prepare(final Message message) {
        return null;
    }

    public Command getCommand() {
        return command;
    }

    public Message setCommand(Command command) {
        this.command = command;
        return this;
    }
}
