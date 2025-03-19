package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.PageDto;
import com.gn.mvc.entity.ChatMessage;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.repository.ChatMessageRepository;
import com.gn.mvc.repository.ChatRoomRepository;
import com.gn.mvc.security.MemberDetails;
import com.gn.mvc.specification.ChatMessageSpecification;
import com.gn.mvc.specification.ChatRoomSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	
	private final ChatRoomRepository chatRoomRepository;
//	private final MemberRepository memberRepository;
	private final ChatMessageRepository chatMessageRepository;
	
	public List<ChatMessage> selectChatMsgList(Long id){
		
		ChatRoom chatRoom = chatRoomRepository.findById(id).orElse(null);
		
		Specification<ChatMessage> spec = (root, query, criteriaBuilder) -> null;
		spec = spec.and(ChatMessageSpecification.chatRoomEquals(chatRoom));
		
		List<ChatMessage> msgList = chatMessageRepository.findAll(spec);
		return msgList;
	}
	
	public ChatRoom selectChatRoomOne(Long id) {
		return chatRoomRepository.findById(id).orElse(null);
	}
	
	public Page<ChatRoom> selectChatRoomAll(PageDto pageDto){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	MemberDetails md = (MemberDetails)authentication.getPrincipal();
//    	Long memberNo = md.getMember().getMemberNo();
    	
		Pageable pageable = PageRequest.of(pageDto.getNowPage() - 1, pageDto.getNumPerPage(),
				Sort.by("lastDate").descending());
		
				
		Specification<ChatRoom> spec = (root, query, criteriaBuilder) -> null;
		spec = spec.and(ChatRoomSpecification.fromMemberEquals(md.getMember()));
		spec = spec.or(ChatRoomSpecification.toMemberEquals(md.getMember()));
		
		Page<ChatRoom> list = chatRoomRepository.findAll(spec,pageable);
		
	
    	return list;
	}
	
	

}
