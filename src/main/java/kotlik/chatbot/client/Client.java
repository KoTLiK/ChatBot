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
        client.write(buffer);
        buffer.clear();
        LOGGER.log(Level.FINE, "Sent: {}", message);
    }

    // TODO create package of raw data from socket, then truncate beginning of the package for one message
    public String receive() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Client.BUFFER_SIZE);
        int i = -1;
        while (true) {
            i = client.read(buffer);
            if (i > 0) {
                buffer.flip();
            } else break;
        }
        return null;
    }
}
