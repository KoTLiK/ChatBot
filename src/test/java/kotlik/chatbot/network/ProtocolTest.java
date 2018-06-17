package kotlik.chatbot.network;

import kotlik.chatbot.network.protocol.IrcProtocol;
import kotlik.chatbot.network.protocol.Protocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;


public class ProtocolTest {
    private ByteBuffer buffer;

    @Before
    public void setup() {
        buffer = ByteBuffer.allocateDirect(1024);
        buffer.put("Nam quis nulla.".getBytes());
        buffer.flip();
    }

    @Test
    public void protocolReceive() {
        final Protocol protocol = new IrcProtocol();

        Assert.assertTrue(protocol.isEmpty());
        Assert.assertTrue(protocol.popMessage().isEmpty());
        Assert.assertFalse(protocol.checkAndAppend(buffer));
        Assert.assertTrue(protocol.popMessage().isEmpty());

        bufferWithNewLines();
        protocol.clear();

        Assert.assertTrue(protocol.checkAndAppend(buffer));
        Assert.assertTrue(protocol.isEmpty());
        Assert.assertEquals("Lorem ipsum dolor sit amet, consectetuer adipiscing elit.", protocol.popMessage());
        Assert.assertFalse(protocol.isEmpty());
        Assert.assertFalse(protocol.popMessage().isEmpty());
        Assert.assertTrue(protocol.isEmpty());
        Assert.assertTrue(protocol.popMessage().isEmpty());
    }

    private void bufferWithNewLines() {
        buffer.clear();
        buffer.put("Lorem ipsum dolor sit amet, consectetuer adipiscing elit.\r\n".getBytes());
        buffer.put("Nulla accumsan, elit sit amet varius semper, nulla mauris mollis quam, tempor suscipit diam nulla vel leo.\r\n".getBytes());
        buffer.put("In rutrum. Etiam dui sem, fermentum vitae, sagittis id, malesuada in, quam. Nulla est.".getBytes());
        buffer.flip();
    }
}
