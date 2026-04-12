package com.metanet.myddareungi.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.metanet.myddareungi.domain.admin.dto.AdminDashboardDto;
import com.metanet.myddareungi.domain.admin.service.IAdminService;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final IAdminService adminService;
    private final MemberAuthService memberAuthService;
    @GetMapping("/mypage")
    public String adminMyPage(Model model, Principal principal,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        String loginId = principal.getName();
        Member member = memberAuthService.getMember(loginId);
        long userId = member.getUserId();

        AdminDashboardDto dashboardData = adminService.getDashboardData(userId, page, size);

        model.addAttribute("adminId", dashboardData.getAdminId());
        model.addAttribute("adminName", dashboardData.getAdminName());
        model.addAttribute("adminEmail", dashboardData.getAdminEmail());
        model.addAttribute("pendingCount", dashboardData.getPendingCount());
        model.addAttribute("todayUploadCount", dashboardData.getTodayUploadCount());
        model.addAttribute("pendingFiles", dashboardData.getPendingFiles());
        model.addAttribute("currentPage", dashboardData.getCurrentPage());
        model.addAttribute("totalPages", dashboardData.getTotalPages());

        // 10개 블록 계산
        int blockSize = 10;
        int blockStart = (page / blockSize) * blockSize;
        int blockEnd = Math.min(blockStart + blockSize - 1, dashboardData.getTotalPages() - 1);
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = blockStart; i <= blockEnd; i++) pageNumbers.add(i);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("activeMenu", "mypage");

        return "admin/mypage";
    }
}
