package com.metanet.myddareungi.config;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.metanet.myddareungi.domain.member.service.MemberAuthService;

import io.jsonwebtoken.JwtException;

@Component
@Profile("!view")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtCookieUtils jwtCookieUtils;
	private final MemberAuthService memberAuthService;

	public JwtAuthenticationFilter(
		JwtTokenProvider jwtTokenProvider,
		JwtCookieUtils jwtCookieUtils,
		MemberAuthService memberAuthService
	) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.jwtCookieUtils = jwtCookieUtils;
		this.memberAuthService = memberAuthService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			Optional<String> accessToken = jwtCookieUtils.resolveAccessToken(request);
			if (accessToken.isPresent()) {
				authenticate(accessToken.get());
			}
		}

		filterChain.doFilter(request, response);
	}

	private void authenticate(String token) {
		try {
			if (!jwtTokenProvider.validateToken(token)) {
				return;
			}

			Long userId = jwtTokenProvider.extractUserId(token);
			UserDetails userDetails = memberAuthService.loadUserById(userId);
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (JwtException | IllegalArgumentException ex) {
			SecurityContextHolder.clearContext();
		}
	}
}
