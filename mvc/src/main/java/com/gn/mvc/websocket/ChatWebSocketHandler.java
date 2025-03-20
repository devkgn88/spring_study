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

	// ConcurrentHashMap은 멀티 스레드 환경에서도 동작하는 Map
	// 채팅의 경우 다른 브라우저 2개가 소통을 해야하므로 사용
    private static final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<Long,WebSocketSession>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    	String userNo = getUserNoFromSession(session);
      userSessions.put(Long.parseLong(userNo), session);
      System.out.println("(1) :  " + userNo + " WebSocket 연결됨");
    }
    
    private String getUserNoFromSession(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1]; // ?userNo=1 같은 방식으로 접속해야 함
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    	ChatMessageDto chatMessage = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);
    	
      // 데이터베이스에 채팅 메시지 등록
    	
    	// 받는 사람한테 메시지 출력
        WebSocketSession receiverSession = userSessions.get(chatMessage.getReceiver_no());
        
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(chatMessage.getMessage_content()));
        }
        
        // 보내는 사람한테 메시지 출력
        WebSocketSession senderSession = userSessions.get(chatMessage.getSender_no());
        
        if(senderSession != null && senderSession.isOpen()) {
        	senderSession.sendMessage(new TextMessage(chatMessage.getMessage_content()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
    	String userNo = getUserNoFromSession(session);
      userSessions.remove(Long.parseLong(userNo));
      System.out.println("(3) :  " + userNo + " WebSocket 연결 해제됨");
    }

}