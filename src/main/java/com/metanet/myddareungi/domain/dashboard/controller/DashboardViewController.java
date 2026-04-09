package com.metanet.myddareungi.domain.dashboard.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardViewController {

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {
		model.addAttribute("loginId", authentication.getName());
		return "dashboard/index";
	}
}
