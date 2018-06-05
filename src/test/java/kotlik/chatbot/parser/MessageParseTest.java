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
                {new Message.Builder(Command.PING)
                         .params(" :tmi.twitch.tv")
                         .build(), "PING :tmi.twitch.tv"},
                {new Message.Builder(Command._001)
                         .nick("tmi.twitch.tv")
                         .params(" <user> :Welcome, GLHF!")
                         .build(), ":tmi.twitch.tv 001 <user> :Welcome, GLHF!"},
                {new Message.Builder(Command.PRIVMSG)
                         .nick("<user>")
                         .user("<user>")
                         .host("<user>.tmi.twitch.tv")
                         .params(" #<channel> :This is a sample message")
                         .build(), ":<user>!<user>@<user>.tmi.twitch.tv PRIVMSG #<channel> :This is a sample message"},
                {new Message.Builder(Command.CAP)
                         .nick("tmi.twitch.tv")
                         .params(" * ACK :twitch.tv/membership")
                         .build(), ":tmi.twitch.tv CAP * ACK :twitch.tv/membership"},
                {new Message.Builder(Command.NOTICE)
                         .tags("@msg-id=<msg_id> ")
                         .nick("tmi.twitch.tv")
                         .params(" #<channel> :<message>")
                         .build(), "@msg-id=<msg_id> :tmi.twitch.tv NOTICE #<channel> :<message>"},
                {new Message.Builder(Command.CLEARCHAT)
                         .tags("@ban-reason=Follow\\sthe\\srules ")
                         .nick("tmi.twitch.tv")
                         .params(" #dallas :ronni")
                         .build(), "@ban-reason=Follow\\sthe\\srules :tmi.twitch.tv CLEARCHAT #dallas :ronni"},
                {new Message.Builder(Command.ROOMSTATE)
                         .tags("@broadcaster-lang=en;r9k=0;slow=0;subs-only=0 ")
                         .nick("tmi.twitch.tv")
                         .params(" #dallas")
                         .build(), "@broadcaster-lang=en;r9k=0;slow=0;subs-only=0 :tmi.twitch.tv ROOMSTATE #dallas"},
                {new Message.Builder(Command.UNKNOWN)
                         .params("tmi.twitch.tv")
                         .build(), "WHO :tmi.twitch.tv"}
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
        Assert.assertEquals(expectedMsg.getTags(), message.getTags());
        Assert.assertEquals(expectedMsg.getNick(), message.getNick());
        Assert.assertEquals(expectedMsg.getUser(), message.getUser());
        Assert.assertEquals(expectedMsg.getHost(), message.getHost());
        Assert.assertEquals(expectedMsg.getCommand(), message.getCommand());
        Assert.assertEquals(expectedMsg.getParams(), message.getParams());
        Assert.assertEquals(expectedMsg.getTrailing(), message.getTrailing());
    }
}
