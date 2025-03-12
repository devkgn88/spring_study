package com.gn.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.mvc.entity.Board;

public interface BoardRepository extends JpaRepository<Board,Long>{
	List<Board> findByBoardTitleContaining(String keyword);
	
	List<Board> findByBoardContentContaining(String keyword);
	
    List<Board> findByBoardTitleContainingOrBoardContentContaining(String titleKeyword, String contentKeyword);

}
