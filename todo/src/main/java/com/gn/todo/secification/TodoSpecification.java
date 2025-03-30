package com.gn.todo.secification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.todo.entity.Todo;

public class TodoSpecification {

	public static Specification<Todo> todoContentContains(String keyword){
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.like(root.get("content"), "%"+keyword+"%");
	}
}
