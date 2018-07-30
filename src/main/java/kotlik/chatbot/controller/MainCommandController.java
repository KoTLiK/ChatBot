package kotlik.chatbot.controller;

import kotlik.chatbot.Bot;
import kotlik.chatbot.annotations.TargetCommand;
import kotlik.chatbot.message.Command;
import kotlik.chatbot.message.Message;
import kotlik.chatbot.message.MessageBuilder;
import kotlik.chatbot.utils.ParametricString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

final public class MainCommandController {
    private final static Logger LOGGER = LoggerFactory.getLogger(MainCommandController.class);
    private static final Message UNKNOWN_MESSAGE = MessageBuilder.unknown();

    @Contract(pure = true)
    @TargetCommand(Command.UNKNOWN)
    public Message unknown(Message message) {
        return message;
    }

    @TargetCommand(Command.PING)
    public Message ping(@NotNull Message message) {
        LOGGER.info("PING PONG");
        return MessageBuilder.command(Command.PONG)
                .withTrailing(message.getTrailing())
                .build();
    }

    @TargetCommand(Command.JOIN)
    public Message join(Message message) {
        LOGGER.info(ParametricString.resolve("User {0} JOINED {1}",
                message.getNick(),
                message.getParam(0)
        ));
        // User joined a channel
        // <nick> JOIN #<channel>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.PRIVMSG)
    public Message privateMessage(Message message) {
        LOGGER.info(ParametricString.resolve("{0} PRIVMSG {1}: {2}",
                message.getNick(),
                message.getParam(0),
                message.getTrailing()
        ));
        // User sent a private message
        // <nick> PRIVMSG #<channel> :<text>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.PART)
    public Message part(Message message) {
        LOGGER.info(ParametricString.resolve("User {0} LEFT {1}",
                message.getNick(),
                message.getParam(0)
        ));
        // User left a channel
        // <nick> PART #<channel>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.QUIT)
    public Message quit(Message message) {
        LOGGER.info(message.getRawMessage());
        // User disconnected from the server
        // TODO check syntax
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.MODE)
    public Message mode(Message message) {
        LOGGER.info(ParametricString.resolve("MODE {0} {1} {2}",
                message.getParam(0),
                message.getParam(1),
                message.getParam(2)
        ));
        // Granting operator privileges
        // <nick> MODE #<channel> +/-o <user>
        // <nick> == 'jtv'
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command._353)
    public Message names_353(Message message) {
        LOGGER.info(ParametricString.resolve("List of users in {0}: {1}",
                message.getParam(2),
                message.getTrailing()
        ));
        // List of users in channel
        // <host> 353 <nick> = #<channel> :<nicks>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command._366)
    public Message names_366(Message message) {
        LOGGER.info("End of users list.");
        // The end of list of users
        // <host> 366 <nick> #<channel> :End of /NAMES list
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.CAP)
    public Message cap(Message message) {
        LOGGER.info(ParametricString.resolve("{0} {1} {2}",
                message.getNick(),
                message.getParam(1).equals("ACK")? "acknowledged" : message.getParam(1),
                message.getTrailing()
        ));
        // Twitch capabilities acknowledgement ?
        // <host> CAP * ACK :<capabilities>
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.CLEARCHAT)
    public Message clearChat(Message message) {
        LOGGER.info(message.getRawMessage());
        // Temporary or permanent ban on a channel
        // TODO twitch tags with more info
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.HOSTTARGET)
    public Message hostTarget(Message message) {
        LOGGER.info(message.getRawMessage());
        // Host starts or stops a message
        // Host start: <host> HOSTTARGET <current-channel> :<target-channel> [<number-of-viewers>|-]
        // Host ends: <host> HOSTTARGET <current-channel> :- [<number-of-viewers>]
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.NOTICE)
    public Message notice(Message message) {
        LOGGER.info(message.getRawMessage());
        // General server stuff TODO twitch tags -> additional info
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.RECONNECT)
    public Message reconnect(Message message) {
        LOGGER.info("Reconnecting...");
        try {
            Bot.getServiceInstance().reconnect();
        } catch (IOException e) {
            LOGGER.warn("Unable to reconnect!", e);
        }
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.ROOMSTATE)
    public Message roomState(Message message) {
        LOGGER.info(message.getRawMessage());
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.USERNOTICE)
    public Message userNotice(Message message) {
        LOGGER.info(message.getRawMessage());
        return UNKNOWN_MESSAGE;
    }

    @TargetCommand(Command.USERSTATE)
    public Message userState(Message message) {
        LOGGER.info(message.getRawMessage());
        return UNKNOWN_MESSAGE;
    }
}
