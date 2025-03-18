package com.gn.mvc.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//스프링에서 읽는 환경설정 파일임을 의미
@Configuration
//스프링 시큐리티를 활성화하겠다는 어노테이션
@EnableWebSecurity
public class WebSecurityConfig {

	
	// 정적 리소스에 스프링 시큐리티를 비활성화
	// 이미지, html등의 파일에는 인증, 인가 적용X
	@Bean
	WebSecurityCustomizer configure() {
		return (web -> web.ignoring()
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
	}
	
	// 특정 HTTP 요청이 들어왔을 때 보안 관련 사항을 설정
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(requests -> requests
							.requestMatchers("/login","/member/create","/member").permitAll()
							.anyRequest().authenticated())
			.formLogin(login -> login
					.loginPage("/login")
					//.defaultSuccessUrl("/")
					.successHandler(new MyLoginSuccessHandler())
					.failureHandler(new MyLoginFailureHandler()))
			.logout(logout -> logout
					.logoutSuccessUrl("/login")
					.invalidateHttpSession(true));
		return http.build();
	} 
	
	// 비밀번호 암호화에 사용될 빈 등록
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// AuthenticationManager(인증 관리자) 관련 사항을 설정
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
	throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
}
