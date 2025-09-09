package com.cmms.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;

@Configuration
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, 
                                 @NonNull ServerHttpResponse response,
                                 @NonNull WebSocketHandler wsHandler, 
                                 @NonNull Map<String, Object> attributes) throws Exception {
        
        // Log the handshake request
        System.out.println("WebSocket handshake request from: " + request.getRemoteAddress());
        System.out.println("WebSocket handshake headers: " + request.getHeaders());
        
        // Allow all connections for now - you can add authentication logic here
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, 
                             @NonNull ServerHttpResponse response,
                             @NonNull WebSocketHandler wsHandler, 
                             @Nullable Exception exception) {
        if (exception != null) {
            System.err.println("WebSocket handshake failed: " + exception.getMessage());
        } else {
            System.out.println("WebSocket handshake successful");
        }
    }
}
