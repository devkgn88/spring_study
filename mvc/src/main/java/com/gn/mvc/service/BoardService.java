package com.gn.mvc.service;

import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	
	public BoardDto createBoard(BoardDto param) {
		System.out.println("전 : "+param);
		Board entity = param.toEntity();
		Board saved = boardRepository.save(entity);
		System.out.println("후 : "+saved);
		return new BoardDto().toDto(saved);
	}
}
