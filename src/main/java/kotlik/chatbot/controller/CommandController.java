package kotlik.chatbot.controller;

import kotlik.chatbot.annotations.Commander;
import kotlik.chatbot.annotations.TargetCommand;
import kotlik.chatbot.message.Command;
import kotlik.chatbot.message.Message;
import kotlik.chatbot.message.MessageBuilder;
import org.jetbrains.annotations.NotNull;


@Commander
final public class CommandController {

    @TargetCommand(Command.UNKNOWN)
    public Message unknown(Message message) {
        return MessageBuilder.build(Command.UNKNOWN, "");
    }

    @TargetCommand(Command.PING)
    public Message ping(@NotNull Message message) {
        return message.setCommand(Command.PONG);
    }
}
