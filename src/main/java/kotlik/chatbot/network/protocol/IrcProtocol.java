package kotlik.chatbot.network.protocol;

import kotlik.chatbot.message.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

final public class IrcProtocol implements Protocol {
    private final StringBuilder container = new StringBuilder();
    private final LinkedList<String> messages = new LinkedList<>();

    public IrcProtocol() {}

    @Override
    public IrcProtocol clear() {
        container.setLength(0);
        messages.clear();
        return this;
    }

    @Contract(pure = true)
    @Override
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    @NotNull
    @Override
    public String popMessage() {
        int position;
        while ((position = container.indexOf(Message.DELIMITER)) != -1) {
            messages.addLast(container.substring(0, position));
            container.delete(0, position + Message.DELIMITER.length());
        }
        return messages.isEmpty() ? "" : messages.pollFirst();
    }

    @Override
    public boolean checkAndAppend(final ByteBuffer buffer) {
        container.append(StandardCharsets.UTF_8.decode(buffer).toString());
        return (container.indexOf(Message.DELIMITER) != -1);
    }
}
