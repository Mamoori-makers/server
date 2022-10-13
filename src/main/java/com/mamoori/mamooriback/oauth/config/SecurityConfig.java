package com.mamoori.mamooriback.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mamoori.mamooriback.oauth.filter.JwtAuthenticationFilter;
import com.mamoori.mamooriback.oauth.AuthTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 의존성 주입 중 생성자 주입 자동으로 설정
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private AuthTokenProvider authTokenProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션 disable
			.and()
			.authorizeRequests()// 요청에 의한 보안검사 시작
			.antMatchers("/**", "/**/**", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/oauth2/**").permitAll() // 모든 요청 URL 중 다음을 포함하는 경우에 대해서는 인가를 모두 수락

			//                .antMatchers("/api/**").hasAnyRole(Role.USER.name(),Role.ADMIN.name()) // /api/v1/** 은 USER권한만 접근 가능
			//                .antMatchers("/owner/**").hasAnyRole(Role.OWNER.name(),Role.ADMIN.name())
			//                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
			//                .anyRequest().authenticated() // 어떤 요청에도 보안 검사함
			.anyRequest().permitAll()
			.and()
			.logout()
			.logoutSuccessUrl("/")
			.and()
			.oauth2Login(); // 보안 검증 방식
		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		;
	}

	@Bean
	public JwtAuthenticationFilter tokenAuthenticationFilter() {
		return new JwtAuthenticationFilter(authTokenProvider);
	}

}