package com.gn.mvc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.mvc.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
	Page<ChatRoom> findAll(Specification<ChatRoom> spec, Pageable pageable); 
}
