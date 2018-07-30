package kotlik.chatbot.data;

import java.util.Map;

public class RuntimeData {
    private static RuntimeData INSTANCE;
    private final static Object lock = new Object();

    private String currentChannel;
    private RoomState roomState;
    private Map<String, Boolean> users;

    public static RuntimeData getInstance() {
        synchronized (lock) {
            if (INSTANCE == null)
                INSTANCE = new RuntimeData();
        }
        return INSTANCE;
    }

    private RuntimeData() {}

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    public String getCurrentChannel() {
        return currentChannel;
    }
}
