package com.gn.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gn.mvc.entity.Board;

public interface BoardRepository extends JpaRepository<Board,Long>{
//	List<Board> findByBoardTitleContaining(String keyword);
//	
//	List<Board> findByBoardContentContaining(String keyword);
//	
//  List<Board> findByBoardTitleContainingOrBoardContentContaining(String titleKeyword, String contentKeyword);
	
	@Query(value="SELECT b FROM Board b "+
	"WHERE b.boardTitle LIKE CONCAT('%',?1,'%')")
	List<Board> findByBoardTitleContaining(String keyword);
	
	@Query(value="SELECT b FROM Board b "+
	"WHERE b.boardContent LIKE CONCAT('%',?1,'%')")
	List<Board> findByBoardContentContaining(String keyword);
	
	@Query(value="SELECT b FROM Board b "+
	"WHERE b.boardTitle LIKE CONCAT('%',?1,'%') "+
	"OR b.boardContent LIKE CONCAT('%',?2,'%')")
	List<Board> findByBoardTitleContainingOrBoardContentContaining(String titleKeyword, String contentKeyword);
			
	

}
