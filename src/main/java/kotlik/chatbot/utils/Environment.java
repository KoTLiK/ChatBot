package kotlik.chatbot.utils;

import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


public class Environment {
    private final static Logger LOGGER = LoggerFactory.getLogger(Environment.class.getName());
    private final static String PROPERTY_FILE_NAME = "bot.properties";
    private final static Properties PROPERTIES = getProperties(PROPERTY_FILE_NAME);

    private final Properties liveProperties;

    public static Properties getProperties(final String filename) {
        Properties appProps = new Properties();
        try {
            appProps.load(Environment.class.getClassLoader().getResourceAsStream(filename));
        } catch (IOException e) {
            LOGGER.error("Unable to load [" + filename + "] property file!");
        }
        return appProps;
    }

    public static String getProperty(@NotNull final Properties properties, final String key) {
        String property = properties.getProperty(key);
        if (StringUtils.isEmpty(property))
            throw new IllegalArgumentException("Property ["+ key +"] has not been found!");
        else return property;
    }

    public static String get(final String key) {
        return getProperty(PROPERTIES, key);
    }

    public static String getPropertyFromFile(final String filename, final String key) {
        return getProperty(getProperties(filename), key);
    }

    public Environment(final String filename) {
        this.liveProperties = getProperties(filename);
    }

    public String getValue(final String key) {
        return getProperty(this.liveProperties, key);
    }
}
