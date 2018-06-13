package kotlik.chatbot.client;

import kotlik.chatbot.utils.ParametricString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;


final public class Client {
    private final static Logger LOGGER = LoggerFactory.getLogger(Client.class);
    public final static int BUFFER_SIZE = 2048;

    private SocketChannel client;
    private final String hostname;
    private final int port;
    private final Protocol protocol = new Protocol();

    public Client(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() throws IOException {
        final InetSocketAddress hostAddress = new InetSocketAddress(hostname, port);
        client = SocketChannel.open(hostAddress);
        LOGGER.info(ParametricString.resolve("Connected to [{0}:{1}].",
                                                new Object[]{ hostname, Integer.toString(port) }));
    }

    public void stop() throws IOException {
        // TODO maybe send a QUIT message ?
        client.close();
        protocol.clear();
        LOGGER.info("Connection closed.");
    }

    public void send(@NotNull final String message) throws IOException {
        if (message.length() == 0) return;
        final ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(Charset.forName("UTF-8")));
        while (buffer.hasRemaining()) {
            client.write(buffer);
        }
        LOGGER.info(ParametricString.resolve("Sent: [{0}]", message));
    }

    @Nullable
    public String receive() throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        if (protocol.isEmpty()) {
            int received;
            while (true) {
                buffer.clear();
                received = client.read(buffer);
                buffer.flip();

                if (received < 0) {
                    LOGGER.info("End of stream reached.");
                    return null;
                } else if (protocol.checkAndAppend(buffer)) {
                    break;
                }
            }
        }

        final String message = protocol.popMessage();
        LOGGER.info(ParametricString.resolve("Received: [{0}]", message));
        return message;
    }
}
