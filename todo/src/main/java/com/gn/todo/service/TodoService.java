package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.repository.TodoRepository;
import com.gn.todo.specification.TodoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
	
	private final TodoRepository todoRepository;
	
	public Page<Todo> selectTodoAll(SearchDto searchDto, PageDto pageDto){
		Specification<Todo> spec = (root, query, criteriaBuilder) -> null;
		Pageable pageable 
			= PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage());
		
		spec = spec.and(TodoSpecification.contentContains(searchDto.getSearch_text()));
		return todoRepository.findAll(spec, pageable);
	}

}
