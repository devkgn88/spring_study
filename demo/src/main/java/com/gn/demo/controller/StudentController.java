package com.gn.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class StudentController {
	

	@GetMapping("/student")
	public String selectStudentList(Model model) {
		List<String> resultList = new ArrayList<String>();
		resultList.add("김철수");
		resultList.add("이영희");
		model.addAttribute("resultList",resultList);
		return "/student/list";
	}
	
//	@GetMapping("/student")
//	public ModelAndView selectStudentList() {
//		List<String> resultList = new ArrayList<String>();
//		resultList.add("김철수");
//		resultList.add("이영희");
//		ModelAndView mav = new ModelAndView();
//		mav.addObject("resultList",resultList);
//		mav.setViewName("/student/list");
//		return mav;
//	}
}
