package kotlik.chatbot.utils;

import org.codehaus.plexus.util.StringUtils;

import java.io.IOException;
import java.util.Properties;


public class Environment {
    private static final String propertyFileName = "bot.properties";

    public static String getProperty(String key) {
        Properties appProps = new Properties();

        try {
            appProps.load(Environment.class.getClassLoader().getResourceAsStream(propertyFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String property = appProps.getProperty(key);

        if (StringUtils.isEmpty(property))
            throw new IllegalArgumentException("Property ["+ key +"] has not been found!");
        else return property;
    }
}
