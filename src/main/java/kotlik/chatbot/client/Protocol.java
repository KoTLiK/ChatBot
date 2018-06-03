package kotlik.chatbot.client;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;


public class Protocol {
    private final static String DELIMITER = "\r\n";
    private final StringBuilder container = new StringBuilder();
    private final LinkedList<String> messages = new LinkedList<>();

    public Protocol() {}

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public String popMessage() {
        int position;
        while ((position = container.indexOf(DELIMITER)) != -1) {
            messages.addLast(container.substring(0, position));
            container.delete(0, position + 2);
        }
        return messages.isEmpty() ? "" : messages.pollFirst();
    }

    public boolean checkAndAppend(ByteBuffer buffer) {
        container.append(StandardCharsets.UTF_8.decode(buffer).toString());
        return (container.indexOf(DELIMITER) != -1);
    }
}
