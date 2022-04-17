package space.pxls.server.packets.chat;

import java.util.Map;

public class ServerChatUserUpdate {
    public String type = "chat_user_update";
    public String who;
    public Map<String, Object> updates;

    public ServerChatUserUpdate(String who, Map<String, Object> updates) {
        this.who = who;
        this.updates = updates;
    }

    public String getType() {
        return type;
    }

    public String getWho() {
        return who;
    }

    public Map<String, Object> getUpdates() {
        return updates;
    }
}
