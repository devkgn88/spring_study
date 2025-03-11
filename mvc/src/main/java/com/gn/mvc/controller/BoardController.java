package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	
	@GetMapping("/board/create")
	public String createBoardView() {
		return "board/create";
	}
	
//	@PostMapping("/board/create")
//	@ResponseBody
//	public Map<String,String> createBoardApi(
//			@RequestParam("board_title") String boardTitle,
//			@RequestParam("board_content") String boardContent){
//		Map<String,String> resultMap = new HashMap<String,String>();
//		resultMap.put("res_code", "404");
//		resultMap.put("res_msg", "게시글 등록중 오류가 발생했습니다.");
//		
//		System.out.println(boardTitle);
//		System.out.println(boardContent);
//		
//		return resultMap;
//	}
	
	
	@PostMapping("/board/create")
	@ResponseBody
	public Map<String,String> createBoardApi(BoardDto dto){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생했습니다.");
		
		BoardDto saved = boardService.createBoard(dto);
		
		if(saved.getBoard_no() != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 등록되었습니다.");
		}
		
		
		return resultMap;
	}
}
