package kotlik.chatbot.utils;

import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

public class Environment {
    private final static Logger LOGGER = LoggerFactory.getLogger(Environment.class.getName());
    private final static String PROPERTY_FILE_NAME = "bot.properties";
    private final static Properties PROPERTIES = getProperties(PROPERTY_FILE_NAME);

    private Properties liveProperties;
    private final String liveFilename;

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
        this.liveFilename = filename;
    }

    //synchronized
    public boolean loadProperties() {
        final URL fileURL = Environment.class.getClassLoader().getResource(liveFilename);
        if (fileURL == null) {
            LOGGER.error(ParametricString.resolve("File URL for '{0}' not found!", liveFilename));
            return false;
        }

        final File file = new File(fileURL.getFile());
        final StringBuilder content = new StringBuilder();

        try (final Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine())
                content.append(scanner.nextLine()).append("\n");

            liveProperties = new Properties();
            liveProperties.load(new StringReader(content.toString()));
        } catch (FileNotFoundException e) {
            LOGGER.error(ParametricString.resolve("Property file '{0}' not found!", liveFilename), e);
            return false;
        } catch (IOException e) {
            LOGGER.error(ParametricString.resolve("Unable to load property file '{0}'!", liveFilename), e);
            return false;
        }

        LOGGER.info("Properties successfully loaded.");
        return true;
    }

    public String getValue(final String key) {
        return getProperty(this.liveProperties, key);
    }
}
