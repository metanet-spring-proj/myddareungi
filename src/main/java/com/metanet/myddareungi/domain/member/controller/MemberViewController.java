package com.metanet.myddareungi.domain.member.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class MemberViewController {

	@GetMapping("/login")
	public String loginPage(Authentication authentication) {
		if (isAuthenticated(authentication)) {
			return "redirect:/dashboard";
		}
		return "member/login";
	}

	@GetMapping("/signup")
	public String signupPage(Authentication authentication) {
		if (isAuthenticated(authentication)) {
			return "redirect:/dashboard";
		}
		return "member/signup";
	}

	private boolean isAuthenticated(Authentication authentication) {
		return authentication != null
			&& authentication.isAuthenticated()
			&& !(authentication instanceof AnonymousAuthenticationToken);
	}
}
