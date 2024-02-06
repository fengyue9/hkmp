package com.ruoyi.device.handler;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 实时视频预览处理器
 *
 * @author hongrongjian
 * @date 2023/12/10
 */
@Component
public class VideoSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 在这里处理实时视频数据的传输
        // message.getPayload() 包含实时视频数据
        // 将数据发送到前端
        try {
            session.sendMessage(new TextMessage("Received: " + message.getPayload()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAll(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                WebSocketMessage<String> webSocketMessage = new TextMessage(message);
                session.sendMessage(webSocketMessage);
            }
        }
    }
}