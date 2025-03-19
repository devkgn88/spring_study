package com.gn.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {
	
	@GetMapping("/chat/list")
	public String chatListView() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    	MemberDetails md = (MemberDetails)authentication.getPrincipal();
//    	Long memberNo = md.getMember().getMemberNo();
		
		return "chat/list";	
	}	

}
