package kotlik.chatbot.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Message {
    public final static String DELIMITER = "\r\n";
    private final static Logger LOGGER = LoggerFactory.getLogger(Message.class);
    private final String rawMessage;
    private final List<String> tags;
    private final String nick;
    private final String user;
    private final String host;
    private final Command command;
    private final List<String> params;
    private final String trailing;

    public Message(List<String> tags, String nick, String user, String host, Command command, List<String> params, String trailing, String rawMessage) {
        this.tags = tags;
        this.nick = nick;
        this.user = user;
        this.host = host;
        this.command = command;
        this.params = params;
        this.trailing = trailing;
        this.rawMessage = rawMessage;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTag(int index) {
        return tags.get(index);
    }

    public String getNick() {
        return nick;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public Command getCommand() {
        return command;
    }

    public List<String> getParams() {
        return params;
    }

    public String getParam(int index) {
        return params.get(index);
    }

    public String getTrailing() {
        return trailing;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Unable to serialize message!", e);
        }
        return "{\n\t\"error:\" \"Unable to serialize message!\"\n}\n";
    }
}
