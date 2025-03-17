package com.gn.mvc.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gn.mvc.entity.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberDetails implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	private final Member member;

	// 사용자의 권한 설정
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 기본권한 user 부여 
	    return List.of(new SimpleGrantedAuthority("user"));
	}

	// 사용자 이름(로그인 ID)반환
	// 이때 사용되는 데이터는 반드시 고유한 값이어야 함
	@Override
	public String getUsername() {
		return member.getMemberId();
	}
	
	// 사용자 비밀번호 반환
	// 암호화된 상태의 비밀번호여야 함
	@Override
	public String getPassword() {
		return member.getMemberPw();
	}

	// 계정 상태 관리
	// is~로 시작하며, boolean 타입을 리턴하는 메소드가 모두 true일때 -> 사용가능한 계정
	
	// 계정 만료 여부 반환하는 메소드
	// 임시 계정이거나 비활성화된 계정인지 관리하는데 사용
	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	// 계정의 잠금 여부 반환하는 메소드
	// 사용자가 부적절한 접근을 다수 시도하면 계정을 잠글 수 있음
	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	// 계정의 만료 여부 반환
	@Override
	public boolean isCredentialsNonExpired() {
		// 패스워드 만료 여부를 확인하는 로직이 필요하다면 추가 
	    return true;
	}

	// 계정 사용 가능 여부 확인
	@Override
	public boolean isEnabled() {
		// 계정이 사용 가능한지 확인하는 로직이 필요하다면 추가 
	    return true;
	}
	
}
