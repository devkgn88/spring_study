package com.gn.mvc.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.mvc.entity.Member;

public class MemberSpecification {
	public static Specification<Member> memberIdEquals(String memberId){
		return (root, query, criteriaBuilder) ->
			criteriaBuilder.equal(root.get("memberId"), memberId);
	}
}
