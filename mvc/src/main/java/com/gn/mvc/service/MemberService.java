package com.gn.mvc.service;

import org.springframework.stereotype.Service;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
	public MemberDto createMember(MemberDto param) {
		System.out.println("전 : "+param);
		Member entity = param.toEntity();
		Member saved = memberRepository.save(entity);
		System.out.println("후 : "+saved);
		return new MemberDto().toDto(saved);
	}
}
