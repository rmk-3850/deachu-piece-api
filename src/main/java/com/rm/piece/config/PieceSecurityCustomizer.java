package com.rm.piece.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

import com.rm.security.SecurityCustomizer;

@Component
public class PieceSecurityCustomizer implements SecurityCustomizer{
	@Override
	public void customize(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests(auth->auth
			.requestMatchers("/public/**").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated()
		);
	}
}
