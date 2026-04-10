package com.metanet.myddareungi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(
					"/swagger-ui/**",
					"/swagger-ui.html",
					"/v3/api-docs/**",
					"/v3/api-docs"
				).permitAll()
				.requestMatchers(HttpMethod.GET, "/", "/home", "/login", "/signup", "/password/forgot").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/auth/password/question", "/api/v1/auth/password/reset").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/users/signup").permitAll()
				.requestMatchers("/css/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.permitAll()
			);
		http.csrf(csrf -> csrf
			    .ignoringRequestMatchers("/api/files/**")
			);
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//    로컬 개발시 모든 경로 security 허용
//    @Bean
//    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
//        return http.build();
//    }
}

