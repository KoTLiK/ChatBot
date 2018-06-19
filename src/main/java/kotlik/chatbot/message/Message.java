package kotlik.chatbot.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Message {
    public final static String DELIMITER = "\r\n";
    private final static Logger LOGGER = LoggerFactory.getLogger(Message.class);

    private List<String> tags;
    private String nick;
    private String user;
    private String host;
    private Command command;
    private List<String> params;
    private String trailing;

    public Message(List<String> tags, String nick, String user, String host, Command command, List<String> params, String trailing) {
        this.tags = tags;
        this.nick = nick;
        this.user = user;
        this.host = host;
        this.command = command;
        this.params = params;
        this.trailing = trailing;
    }

    public List<String> getTags() {
        return tags;
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

    public String getTrailing() {
        return trailing;
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
