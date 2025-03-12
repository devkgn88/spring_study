package com.gn.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	
	public List<Board> selectBoardAll(SearchDto dto){
		List<Board> list = new ArrayList<Board>();
		if(dto.getSearch_type() == 1) {
			list = boardRepository.findByBoardTitleContaining(dto.getSearch_text());	
			System.out.println("제목 : "+list);
		}else if(dto.getSearch_type() == 2) {
			list = boardRepository.findByBoardContentContaining(dto.getSearch_text());
			System.out.println("내용 : "+list);
		} else if(dto.getSearch_type() == 3) {
			list = boardRepository.findByBoardTitleContainingOrBoardContentContaining(dto.getSearch_text(),dto.getSearch_text());
			System.out.println("제목+내용 : "+list);
		} else {
			list = boardRepository.findAll();
		}
		return list;
	}
	
	public BoardDto createBoard(BoardDto param) {
		System.out.println("전 : "+param);
		Board entity = param.toEntity();
		Board saved = boardRepository.save(entity);
		System.out.println("후 : "+saved);
		return new BoardDto().toDto(saved);
	}
}
