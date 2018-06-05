package kotlik.chatbot.parser;

import org.jetbrains.annotations.NotNull;


public enum Command {
    PASS("PASS"),
    NICK("NICK"),
    PING("PING"),
    PONG("PONG"),
    JOIN("JOIN"),
    PART("PART"),
    PRIVMSG("PRIVMSG"),
    NOTICE("NOTICE"),
    MODE("MODE"),
    NAMES("NAMES"),
    CAP("CAP"),
    CLEARCHAT("CLEARCHAT"),
    HOSTTARGET("HOSTTARGET"),
    RECONNECT("RECONNECT"),
    ROOMSTATE("ROOMSTATE"),
    USERNOTICE("USERNOTICE"),
    USERSTATE("USERSTATE"),

    UNKNOWN("UNKNOWN"),

    /**
     * Numbered commands might be deleted later on.
     * They are only in responses, but one never knows.
     */
    _001("001"),
    _002("002"),
    _003("003"),
    _004("004"),
    _353("353"),
    _366("366"),
    _372("372"),
    _375("375"),
    _376("376"),
    _421("421"),

    _5XX("5XX");

    private final String cmd;

    Command(@NotNull final String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return this.cmd;
    }

    public static Command fromString(@NotNull final String cmd) {
        if (Character.isDigit(cmd.charAt(0)))
            return Command.valueOf("_" + cmd);
        else return Command.valueOf(cmd);
    }
}
