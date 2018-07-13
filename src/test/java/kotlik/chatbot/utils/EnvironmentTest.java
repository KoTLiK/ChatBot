package kotlik.chatbot.utils;

import org.junit.Assert;
import org.junit.Test;

public class EnvironmentTest {

    private final static String PROPERTY_FILE_NAME = "test.properties";

    @Test(expected = IllegalArgumentException.class)
    public void getEnvironmentPropertyTest() {
        final Environment environment = new Environment(PROPERTY_FILE_NAME);

        Assert.assertNotNull(environment);
        Assert.assertTrue(environment.loadProperties());
        Assert.assertEquals("oauth:", environment.getValue("bot.client.oauth.prefix"));
        Assert.assertEquals("6667", environment.getValue("bot.twitch.port"));

        environment.getValue("some.random.not.existing.property");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEnvironmentPropertyFromFileTest() {
        Assert.assertEquals("oauth:", Environment.getPropertyFromFile(PROPERTY_FILE_NAME,"bot.client.oauth.prefix"));
        Assert.assertEquals("6667", Environment.getPropertyFromFile(PROPERTY_FILE_NAME, "bot.twitch.port"));

        Environment.getPropertyFromFile(PROPERTY_FILE_NAME, "some.random.not.existing.property");
    }

    @Test(expected = NullPointerException.class)
    public void fileNotExistsTest() {
        Environment.getProperties("NotExistingFile.properties");
    }
}
