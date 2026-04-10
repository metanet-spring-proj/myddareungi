package com.metanet.myddareungi.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.metanet.myddareungi.domain.member.model.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
@Profile("!view")
public class JwtTokenProvider {

	private final SecretKey secretKey;
	private final Duration accessTokenExpiration;

	public JwtTokenProvider(
		@Value("${secret.key}") String secretKey,
		@Value("${jwt.access-token-expiration-minutes:30}") long accessTokenExpirationMinutes
	) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.trim().getBytes(StandardCharsets.UTF_8));
		this.accessTokenExpiration = Duration.ofMinutes(accessTokenExpirationMinutes);
	}

	public String createAccessToken(Member member) {
		if (member.getUserId() == null) {
			throw new IllegalArgumentException("JWT 발급을 위한 회원 식별자가 없습니다.");
		}

		Instant now = Instant.now();
		Instant expiry = now.plus(accessTokenExpiration);

		return Jwts.builder()
			.subject(String.valueOf(member.getUserId()))
			.claim("role", member.getRole())
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiry))
			.signWith(secretKey)
			.compact();
	}

	public boolean validateToken(String token) {
		parseClaims(token);
		return true;
	}

	public Long extractUserId(String token) {
		return Long.parseLong(parseClaims(token).getSubject());
	}

	public long getAccessTokenExpirationSeconds() {
		return accessTokenExpiration.getSeconds();
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
