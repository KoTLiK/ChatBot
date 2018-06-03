package kotlik.chatbot.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {
    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());
    private final static int BUFFER_SIZE = 2048;

    private SocketChannel client;
    private final String hostname;
    private final int port;
    private final Protocol protocol = new Protocol();

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() throws IOException {
        final InetSocketAddress hostAddress = new InetSocketAddress(hostname, port);
        client = SocketChannel.open(hostAddress);
        LOGGER.log(Level.INFO, "Connected to [{}].", (hostname + ":" + Integer.toString(port)) );
    }

    public void send(String message) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        while (buffer.hasRemaining()) {
            client.write(buffer);
        }
        LOGGER.log(Level.FINE, "Sent: [{}]", message);
    }

    public String receive() throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        if (protocol.isEmpty()) {
            int received;
            while (true) {
                buffer.clear();
                received = client.read(buffer);
                buffer.flip();

                if (received < 0) {
                    LOGGER.log(Level.INFO, "End of stream reached. Connection closed.");
                    return null;
                } else if (protocol.checkAndAppend(buffer)) {
                    break;
                }
            }
        }

        final String message = protocol.popMessage();
        LOGGER.log(Level.FINE, "Received: [{}]", message);
        return message;
    }
}
