package com.gn.mvc.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gn.mvc.dto.PageDto;
import com.gn.mvc.entity.ChatMessage;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatService service;
	
	@GetMapping("/chat/list")
	public String selectChatRoomAll(Model model, PageDto pageDto) {
		if(pageDto.getNowPage() == 0) pageDto.setNowPage(1);
		
		Page<ChatRoom> resultList = service.selectChatRoomAll(pageDto);
		pageDto.setTotalPage(resultList.getTotalPages());
		
		model.addAttribute("chatRoomList", resultList);
		model.addAttribute("pageDto", pageDto);
		
		return "chat/list";	
	}	
	
	@GetMapping("/chat/{id}/detail")
	public String selectChatRoomOne(@PathVariable("id") Long id,
			Model model) {
		ChatRoom chatRoom = service.selectChatRoomOne(id);
		model.addAttribute("chatRoom", chatRoom);
		
		List<ChatMessage> msgList = service.selectChatMsgList(id);
		model.addAttribute("msgList", msgList);
		
		return "chat/detail";
	}
	

}
