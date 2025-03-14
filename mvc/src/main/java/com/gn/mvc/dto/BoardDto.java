package com.gn.mvc.dto;

import com.gn.mvc.entity.Board;
import com.gn.mvc.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BoardDto {
	private Long board_no;
	private String board_title;
	private String board_content;
	private Long board_writer;
	
	public Board toEntity() {
		return Board.builder()
				.boardTitle(board_title)
				.boardContent(board_content)
				.boardNo(board_no)
				.member(Member.builder().memberNo(board_writer).build())
				.build();
	}
	
	public BoardDto toDto(Board board){
		return BoardDto.builder()
				.board_title(board.getBoardTitle())
				.board_content(board.getBoardContent())
				.board_no(board.getBoardNo())
				.build();
	}
}
