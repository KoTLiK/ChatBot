package kotlik.chatbot.message;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@RunWith(Parameterized.class)
public class MessageTest {

    public MessageTest() {}

    @NotNull
    @Parameters
    public static Collection messages() {
        return Arrays.asList(new Object[][] {
                {MessageBuilder.command(Command.PING)
                        .withTrailing("tmi.twitch.tv")
                        .build(),
                    "PING :tmi.twitch.tv"},
                {MessageBuilder.command(Command._001).withParams("<user>")
                        .withTrailing("Welcome, GLHF! Also some colon : maybe another : OK!")
                        .withNick("tmi.twitch.tv")
                        .build(),
                    ":tmi.twitch.tv 001 <user> :Welcome, GLHF! Also some colon : maybe another : OK!"},
                {MessageBuilder.command(Command.PRIVMSG)
                        .withParams("#<channel>")
                        .withTrailing("This is a sample message")
                        .withNick("<user>")
                        .withUser("<user>")
                        .withHost("<user>.tmi.twitch.tv")
                        .build(),
                    ":<user>!<user>@<user>.tmi.twitch.tv PRIVMSG #<channel> :This is a sample message"},
                {MessageBuilder.command(Command.CAP)
                        .withParams("*", "ACK")
                        .withTrailing("twitch.tv/membership")
                        .withNick("tmi.twitch.tv")
                        .build(),
                    ":tmi.twitch.tv CAP * ACK :twitch.tv/membership"},
                {MessageBuilder.command(Command.NOTICE)
                        .withParams("#<channel>")
                        .withTrailing("<message>")
                        .withTags("msg-id=<msg_id>")
                        .withNick("tmi.twitch.tv")
                        .build(),
                    "@msg-id=<msg_id> :tmi.twitch.tv NOTICE #<channel> :<message>"},
                {MessageBuilder.command(Command.CLEARCHAT)
                        .withParams("#dallas")
                        .withTrailing("ronni")
                        .withTags("ban-reason=Follow\\sthe\\srules")
                        .withNick("tmi.twitch.tv")
                        .build(),
                    "@ban-reason=Follow\\sthe\\srules :tmi.twitch.tv CLEARCHAT #dallas :ronni"},
                {MessageBuilder.command(Command.ROOMSTATE)
                        .withParams("#dallas")
                        .withTags("broadcaster-lang=en;r9k=0;slow=0;subs-only=0")
                        .withNick("tmi.twitch.tv")
                        .build(),
                    "@broadcaster-lang=en;r9k=0;slow=0;subs-only=0 :tmi.twitch.tv ROOMSTATE #dallas"},
                {MessageBuilder.command(Command.UNKNOWN)
                        .withTrailing("tmi.twitch.tv")
                        .build(),
                    "WHO :tmi.twitch.tv"}
        });
    }

    @Parameter(0)
    public Message expectedMsg;

    @Parameter(1)
    public String inputStr;

    @Test
    public void messageParseTest() {
        Message message = MessageParser.parse(inputStr);

        Assert.assertNotNull(message);
        Assert.assertTrue(mapEquals(expectedMsg.getTags(), message.getTags()));
        Assert.assertEquals(expectedMsg.getNick(), message.getNick());
        Assert.assertEquals(expectedMsg.getUser(), message.getUser());
        Assert.assertEquals(expectedMsg.getHost(), message.getHost());
        Assert.assertEquals(expectedMsg.getCommand(), message.getCommand());
        Assert.assertEquals(expectedMsg.getParams(), message.getParams());
        Assert.assertEquals(expectedMsg.getTrailing(), message.getTrailing());
    }

    @Test
    public void messageParseNoRegexpTest() { // TODO benchmark Regexp vs. NoRegexp
        Message message = MessageParser.parseNoRegexp(inputStr);

        Assert.assertNotNull(message);
        Assert.assertTrue(mapEquals(expectedMsg.getTags(), message.getTags()));
        Assert.assertEquals(expectedMsg.getNick(), message.getNick());
        Assert.assertEquals(expectedMsg.getUser(), message.getUser());
        Assert.assertEquals(expectedMsg.getHost(), message.getHost());
        Assert.assertEquals(expectedMsg.getCommand(), message.getCommand());
        Assert.assertEquals(expectedMsg.getParams(), message.getParams());
        Assert.assertEquals(expectedMsg.getTrailing(), message.getTrailing());
    }

    @Test
    public void messageFormatTest() {
        if (expectedMsg.getCommand().equals(Command.UNKNOWN)) return;

        final String result = MessageFormatter.fullFormat(expectedMsg);
        Assert.assertEquals(inputStr + Message.DELIMITER, result);
    }

    private static boolean mapEquals(@NotNull Map<String, String[]> expected, @NotNull Map<String, String[]> actual) {
        if (!expected.keySet().equals(actual.keySet()))
            return false;
        for (Map.Entry<String, String[]> item : expected.entrySet()) {
            if (!Arrays.equals(actual.get(item.getKey()), item.getValue()))
                return false;
        }
        return true;
    }
}
