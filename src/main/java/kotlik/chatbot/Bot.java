package kotlik.chatbot;

import kotlik.chatbot.controller.FreeMessageService;
import kotlik.chatbot.controller.RunnableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


public class Bot {
    private final static Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    public static void main(String[] args) {
        final RunnableService service = new FreeMessageService();

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
        } catch (InterruptedException e) {
            LOGGER.error("Service thread problem!", e);
        }
    }
}
