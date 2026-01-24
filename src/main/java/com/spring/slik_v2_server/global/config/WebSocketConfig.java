package com.spring.slik_v2_server.global.config;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {

    @Value("${socketio.port:9092}")
    private int port;

    private SocketIOServer server;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(port);
        config.setOrigin("*");
        config.setAllowHeaders("*");

        server = new SocketIOServer(config);
        server.addConnectListener(client -> {
            String deviceId = client.getHandshakeData().getSingleUrlParam("deviceId");
            if (deviceId != null) {
                client.joinRoom(deviceId);
            }
        });
        server.start();
        return server;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        if (server != null) {
            server.stop();
        }
    }
}
