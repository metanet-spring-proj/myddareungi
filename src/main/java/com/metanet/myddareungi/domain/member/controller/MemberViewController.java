package com.metanet.myddareungi.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class MemberViewController {

	@GetMapping("/login")
	public String loginPage() {
		return "member/login";
	}
}
