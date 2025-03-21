package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
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
	
	@PostMapping("/create")
	@ResponseBody
	public Map<String,String> todoCreate(TodoDto dto){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할 일 추가중 오류가 발생하였습니다.");
		
		int result = service.createTodo(dto);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "정상적으로 할 일이 추가되었습니다.");			
		}
		
		return resultMap;
	}
}
