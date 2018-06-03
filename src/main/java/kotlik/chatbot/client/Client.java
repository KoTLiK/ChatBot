package kotlik.chatbot.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {
    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());
    private final static int BUFFER_SIZE = 2048;
    private final static Charset CHARSET = Charset.forName("UTF-8");
    private SocketChannel client;
    private final String hostname;
    private final int port;
    private final Protocol protocol = new Protocol(CHARSET);

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
        final ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(CHARSET));
        client.write(buffer);
        buffer.clear();
        LOGGER.log(Level.FINE, "Sent: [{}]", message);
    }

    // TODO create package of raw data from socket, then truncate beginning of the package for one message
    public String receive() throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        if (protocol.empty()) {
            int received;
            while (true) {
                buffer.clear();
                received = client.read(buffer);
                buffer.flip();

                if (received < 0) {
                    // TODO end-of-stream
                    return null;
                } else if (protocol.checkAndAppend(buffer, received)) {
                    break;
                }
            }
        }

        final String message = protocol.popMessage();
        LOGGER.log(Level.FINE, "Received: [{}]", message);
        return message;
    }
}
