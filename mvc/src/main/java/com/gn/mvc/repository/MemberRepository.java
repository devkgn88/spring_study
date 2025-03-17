package com.gn.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.mvc.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long>{
	
	
}
