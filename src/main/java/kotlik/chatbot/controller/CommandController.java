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
        // <nick> JOIN #<channel>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.PRIVMSG)
    public Message privateMessage(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.PART)
    public Message part(Message message) {
        // <nick> PART #<channel>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.QUIT)
    public Message quit(Message message) {
        // TODO check syntax
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.MODE)
    public Message mode(Message message) {
        // <nick> MODE #<channel> +/-o <user>
        // <nick> == 'jtv'
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command._353)
    public Message names_353(Message message) {
        // <host> 353 <nick> = #<channel> :<nicks>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command._366)
    public Message names_366(Message message) {
        // <host> 366 <nick> #<channel> :End of /NAMES list
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.CAP)
    public Message cap(Message message) {
        // <host> CAP * ACK :<capabilities>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.CLEARCHAT)
    public Message clearChat(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.HOSTTARGET)
    public Message hostTarget(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.NOTICE)
    public Message notice(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.RECONNECT)
    public Message reconnect(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.ROOMSTATE)
    public Message roomState(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.USERNOTICE)
    public Message userNotice(Message message) {
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.USERSTATE)
    public Message userState(Message message) {
        return UNKNOWN_MESSAGE;
    }
}
