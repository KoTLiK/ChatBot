package kotlik.chatbot.controller;

import kotlik.chatbot.annotations.Commander;
import kotlik.chatbot.annotations.TargetCommand;
import kotlik.chatbot.parser.Command;
import kotlik.chatbot.parser.Message;
import kotlik.chatbot.parser.MessageBuilder;


@Commander
public class CommandController {

    @TargetCommand(Command.UNKNOWN)
    public Message unknown(Message message) {
        return MessageBuilder.build(Command.UNKNOWN, "");
    }

    @TargetCommand(Command.PING)
    public Message ping(Message message) {
        return MessageBuilder.build(Command.PONG, "").setTrailing(message.getTrailing());
    }
}
