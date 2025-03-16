package com.gn.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.service.AttachService;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final AttachService attachService;

	private Logger logger = LoggerFactory.getLogger(BoardController.class);

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
	public Map<String, String> createBoardApi(BoardDto dto) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생했습니다.");

		List<AttachDto> attachDtoList = new ArrayList<AttachDto>();

		for (MultipartFile mf : dto.getFiles()) {
			AttachDto attachDto = attachService.uploadFile(mf);
			if (attachDto != null)
				attachDtoList.add(attachDto);
		}

		if (attachDtoList.size() == dto.getFiles().size()) {
			// 컴퓨터에 정상 저장된 파일의 수와
			// 전달받은 파일의 개수가 일치하는 경우
			int result = boardService.createBoard(dto, attachDtoList);
			if (result > 0) {
				resultMap.put("res_code", "200");
				resultMap.put("res_msg", "게시글이 등록되었습니다.");
			}
		}
		return resultMap;
	}

	@GetMapping("/board")
	public String selectBoardAll(Model model, SearchDto searchDto, PageDto pageDto) {

		if (pageDto.getNowPage() == 0)
			pageDto.setNowPage(1);

		Page<Board> resultList = boardService.selectBoardAll(searchDto, pageDto);

		pageDto.setTotalPage(resultList.getTotalPages());

		model.addAttribute("boardList", resultList);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("pageDto", pageDto);
		return "board/list";
	}

	@GetMapping("/board/{id}")
	public String selectBoardOne(@PathVariable("id") Long id, Model model) {
		logger.info("게시글 단일 조회 : " + id);
		Board result = boardService.selectBoardOne(id);
		model.addAttribute("board", result);
		List<Attach> attachList = attachService.selectAttachList(id);
		model.addAttribute("attachList",attachList);
		return "board/detail";
	}

	@GetMapping("/board/{id}/update")
	public String updateBoardView(@PathVariable("id") Long id, Model model) {
		Board entity = boardService.selectBoardOne(id);
		model.addAttribute("board", entity);
		return "board/update";
	}

	@PostMapping("/board/{id}/update")
	@ResponseBody
	public Map<String, String> updateBoardApi(@PathVariable("id") Long id, BoardDto param) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 수정중 오류가 발생했습니다.");

		param.setBoard_no(id);

		Board saved = boardService.updateBoard(param);
		if (saved != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 수정되었습니다.");
		}

		return resultMap;
	}

	@DeleteMapping("/board/{id}")
	@ResponseBody
	public Map<String, String> deleteBoardApi(@PathVariable("id") Long id) {

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 삭제중 오류가 발생했습니다.");

		int result = boardService.deleteBoard(id);

		if (result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 삭제되었습니다.");
		}

		return resultMap;
	}
}
