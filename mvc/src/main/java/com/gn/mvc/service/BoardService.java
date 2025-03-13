package com.gn.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	
	public Board selectBoardOne(Long id) {
		return boardRepository.findById(id).orElse(null);
	}
	
	public Page<Board> selectBoardAll(SearchDto searchDto,PageDto pageDto){
		
		Specification<Board> spec = (root,query,criteriaBuilder) -> null; 
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").descending());
		
		if(searchDto.getOrder_type() == 2) {
			pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").ascending());
		}
		
//		Sort sort = Sort.by("regDate").descending();
//		if(dto.getOrder_type() == 2) {
//			sort = Sort.by("regDate").ascending();
//		}
		if(searchDto.getSearch_type() == 1) {
//			list = boardRepository.findByBoardTitleContaining(dto.getSearch_text());	
//			System.out.println("제목 : "+list);
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()));
		}else if(searchDto.getSearch_type() == 2) {
//			list = boardRepository.findByBoardContentContaining(dto.getSearch_text());
//			System.out.println("내용 : "+list);
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 3) {
//			list = boardRepository.findByBoardTitleContainingOrBoardContentContaining(dto.getSearch_text(),dto.getSearch_text());
//			System.out.println("제목+내용 : "+list);
			spec = spec.and(BoardSpecification.boardTitleContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		}
		
		return boardRepository.findAll(spec,pageable);
	}
	
	public BoardDto createBoard(BoardDto param) {
		System.out.println("전 : "+param);
		Board entity = param.toEntity();
		Board saved = boardRepository.save(entity);
		System.out.println("후 : "+saved);
		return new BoardDto().toDto(saved);
	}
}
