package com.metanet.myddareungi.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.metanet.myddareungi.domain.admin.dto.AdminDashboardDto;
import com.metanet.myddareungi.domain.admin.service.IAdminService;

@Controller
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/mypage")
    public String adminMyPage(Model model, Principal principal) {
        // [JWT 분기 처리 가이드]
        // 1. Spring Security의 Authentication 객체에서 권한을 확인하여 분기 가능
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // if (auth.getAuthorities().stream().noneMatch(a ->
        // a.getAuthority().equals("ROLE_ADMIN"))) {
        // return "redirect:/user/mypage"; // 일반 유저 페이지로 리다이렉트
        // }

        // [JWT 연동 준비]
        // 운영 시에는 JWT 토큰에서 userId를 추출하여 사용합니다.
        // 현재는 개발 및 테스트를 위해 mockUserId(예: 42)를 사용합니다.
        long userId = 1;

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
