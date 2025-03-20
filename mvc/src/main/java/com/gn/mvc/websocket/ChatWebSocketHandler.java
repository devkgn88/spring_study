package com.gn.mvc.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gn.mvc.dto.ChatMessageDto;
import com.gn.mvc.entity.ChatMessage;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

	// ConcurrentHashMap은 멀티 스레드 환경에서도 동작하는 Map
	// 채팅의 경우 다른 브라우저 2개가 소통을 해야하므로 사용
    private static final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<Long,WebSocketSession>();
    private static final Map<Long, Long> userRooms = new ConcurrentHashMap<Long,Long>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatMessageRepository chatMessageRepository;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    	String userNo = getQueryParam(session,"userNo");
    	String roomNo = getQueryParam(session,"roomNo");
      userSessions.put(Long.parseLong(userNo), session);
        
      // 채팅방 ID 저장(사용자가 현재 접속한 방 정보 업데이트)
      userRooms.put(Long.parseLong(userNo), Long.parseLong(roomNo));
      System.out.println("(1) :  " + userNo + " WebSocket 연결됨");
    }
    
    private String getQueryParam(WebSocketSession session, String param) {
    	// userNo=1&roomNo=3
    	String query = session.getUri().getQuery();
    	if(query != null) {
    		// userNo=1 , roomNo=3
    		for(String target : query.split("&")) {
    			String[] keyValue = target.split("=");
    			if(keyValue.length == 2 && keyValue[0].equals(param)) {
    				return keyValue[1];
    			}
    		}
    	}
    	return null;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    	ChatMessageDto dto = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);
    	
        // 데이터베이스에 채팅 메시지 등록
    	if(userSessions.containsKey(dto.getSender_no())) {
    		ChatMessage entity = ChatMessage.builder()
    							.sendMember(Member.builder().memberNo(dto.getSender_no()).build())
    							.chatRoom(ChatRoom.builder().roomNo(dto.getRoom_no()).build())
    							.messageContent(dto.getMessage_content())
    							.build();
    		chatMessageRepository.save(entity);				
    	}
    	
    	// 받는 사람한테 메시지 출력
        WebSocketSession receiverSession = userSessions.get(dto.getReceiver_no());
        Long receiverRoom = userRooms.get(dto.getReceiver_no());
        
        if (receiverSession != null && receiverSession.isOpen()
        		&& receiverRoom == dto.getRoom_no()) {
        	// 단순 메시지를 보내는 것 X
        	// 모든 정보가 포함된 데이터 JSON 형태로 보내기
            receiverSession.sendMessage(new TextMessage(message.getPayload()));
        }
        
        // 보내는 사람한테 메시지 출력
        WebSocketSession senderSession = userSessions.get(dto.getSender_no());
        Long senderRoom = userRooms.get(dto.getSender_no());
        
        if(senderSession != null && senderSession.isOpen()
        		&& senderRoom == dto.getRoom_no()) {
        	senderSession.sendMessage(new TextMessage(message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
    	String userNo = getQueryParam(session,"userNo");
        userSessions.remove(Long.parseLong(userNo));
        userRooms.remove(Long.parseLong(userNo));
        System.out.println("(3) :  " + userNo + " WebSocket 연결 해제됨");
    }

//    private String getUserIdFromSession(WebSocketSession session) {
//        return session.getUri().getQuery().split("=")[1]; // ?userId=1 같은 방식으로 접속해야 함
//    }
}