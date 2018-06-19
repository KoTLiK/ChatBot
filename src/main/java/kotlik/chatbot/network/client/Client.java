package kotlik.chatbot.network.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;


public interface Client extends AutoCloseable {

    void start() throws IOException;

    void stop() throws IOException;

    default void close() throws Exception {
        stop();
    }

    void send(@NotNull final String message) throws IOException;

    @Nullable
    String receive() throws IOException;
}
