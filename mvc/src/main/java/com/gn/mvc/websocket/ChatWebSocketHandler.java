package com.gn.mvc.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gn.mvc.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserIdFromSession(session);
        userSessions.put(userId, session);
        System.out.println("(1) :  " + userId + " WebSocket 연결됨");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    	ChatMessageDto chatMessage = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);
        // 데이터베이스에 채팅 메시지 등록
        WebSocketSession receiverSession = userSessions.get(String.valueOf(chatMessage.getReceiver_no()));
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(chatMessage.getMessage_content()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String userId = getUserIdFromSession(session);
        userSessions.remove(userId);
        System.out.println("(3) :  " + userId + " WebSocket 연결 해제됨");
    }

    private String getUserIdFromSession(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1]; // ?userId=1 같은 방식으로 접속해야 함
    }
}
