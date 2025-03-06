package com.gn.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gn.demo.vo.Member;

@Controller
public class HomeController {

	@GetMapping({"","/"})
	public String home(Model model) {
		model.addAttribute("member", new Member("홍길동",50));
		return "home";
	}
	
	@GetMapping("/test")
	public String testView() {
		return "test";
	}
	
	@GetMapping("bye")
	public String byeMember(Model model) {
		model.addAttribute("member",new Member("홍길동",50));
		return "goodbye";
	}
	
	
}
