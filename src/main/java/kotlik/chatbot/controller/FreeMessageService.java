package kotlik.chatbot.controller;

import kotlik.chatbot.message.Message;
import kotlik.chatbot.message.MessageBuilder;
import kotlik.chatbot.message.MessageFormatter;
import kotlik.chatbot.utils.ParametricString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

final public class FreeMessageService extends RunnableService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public FreeMessageService() {}

    @Override
    protected void login() throws IOException {
        // Login
        final String nickname = userEnvironment.getValue("user.client.username");
        client.send(MessageFormatter.format(MessageBuilder.nick(nickname)));
        client.send(ParametricString.resolve("USER {0} {0} {0} :{0}{1}", nickname, Message.DELIMITER));

        // Join channel
        client.send(MessageFormatter.format(MessageBuilder.join(userEnvironment.getValue("user.client.channel"))));
    }
}

