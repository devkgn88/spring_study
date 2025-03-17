package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final AttachRepository attachRepository;
	private final AttachService attachService;
	
	public int deleteBoard(Long id) {
		int result = 0;
		try {
			// 1. id를 기준으로 타킷 조회
			Board target = boardRepository.findById(id).orElse(null);
			// 2. 타킷이 존재하는 경우 삭제
			if(target != null) {
				boardRepository.deleteById(id);
			}
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Board updateBoard(BoardDto param, List<AttachDto> attachList) {
		Board result = null;
		try {
			// 1. id를 기준으로 타킷 조회
			Board target = boardRepository.findById(param.getBoard_no()).orElse(null);
			
			// 2. 타깃이 존재하는 경우 업데이트
			if(target != null) {
				if(param.getDelete_files() != null && !param.getDelete_files().isEmpty()) {
					for(Long attach_no : param.getDelete_files()) {
						// 1. 메모리에서 파일 자체 삭제
						if(attachService.deleteFile(attach_no) > 0) {
							Attach targetAttach = attachRepository.findById(attach_no).orElse(null);
							if(targetAttach != null) {
								attachRepository.deleteById(attach_no);
							}
						}
					}
				}
				if(attachList.size() != 0) {
					for(AttachDto attachDto : attachList) {
						attachDto.setBoard_no(param.getBoard_no());
						Attach attachTarget = attachDto.toEntity();
						attachRepository.save(attachTarget);
					}
				}
				result = boardRepository.save(param.toEntity());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
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
	
	@Transactional(rollbackFor = Exception.class)
	public int createBoard(BoardDto param,List<AttachDto> attachList) {
		int result = 0;
		try {
			Board entity = param.toEntity();
			Board saved = boardRepository.save(entity);
			Long boardNo = saved.getBoardNo();
			if(attachList.size() != 0) {
				for(AttachDto attachDto : attachList) {
					attachDto.setBoard_no(boardNo);
					Attach attach = attachDto.toEntity();
					attachRepository.save(attach);	 
				}
			}
		result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
