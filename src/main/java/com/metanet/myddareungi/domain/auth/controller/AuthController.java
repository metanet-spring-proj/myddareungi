package com.metanet.myddareungi.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.metanet.myddareungi.domain.member.dto.LoginRequestDto;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Profile("!view")
@Tag(name = "인증")
@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final MemberAuthService memberAuthService;

	public AuthController(MemberAuthService memberAuthService) {
		this.memberAuthService = memberAuthService;
	}

	@Operation(summary = "로그인", description = "LOGIN_ID와 PASSWORD를 확인한 뒤 대시보드로 리다이렉트합니다.")
	@PostMapping("/login")
	public String login(
		@Valid @ModelAttribute("loginRequest") LoginRequestDto loginRequest,
		BindingResult bindingResult,
		HttpServletRequest request
	) {
		if (bindingResult.hasErrors()) {
			return "redirect:/login?error=1";
		}

		try {
			Member member = memberAuthService.authenticate(loginRequest.getLoginId(), loginRequest.getPassword());
			UserDetails userDetails = memberAuthService.toUserDetails(member);
			Authentication authentication = org.springframework.security.authentication.UsernamePasswordAuthenticationToken
				.authenticated(userDetails, null, userDetails.getAuthorities());

			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication);

			HttpSession session = request.getSession(true);
			request.changeSessionId();
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
			SecurityContextHolder.setContext(context);

			return "redirect:/dashboard";
		} catch (AuthenticationException ex) {
			SecurityContextHolder.clearContext();
			return "redirect:/login?error=1";
		}
	}
}
