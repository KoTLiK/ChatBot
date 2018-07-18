package kotlik.chatbot.controller;

import kotlik.chatbot.Bot;
import kotlik.chatbot.annotations.TargetCommand;
import kotlik.chatbot.message.Command;
import kotlik.chatbot.message.Message;
import kotlik.chatbot.message.MessageBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

final public class CommandController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommandController.class);
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
        // User joined a channel
        // <nick> JOIN #<channel>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.PRIVMSG)
    public Message privateMessage(Message message) {
        // User sent a private message
        // <nick> PRIVMSG #<channel> :<text>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.PART)
    public Message part(Message message) {
        // User left a channel
        // <nick> PART #<channel>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.QUIT)
    public Message quit(Message message) {
        // User disconnected from the server
        // TODO check syntax
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.MODE)
    public Message mode(Message message) {
        // Granting operator privileges
        // <nick> MODE #<channel> +/-o <user>
        // <nick> == 'jtv'
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command._353)
    public Message names_353(Message message) {
        // List of users in channel
        // <host> 353 <nick> = #<channel> :<nicks>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command._366)
    public Message names_366(Message message) {
        // The end of list of users
        // <host> 366 <nick> #<channel> :End of /NAMES list
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.CAP)
    public Message cap(Message message) {
        // Twitch capabilities acknowledgement ?
        // <host> CAP * ACK :<capabilities>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.CLEARCHAT)
    public Message clearChat(Message message) {
        // Temporary or permanent ban on a channel
        // TODO twitch tags with more info
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.HOSTTARGET)
    public Message hostTarget(Message message) {
        // Host starts or stops a message
        // Host start: <host> HOSTTARGET <current-channel> :<target-channel> [<number-of-viewers>|-]
        // Host ends: <host> HOSTTARGET <current-channel> :- [<number-of-viewers>]
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.NOTICE)
    public Message notice(Message message) {
        // General server stuff TODO twitch tags -> additional info
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.RECONNECT)
    public Message reconnect(Message message) {
        try {
            LOGGER.info("Reconnecting...");
            Bot.getServiceInstance().reconnect();
        } catch (IOException e) {
            LOGGER.warn("Unable to reconnect!", e);
        }
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
