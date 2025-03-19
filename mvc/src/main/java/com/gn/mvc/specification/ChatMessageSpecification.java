package com.gn.mvc.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.mvc.entity.ChatMessage;
import com.gn.mvc.entity.ChatRoom;

public class ChatMessageSpecification {
	public static Specification<ChatMessage> chatRoomEquals(ChatRoom chatRoom){
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.equal(root.get("chatRoom"), chatRoom);
	}
}
