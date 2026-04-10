package com.metanet.myddareungi.domain.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class LandingPageController {

	@GetMapping({"/", "/home"})
	public String landingPage() {
		return "home/landing";
	}
}
