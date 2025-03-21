package com.gn.todo.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodoController {
	
	private final TodoService service;
	

	@GetMapping({"","/"})
	public String todoListView(PageDto pageDto, SearchDto searchDto,
			Model model) {	
		if(pageDto.getNowPage() == 0) pageDto.setNowPage(1);
		
		Page<Todo> resultList = service.selectTodoAll(searchDto, pageDto);
		
		pageDto.setTotalPage(resultList.getTotalPages());
		
		model.addAttribute("resultList", resultList);
		model.addAttribute("pageDto",pageDto);
		model.addAttribute("searchDto", searchDto);
		
		return "todo/list";
	}
}
