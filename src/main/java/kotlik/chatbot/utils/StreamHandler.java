package kotlik.chatbot.utils;

import kotlik.chatbot.controller.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class StreamHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(StreamHandler.class);
    private final Scanner input;
    private Service service = null;

    public StreamHandler(final InputStream stream) {
        this.input = new Scanner(stream);
    }

    public void bind(Service service) {
        this.service = service;
    }


    public void start() {
        if (service == null)
            throw new RuntimeException("Service unavailable!");

        Thread thread = new Thread(service);
        thread.start();

        LOGGER.info("CLI is running.");

        String inputText;
        try {
            while (input.hasNext()) {
                inputText = input.nextLine().toLowerCase();
                LOGGER.info(inputText);
                switch (inputText) {
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
            LOGGER.error("Service thread problem!", e);
        }
    }
}
