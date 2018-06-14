package kotlik.chatbot.utils;

import org.junit.Assert;
import org.junit.Test;


public class EnvironmentTest {

    private final static String PROPERTYFILENAME = "test.properties";

    @Test
    public void getEnvironmentPropertyTest() {
        final Environment environment = new Environment(PROPERTYFILENAME);

        Assert.assertNotNull(environment);
        Assert.assertEquals("oauth:", environment.getValue("bot.client.oauth.prefix"));
        Assert.assertEquals("6667", environment.getValue("bot.twitch.port"));
    }

    @Test
    public void getEnvironmentPropertyFromFileTest() {
        Assert.assertEquals("oauth:", Environment.getPropertyFromFile(PROPERTYFILENAME,"bot.client.oauth.prefix"));
        Assert.assertEquals("6667", Environment.getPropertyFromFile(PROPERTYFILENAME, "bot.twitch.port"));
    }
}
