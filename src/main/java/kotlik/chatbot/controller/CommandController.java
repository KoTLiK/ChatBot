package kotlik.chatbot.controller;

import kotlik.chatbot.annotations.Commander;
import kotlik.chatbot.annotations.TargetCommand;
import kotlik.chatbot.message.Command;
import kotlik.chatbot.message.Message;
import kotlik.chatbot.message.MessageBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Commander
final public class CommandController {
    private static final Message UNKNOWN_MESSAGE = MessageBuilder.unknown();

    @Contract(pure = true)
    @TargetCommand(Command.UNKNOWN)
    public Message unknown(Message message) {
        return message;
    }

    @TargetCommand(Command.PING)
    public Message ping(@NotNull Message message) {
        return MessageBuilder.command(Command.PONG)
                .withTrailing(message.getTrailing())
                .build();
    }

    @TargetCommand(Command.JOIN)
    public Message join(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.MODE)
    public Message mode(Message message) {
        return UNKNOWN_MESSAGE;
    }

    // Might never happen
    @TargetCommand(Command.NAMES)
    public Message names(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command._353)
    public Message names_353(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command._366)
    public Message names_366(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.PART)
    public Message part(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.QUIT)
    public Message quit(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.CAP)
    public Message cap(Message message) {
        return UNKNOWN_MESSAGE;
    }
}
