package kotlik.chatbot.controller;

import java.io.IOException;

public interface Service extends Runnable {
    public void stop() throws IOException;
}
