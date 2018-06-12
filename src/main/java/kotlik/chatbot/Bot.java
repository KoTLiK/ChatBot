package kotlik.chatbot;

import kotlik.chatbot.service.Service;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Bot {
    private final static Logger LOGGER = Logger.getLogger(Bot.class.getName());
    public static void main(String[] args) {
        final Service service = new Service();
        final Thread thread = new Thread(service);

        thread.start();

        Scanner input = new Scanner(System.in);
        String inputText;
        while (input.hasNext()) {
            inputText = input.nextLine().toLowerCase();
            LOGGER.log(Level.INFO, "Bot: [{0}]", inputText);
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
