package kotlik.chatbot.message;

import org.jetbrains.annotations.NotNull;

public enum Command {
    PASS,
    NICK,
    PING,
    PONG,
    JOIN,
    PART,
    PRIVMSG,
    NOTICE,
    MODE,
    NAMES,
    CAP,
    CLEARCHAT,
    HOSTTARGET,
    RECONNECT,
    ROOMSTATE,
    USERNOTICE,
    USERSTATE,
    GLOBALUSERSTATE,
    QUIT,
    UNKNOWN,

    /**
     * Numbered commands might be deleted later on.
     * They are only in responses, but one never knows.
     */
    _001,
    _002,
    _003,
    _004,
    _353,
    _366,
    _372,
    _375,
    _376,
    _421,


    ;

    @Override
    public String toString() {
        return this.name().startsWith("_") ? this.name().substring(1) : this.name();
    }

    public static Command fromString(@NotNull final String cmd) {
        try {
            return cmd.matches("\\d{3}") ? Command.valueOf("_" + cmd) : Command.valueOf(cmd);
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }
}
