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
        return MessageBuilder.unknown();
    }

    @TargetCommand(Command.MODE)
    public Message mode(Message message) {
        return MessageBuilder.unknown();
    }

    // Might never happen
    @TargetCommand(Command.NAMES)
    public Message names(Message message) {
        return MessageBuilder.unknown();
    }

    @TargetCommand(Command._353)
    public Message names_353(Message message) {
        return MessageBuilder.unknown();
    }

    @TargetCommand(Command._366)
    public Message names_366(Message message) {
        return MessageBuilder.unknown();
    }

    @TargetCommand(Command.PART)
    public Message part(Message message) {
        return MessageBuilder.unknown();
    }

    @TargetCommand(Command.QUIT)
    public Message quit(Message message) {
        return MessageBuilder.unknown();
    }
}
