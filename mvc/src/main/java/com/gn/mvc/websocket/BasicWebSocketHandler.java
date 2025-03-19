package com.gn.mvc.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class BasicWebSocketHandler extends TextWebSocketHandler{

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		System.out.println("서버가 받은 메시지 : "+payload);
		session.sendMessage(new TextMessage("서버가 보낸 메시지 : "+session.getId()));
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("새로운 WebSocket 연결 : "+session.getId());
	}


	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("WebSocket 연결 종료 : "+session.getId());
	}
	
	

}
