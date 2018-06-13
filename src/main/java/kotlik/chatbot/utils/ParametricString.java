package kotlik.chatbot.utils;

import org.jetbrains.annotations.NotNull;


public class ParametricString {

    public static String resolve(String msg, @NotNull Object param) {
        Object params[] = { param };
        return resolve(msg, params);
    }

    public static String resolve(String msg, @NotNull Object params[]) {
        for (int i = 0; i < params.length; i++) {
            msg = msg.replace("{"+Integer.toString(i)+"}", params[i].toString());
        }
        return msg;
    }
}
