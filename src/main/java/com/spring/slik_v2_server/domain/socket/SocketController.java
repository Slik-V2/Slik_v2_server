package com.spring.slik_v2_server.domain.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SocketController {

    @OnEvent("join_room")
    public void onJoinRoom(SocketIOClient client, Map<String, String> data) {
        client.joinRoom(data.get("deviceId"));
    }
}
