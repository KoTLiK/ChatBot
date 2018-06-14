package kotlik.chatbot.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class ParametricString {
    private final HashMap<String, Object> params = new HashMap<>();
    private String buffer;

    public ParametricString(String buffer) {
        this.buffer = buffer;
    }

    public ParametricString set(final String key, Object value) {
        params.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            buffer = buffer.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return buffer;
    }

    public static String resolve(String msg, @NotNull Object param) {
        Object params[] = { param };
        return resolve(msg, params);
    }

    public static String resolve(String msg, @NotNull Object params[]) {
        for (int i = 0; i < params.length; i++) {
            msg = msg.replace("{" + Integer.toString(i) + "}", params[i].toString());
        }
        return msg;
    }
}
