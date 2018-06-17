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


@RunWith(Parameterized.class)
public class MessageParseTest {

    public MessageParseTest() {}

    @NotNull
    @Parameters
    public static Collection messages() {
        return Arrays.asList(new Object[][] {
                {MessageBuilder.build(Command.PING,
                        " :tmi.twitch.tv"),
                    "PING :tmi.twitch.tv"},
                {MessageBuilder.build(Command._001,
                        " <user> :Welcome, GLHF! Also some colon : maybe another : OK!",
                        null,
                        "tmi.twitch.tv"),
                    ":tmi.twitch.tv 001 <user> :Welcome, GLHF! Also some colon : maybe another : OK!"},
                {MessageBuilder.build(Command.PRIVMSG,
                        " #<channel> :This is a sample message",
                        null,
                        "<user>",
                        "<user>",
                        "<user>.tmi.twitch.tv"),
                    ":<user>!<user>@<user>.tmi.twitch.tv PRIVMSG #<channel> :This is a sample message"},
                {MessageBuilder.build(Command.CAP,
                        " * ACK :twitch.tv/membership",
                        null,
                        "tmi.twitch.tv"),
                    ":tmi.twitch.tv CAP * ACK :twitch.tv/membership"},
                {MessageBuilder.build(Command.NOTICE,
                        " #<channel> :<message>",
                        "msg-id=<msg_id> ",
                        "tmi.twitch.tv"),
                    "@msg-id=<msg_id> :tmi.twitch.tv NOTICE #<channel> :<message>"},
                {MessageBuilder.build(Command.CLEARCHAT,
                        " #dallas :ronni",
                        "ban-reason=Follow\\sthe\\srules ",
                        "tmi.twitch.tv"),
                    "@ban-reason=Follow\\sthe\\srules :tmi.twitch.tv CLEARCHAT #dallas :ronni"},
                {MessageBuilder.build(Command.ROOMSTATE,
                        " #dallas",
                        "broadcaster-lang=en;r9k=0;slow=0;subs-only=0 ",
                        "tmi.twitch.tv"),
                    "@broadcaster-lang=en;r9k=0;slow=0;subs-only=0 :tmi.twitch.tv ROOMSTATE #dallas"},
                {MessageBuilder.build(Command.UNKNOWN,
                        ":tmi.twitch.tv"),
                    "WHO :tmi.twitch.tv"}
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
