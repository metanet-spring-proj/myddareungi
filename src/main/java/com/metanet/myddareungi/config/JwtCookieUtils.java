package com.metanet.myddareungi.config;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@Profile("!view")
public class JwtCookieUtils {

	public static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

	private final boolean secureCookie;

	public JwtCookieUtils(@Value("${jwt.cookie.secure:false}") boolean secureCookie) {
		this.secureCookie = secureCookie;
	}

	public ResponseCookie createAccessTokenCookie(String token, long maxAgeSeconds) {
		return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, token)
			.httpOnly(true)
			.secure(secureCookie)
			.path("/")
			.sameSite("Lax")
			.maxAge(maxAgeSeconds)
			.build();
	}

	public ResponseCookie createExpiredAccessTokenCookie() {
		return ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, "")
			.httpOnly(true)
			.secure(secureCookie)
			.path("/")
			.sameSite("Lax")
			.maxAge(0)
			.build();
	}

	public Optional<String> resolveAccessToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return Optional.empty();
		}

		return Arrays.stream(cookies)
			.filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
			.map(Cookie::getValue)
			.filter(value -> value != null && !value.isBlank())
			.findFirst();
	}
}
