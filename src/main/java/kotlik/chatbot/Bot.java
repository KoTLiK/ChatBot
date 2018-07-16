package kotlik.chatbot;

import kotlik.chatbot.controller.Service;
import kotlik.chatbot.utils.Environment;
import kotlik.chatbot.utils.StreamHandler;

public class Bot {
    private static Service service = null;

    public static void main(String[] args) {
        new StreamHandler(System.in).start();
    }

    public static Service getServiceInstance() {
        try {
            if (service == null) {
                final Class<?> serviceClass = Class.forName(Environment.get("bot.class.service.runner"));
                service = (Service) serviceClass.newInstance();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Service runner not found!", e);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Unable to instantiate service!", e);
        }
        return service;
    }
}
