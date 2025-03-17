package com.gn.mvc.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	public MemberDto createMember(MemberDto param) {
		param.setMember_pw(passwordEncoder.encode(param.getMember_pw()));
		
		Member entity = param.toEntity();
		Member saved = memberRepository.save(entity);
		System.out.println("후 : "+saved);
		return new MemberDto().toDto(saved);
	}
}
