package com.metanet.myddareungi.domain.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.metanet.myddareungi.domain.admin.dto.AdminDashboardDto;
import com.metanet.myddareungi.domain.admin.service.IAdminService;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
// @PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final IAdminService adminService;
    private final MemberAuthService memberAuthService;

    @GetMapping("/mypage")
    public String adminMyPage(Model model, Principal principal) {
        String loginId = principal.getName();
        Member member = memberAuthService.getMember(loginId);
        long userId = member.getUserId();

        AdminDashboardDto dashboardData = adminService.getDashboardData(userId);

        model.addAttribute("adminId", dashboardData.getAdminId());
        model.addAttribute("adminName", dashboardData.getAdminName());
        model.addAttribute("adminEmail", dashboardData.getAdminEmail());
        model.addAttribute("pendingCount", dashboardData.getPendingCount());
        model.addAttribute("todayUploadCount", dashboardData.getTodayUploadCount());
        model.addAttribute("pendingFiles", dashboardData.getPendingFiles());
        model.addAttribute("activeMenu", "mypage");

        return "admin/mypage";
    }
}
