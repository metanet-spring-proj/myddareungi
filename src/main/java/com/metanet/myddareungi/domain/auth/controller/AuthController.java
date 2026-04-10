package com.metanet.myddareungi.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.metanet.myddareungi.domain.member.dto.LoginRequestDto;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Profile("!view")
@Tag(name = "인증")
@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final MemberAuthService memberAuthService;
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtCookieUtils jwtCookieUtils;

	public AuthController(
		MemberAuthService memberAuthService,
		JwtTokenProvider jwtTokenProvider,
		JwtCookieUtils jwtCookieUtils
	) {
		this.memberAuthService = memberAuthService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.jwtCookieUtils = jwtCookieUtils;
	}

	@Operation(summary = "로그인", description = "LOGIN_ID와 PASSWORD를 확인한 뒤 대시보드로 리다이렉트합니다.")
	@PostMapping("/login")
	public String login(
		@Valid @ModelAttribute("loginRequest") LoginRequestDto loginRequest,
		BindingResult bindingResult,
		HttpServletResponse response
	) {
		if (bindingResult.hasErrors()) {
			return "redirect:/login?error=1";
		}

		try {
			Member member = memberAuthService.authenticate(loginRequest.getLoginId(), loginRequest.getPassword());
			String accessToken = jwtTokenProvider.createAccessToken(member);
			response.addHeader(
				"Set-Cookie",
				jwtCookieUtils.createAccessTokenCookie(
					accessToken,
					jwtTokenProvider.getAccessTokenExpirationSeconds()
				).toString()
			);

			return "redirect:/dashboard";
		} catch (AuthenticationException ex) {
			return "redirect:/login?error=1";
		}
	}
}
