package kotlik.chatbot;

import kotlik.chatbot.service.Service;

import java.util.Scanner;


public class Bot {
    public static void main(String[] args) {
        final Service service = new Service(true);
        final Thread thread = new Thread(service);

        thread.start();

        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            if (input.nextLine().toLowerCase().equals("stop")) {
                service.stop();
            }
        }
    }
}
