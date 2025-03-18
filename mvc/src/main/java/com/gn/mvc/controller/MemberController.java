package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;

	@GetMapping("/member/create")
	public String createMemberView() {
		return "member/create";
	}
	
	@PostMapping("/member")
	@ResponseBody
	public Map<String,String> createMemberApi(MemberDto dto){
		System.out.println("11123434234");
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "회원가입 중 오류가 발생했습니다.");
		
		MemberDto saved = memberService.createMember(dto);
		
		if(saved.getMember_no() != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원가입이 완료되었습니다.");
		}
		
		
		return resultMap;
	}
	
	@GetMapping("/login")
	public String loginView(Model model,
			@RequestParam(value="error", required=false) String error,
			@RequestParam(value="errorMsg", required=false) String errorMsg) {
		model.addAttribute("error", error);
		model.addAttribute("errorMsg", errorMsg);
		return "member/login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
	    System.out.println("Before logout - Session ID: " + request.getSession().getId());

		new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
	    System.out.println("After logout - Session ID: " + request.getSession().getId());

	    Cookie rememberMeCookie = new Cookie("remember-me", null);
	    rememberMeCookie.setMaxAge(0);  // 쿠키 만료
	    rememberMeCookie.setPath("/");  // 모든 경로에서 삭제 적용
	    response.addCookie(rememberMeCookie);

		return "redirect:/login";
	}
	
	@GetMapping("/member/{id}/update")
	public String memberUpdateView(@PathVariable("id") Long id, Model model) {
		Member member = memberService.selectMemberOne(id);
		model.addAttribute("member",member);
		return "member/update";
	}
	
	@PostMapping("/member/{id}/update")
	@ResponseBody
	public Map<String,String> memberUpdateApi(MemberDto param){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 수정중 오류가 발생하였습니다.");
		
		int result = memberService.updateMember(param);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원이 수정되었습니다.");
		}
		
		return resultMap;
	}
	
	@DeleteMapping("/member/{id}")
	@ResponseBody
	public Map<String,String> memberDeleteApi(@PathVariable("id") Long id){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 탈퇴중 오류가 발생했습니다.");
		
		// 현재 로그인된 사용자 정보 제거

		int result = memberService.deleteMember(id);

		if (result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원 탈퇴되었습니다.");
		}

		return resultMap;
	}
	
//	@GetMapping("/member/{id}/delete")
//	public String memberDeleteApi(@PathVariable("id") Long id) {
//		memberService.deleteMember(id);
//		return "redirect:/logout";
//	}
}
