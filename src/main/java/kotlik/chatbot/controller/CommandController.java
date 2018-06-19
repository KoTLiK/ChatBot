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
}
