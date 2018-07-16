package kotlik.chatbot.controller;

import java.io.IOException;

public interface Service extends Runnable {
    // TODO Create a solution to Command & Message Limits per 30s
    void stop() throws IOException;
    void reconnect() throws IOException;
    void changeChannel(final String channel);
}
