package com.metanet.myddareungi.domain.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardPageController {

    @GetMapping
    public String dashboardPage(org.springframework.ui.Model model) {
        model.addAttribute("activeMenu", "dashboard");
        return "dashboard/dashboard";
    }
}