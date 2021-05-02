package unibo.btlib;

import org.json.JSONException;

public interface CommChannel {
    void close();

    void registerListener(Listener listener);

    void removeListener(Listener listener);

    String getRemoteDeviceName();

    void sendMessage(String message);

    interface Listener {
        void onMessageReceived(String receivedMessage) throws JSONException;
        void onMessageSent(String sentMessage);
    }
}
