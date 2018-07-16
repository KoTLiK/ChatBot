package kotlik.chatbot.utils;

import kotlik.chatbot.Bot;
import kotlik.chatbot.controller.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class StreamHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(StreamHandler.class);
    private final Scanner input;

    public StreamHandler(final InputStream stream) {
        this.input = new Scanner(stream);
    }

    public void start() {
        final Service service = Bot.getServiceInstance();

        Thread thread = new Thread(service);
        thread.start();

        LOGGER.info("CLI is running.");

        String inputText;
        try {
            while (input.hasNext()) {
                inputText = input.nextLine().toLowerCase();
                LOGGER.info(inputText);

                if (inputText.startsWith("channel:")) {
                    service.changeChannel(inputText.substring(8));
                    continue;
                }

                switch (inputText) {
                    case "reconnect":
                        service.reconnect();
                        break;

                    case "restart":
                        service.stop();
                    case "start":
                        thread.join();
                        thread = new Thread(service);
                        thread.start();
                        break;

                    case "stop":
                        service.stop();
                        break;

                    case "exit":
                    case "quit":
                        service.stop();
                        thread.join();
                        return;

                    default: break;
                }
            }
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Service problem!", e);
        }
    }
}
