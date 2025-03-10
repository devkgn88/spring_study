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

@Controller
public class BoardController {
	
	@Autowired
	private BoardRepository boardRepository;
	
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
		
		// 1. DTO를 엔티티로 변환
		Board board = dto.toEntity();
		// 2. DTO가 엔티티로 잘 변환되었는지 확인
		System.out.println(board);
		// 3. 레포지토리로 엔티티를 DB에 저장
		Board saved = boardRepository.save(board);
		// 4. board가 DB에 잘 저장되는지 확인
		System.out.println(saved);
		
		
		return resultMap;
	}
}
