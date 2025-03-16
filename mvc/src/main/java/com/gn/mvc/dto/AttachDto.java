package com.gn.mvc.dto;

import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttachDto {

	private Long attach_no;
	private Long board_no;
	private String ori_name;
	private String new_name;
	private String attach_path;
	
	public Attach toEntity() {
		return Attach.builder()
				.attachNo(attach_no)
				.board(Board.builder().boardNo(board_no).build())
				.oriName(ori_name)
				.newName(new_name)
				.attachPath(attach_path)
				.build();
	}
}
