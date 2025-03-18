package com.gn.mvc.service;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	
	private final DataSource dataSource;
	private final UserDetailsService userDetailsService;
	
	public int deleteMember(Long id) {
		int result = 0;
		try {
			// 1. id를 기준으로 타킷 조회
			Member target = memberRepository.findById(id).orElse(null);
			if(target != null) {
				memberRepository.delete(target);
				// 2. remember-me가 있다면 무효화
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username = ?";
				jdbcTemplate.update(sql,target.getMemberId());
				// 3. 변경된 회원 정보 Spring Security에 즉시 반영
				
				SecurityContextHolder.getContext().setAuthentication(null);

			}
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int updateMember(MemberDto param) {
		int result = 0;
		try {
			// 1. 회원 정보 수정
			param.setMember_pw(passwordEncoder.encode(param.getMember_pw()));
			Member updated = memberRepository.save(param.toEntity());
			if(updated != null) {
				// 2. remember-me가 있다면 무효화
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username = ?";
				jdbcTemplate.update(sql,param.getMember_id());
				// 3. 변경된 회원 정보 Spring Security에 즉시 반영
				UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(param.getMember_id());
				Authentication newAuth = new UsernamePasswordAuthenticationToken(
						updatedUserDetails, 
						updatedUserDetails.getPassword(), 
						updatedUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
			}
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Member selectMemberOne(Long id) {
		Member member = memberRepository.findById(id).orElse(null);
		return member;
	}
	
	public MemberDto createMember(MemberDto param) {
		param.setMember_pw(passwordEncoder.encode(param.getMember_pw()));
		
		Member entity = param.toEntity();
		Member saved = memberRepository.save(entity);
		System.out.println("후 : "+saved);
		return new MemberDto().toDto(saved);
	}
}
