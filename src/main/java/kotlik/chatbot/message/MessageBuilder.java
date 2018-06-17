package kotlik.chatbot.message;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MessageBuilder {

    public static Message build(Command command, String params) {
        final Message message = addParams(new Message(), params);
        return message.setCommand(command);
    }

    public static Message build(Command command, String params, String tags) {
        final Message message = build(command, params);
        return addTags(message, tags);
    }

    public static Message build(Command command, String params, String tags, String nick) {
        final Message message = build(command, params, tags);
        return message.setNick(nick);
    }

    public static Message build(Command command, String params, String tags, String nick, String user, String host) {
        final Message message = build(command, params, tags, nick);
        return message.setUser(user).setHost(host);
    }

    public static Message addParams(Message message, @NotNull String params) {
        if (params.contains(":")) {
            final int index = params.indexOf(":");
            message.setTrailing(true, params.substring(index + 1));
            params = params.substring(0, index);
        }

        List<String> middles = Arrays.asList(params.split("\\s+"));
        return message.setParams(middles.stream()
                                  .filter(middle -> middle.length() > 0)
                                  .collect(Collectors.toList()));
    }

    public static Message addTags(Message message, final String tags) {
        if (tags != null)
            return message.setTags(new ArrayList<>(Arrays.asList(tags.split("[; ]"))));
        else return message;
    }
}
