package kotlik.chatbot;

import kotlik.chatbot.controller.Service;
import kotlik.chatbot.utils.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;


public class Bot {
    private final static Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    public static void main(String[] args) {
        final Service service = getService();

        Thread thread = new Thread(service);
        thread.start();

        final Scanner input = new Scanner(System.in);
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

    private static Class<?> getClassRunner() {
        try {
            return Class.forName(Environment.get("bot.class.service.runner"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Service runner not found!", e);
        }
    }

    private static Service getService() {
        try {
            return (Service) getClassRunner().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Unable to instantiate service!", e);
        }
    }
}
