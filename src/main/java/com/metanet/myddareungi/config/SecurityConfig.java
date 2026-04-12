package com.metanet.myddareungi.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			ObjectProvider<JwtAuthenticationFilter> jwtAuthenticationFilterProvider,
			JwtCookieUtils jwtCookieUtils) throws Exception {
		http
				.csrf(csrf -> {
					CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
					requestHandler.setCsrfRequestAttributeName(null);
					csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
							.csrfTokenRequestHandler(requestHandler);
				})
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
						.requestMatchers(
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/v3/api-docs/**",
								"/v3/api-docs")
						.permitAll()
						.requestMatchers(HttpMethod.GET, "/", "/home", "/login", "/signup", "/password/forgot")
						.permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/password/question",
								"/api/v1/auth/password/reset")
						.permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/users/signup").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/analysis/**", "/api/v1/analysis/**").authenticated()
						.requestMatchers("/css/**").permitAll()
						.anyRequest().authenticated())
				.logout(logout -> logout
						.logoutUrl("/api/v1/auth/logout")
						.addLogoutHandler((request, response, authentication) -> response.addHeader(
								HttpHeaders.SET_COOKIE,
								jwtCookieUtils.createExpiredAccessTokenCookie().toString()))
						.logoutSuccessHandler((request, response, authentication) -> response.sendRedirect("/login?logout=1"))
						.permitAll())
				.formLogin(form -> form
						.loginPage("/login")
						.permitAll());

		jwtAuthenticationFilterProvider.ifAvailable(
				filter -> http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class));

		http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static final class CsrfCookieFilter extends OncePerRequestFilter {
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain)
				throws ServletException, IOException {
			CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
			if (csrfToken != null) {
				csrfToken.getToken();
			}
			filterChain.doFilter(request, response);
		}
	}

	// 로컬 개발시 모든 경로 security 허용
	// @Bean
	// public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception
	// {
	// http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
	// return http.build();
	// }
}
