package com.spring.slik_v2_server.global.config;

import com.corundumstudio.socketio.AckMode;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Value("${socketio.server.hostname:0.0.0.0}")
    private String hostname;
    @Value("${socketio.server.port:443}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        config.setOrigin("http://localhost:5173");
        config.setAckMode(AckMode.AUTO);
        config.setPingInterval(60000);
        config.setPingTimeout(18000);

        return new SocketIOServer(config);
    }
}
