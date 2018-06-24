package kotlik.chatbot.controller;

import java.io.IOException;

public interface Service extends Runnable {
    // TODO Create a solution to Command & Message Limits per 30s
    public void stop() throws IOException;
    public void restart();
}
