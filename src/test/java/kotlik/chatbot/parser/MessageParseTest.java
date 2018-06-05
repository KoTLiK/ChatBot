package kotlik.chatbot.parser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class MessageParseTest {

    public MessageParseTest() {}

    @Parameters
    public static Collection messages() {
        return Arrays.asList(new Object[][] {
                {new Message(Command._001), ":tmi.twitch.tv 001 <user> :Welcome, GLHF!"},
                {new Message(Command.PRIVMSG), ":<user>!<user>@<user>.tmi.twitch.tv PRIVMSG #<channel> :This is a sample message"},
                {new Message(Command.CAP), ":tmi.twitch.tv CAP * ACK :twitch.tv/membership"},
                {new Message(Command.NOTICE), "@msg-id=<msg_id> :tmi.twitch.tv NOTICE #<channel> :<message>"}
        });
    }

    @Parameter(0)
    public Message expectedMsg;

    @Parameter(1)
    public String inputStr;

    @Test
    public void MessageTest() {
        Message message = Message.parse(inputStr);

        Assert.assertNotNull(message);
        Assert.assertEquals(expectedMsg.getCommand(), message.getCommand());
    }
}
