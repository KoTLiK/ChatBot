package kotlik.chatbot.network.protocol;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface Protocol {

    public IrcProtocol clear();

    @Contract(pure = true)
    public boolean isEmpty();

    @NotNull
    public String popMessage();

    public boolean checkAndAppend(final ByteBuffer buffer);
}
