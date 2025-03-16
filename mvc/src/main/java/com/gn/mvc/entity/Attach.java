package com.gn.mvc.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="attach")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Builder
public class Attach {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attachNo;
	
	@Column
	private String oriName;
	
	@Column
	private String newName;
	
	@Column
	private String attachPath;
	
	@ManyToOne
	@JoinColumn(name="board_no")
	private Board board;
	
}
