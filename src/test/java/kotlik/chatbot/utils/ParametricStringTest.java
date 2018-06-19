package kotlik.chatbot.utils;

import org.junit.Assert;
import org.junit.Test;

public class ParametricStringTest {
    private final static Environment ENV = new Environment("test.properties");

    @Test
    public void resolveParametricStringTest() {
        final String resultSingle = ParametricString.resolve(ENV.getValue("param.string.resolve.single"), "GLHF");
        final String resultMultiple = ParametricString.resolve(ENV.getValue("param.string.resolve.multiple"), "_");
        final String resultDifferent = ParametricString.resolve(ENV.getValue("param.string.resolve.different"), "_", "-");

        Assert.assertEquals(ENV.getValue("param.string.resolved.single"), resultSingle);
        Assert.assertEquals(ENV.getValue("param.string.resolved.multiple"), resultMultiple);
        Assert.assertEquals(ENV.getValue("param.string.resolved.different"), resultDifferent);
    }

    @Test
    public void bufferedParametricStringTest() {
        final ParametricString psSingle = new ParametricString(ENV.getValue("param.string.resolve.single"));
        psSingle.set("0", "GLHF");
        final ParametricString psMultiple = new ParametricString(ENV.getValue("param.string.resolve.multiple"));
        psMultiple.set("0", "_");
        final ParametricString psDifferent = new ParametricString(ENV.getValue("param.string.resolve.different"));
        psDifferent.set("0", "_").set("1", "-");

        Assert.assertEquals(ENV.getValue("param.string.resolved.single"), psSingle.toString());
        Assert.assertEquals(ENV.getValue("param.string.resolved.multiple"), psMultiple.toString());
        Assert.assertEquals(ENV.getValue("param.string.resolved.different"), psDifferent.toString());
    }
}
