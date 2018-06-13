package kotlik.chatbot;

import kotlik.chatbot.service.Service;
import kotlik.chatbot.utils.ParametricString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


public class Bot {
    private final static Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    public static void main(String[] args) {
        final Service service = new Service();
        final Thread thread = new Thread(service);

        thread.start();

        Scanner input = new Scanner(System.in);
        String inputText;
        while (input.hasNext()) {
            inputText = input.nextLine().toLowerCase();
            LOGGER.info(ParametricString.resolve("Bot: [{0}]", inputText));
            switch (inputText) {
                case "stop":
                    service.stop();
                    break;
                case "exit":
                case "quit":
                    service.stop();
                    return;
                default:
            }
        }
    }
}
