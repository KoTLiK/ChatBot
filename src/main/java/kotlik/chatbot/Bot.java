package kotlik.chatbot;

import kotlik.chatbot.service.Service;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;


public class Bot {
    private final static Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    public static void main(String[] args) {
        final Service service = new Service();

        Thread thread = new Thread(service);
        thread.start();

        Scanner input = new Scanner(System.in);
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
            LOGGER.error(exceptionToString(e));
        }
    }

    public static String exceptionToString(@NotNull Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
