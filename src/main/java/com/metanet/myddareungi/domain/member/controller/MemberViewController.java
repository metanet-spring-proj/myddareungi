package com.metanet.myddareungi.domain.member.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import com.metanet.myddareungi.domain.member.service.MemberSignupService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@Hidden
@Controller
@RequiredArgsConstructor
public class MemberViewController {
	private final MemberAuthService memberAuthService;


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

	@GetMapping("/member/update")
    public String updatePage(Authentication authentication, Model model) {
        if (!isAuthenticated(authentication)) {
            return "redirect:/login";
        }
        // 로그인된 유저 정보 꺼내서 폼에 채워줌
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberAuthService.getMember(userDetails.getUsername()); 
        model.addAttribute("member", member);
        return "member/update";
    }

	private boolean isAuthenticated(Authentication authentication) {
		return authentication != null
			&& authentication.isAuthenticated()
			&& !(authentication instanceof AnonymousAuthenticationToken);
	}
}
