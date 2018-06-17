package kotlik.chatbot.network.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;


public interface Client {

    public void start() throws IOException;

    public void stop() throws IOException;

    public void send(@NotNull final String message) throws IOException;

    @Nullable
    public String receive() throws IOException;
}
