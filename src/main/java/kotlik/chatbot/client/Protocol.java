package kotlik.chatbot.client;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public class Protocol {
    private final static String DELIMITER = "\r\n";
    private final StringBuilder container = new StringBuilder();
    private final Charset charset;

    public Protocol() {
        this.charset = Charset.forName("UTF-8");
    }

    public Protocol(Charset charset) {
        this.charset = charset;
    }

    public boolean empty() {
        return false;
    }

    public String popMessage() {
        return null;
    }

    public boolean checkAndAppend(ByteBuffer buffer, int received) {
        container.append(new String(buffer.array(), 0, received, charset));
        return (container.indexOf(DELIMITER) != -1);
    }
}
