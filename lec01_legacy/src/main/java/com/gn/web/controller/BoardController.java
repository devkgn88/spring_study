package com.gn.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.gn.web.vo.Board;

@Controller
public class BoardController {

	@GetMapping
	public void selectBoardList() {
		List<Board> resultList = new ArrayList<Board>();
		System.out.println(resultList);
	}
}
