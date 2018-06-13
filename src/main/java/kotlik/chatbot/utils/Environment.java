package kotlik.chatbot.utils;

import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


public class Environment {
    private final static Logger LOGGER = LoggerFactory.getLogger(Environment.class.getName());
    private static final String propertyFileName = "bot.properties";
    private static final Properties properties = getProperties(propertyFileName);

    private final Properties liveProperties;

    public static Properties getProperties(final String filename) {
        Properties appProps = new Properties();
        try {
            appProps.load(Environment.class.getClassLoader().getResourceAsStream(filename));
        } catch (IOException e) {
            LOGGER.error("Unable to load [" + filename + "] property file!");
            e.printStackTrace();
        }
        return appProps;
    }

    public static String getProperty(@NotNull Properties properties, final String key) {
        String property = properties.getProperty(key);
        if (StringUtils.isEmpty(property))
            throw new IllegalArgumentException("Property ["+ key +"] has not been found!");
        else return property;
    }

    public static String get(final String key) {
        return getProperty(properties, key);
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
