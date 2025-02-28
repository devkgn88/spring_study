package com.gn.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

	@Controller
	public class HomeController {
		// 클라이언트가 요청한 서비스 주소와 메소드 연결
		@GetMapping({"","/"})
		public String home() {
			return "home";
		}
	}