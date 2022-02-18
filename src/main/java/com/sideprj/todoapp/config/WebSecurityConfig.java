package com.sideprj.todoapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.sideprj.todoapp.security.JwtAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http ��ť��Ƽ ����
		http.cors() // WebMvcConfig���� �̹� ���������Ƿ� �⺻ cors ����.
			.and()
			.csrf()// csrf�� ���� ������� �����Ƿ� disable
				.disable()
			.httpBasic()// token�� ����ϹǷ� basic ���� disable
				.disable()
			.sessionManagement()  // session ����� �ƴ��� ����
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests() // /�� /auth/** ��δ� ���� ���ص� ��.
				.antMatchers("/", "/auth/**").permitAll()
				.anyRequest() // /�� /auth/**�̿��� ��� ��δ� ���� �ؾߵ�.
			.authenticated();

		// filter ���.
		// �� ������Ʈ����
		// CorsFilter ������ �Ŀ�
		// jwtAuthenticationFilter �����Ѵ�.
		http.addFilterAfter(
						jwtAuthenticationFilter,
						CorsFilter.class
		);
	}
	;

}
