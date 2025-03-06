package com.gn.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gn.demo.vo.Member;

@Controller
public class MemberController {

	@GetMapping("/memberList")
	public String selectMemberList(Model model) {
		List<Member> list = new ArrayList<Member>();
		list.add(new Member("김철수",25));
		list.add(new Member("이영희",17));
		model.addAttribute("members", list);
		return "/member/list";
	}
}
