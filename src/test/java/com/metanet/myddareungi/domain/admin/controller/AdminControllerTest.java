package com.metanet.myddareungi.domain.admin.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.domain.admin.dto.AdminDashboardDto;
import com.metanet.myddareungi.domain.admin.service.IAdminService;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;

@WebMvcTest(AdminController.class)
@Import({ SecurityConfig.class, AdminControllerTest.TestConfig.class })
public class AdminControllerTest {

        @TestConfiguration
        @EnableMethodSecurity
        static class TestConfig {
        }

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private IAdminService adminService;

        @MockitoBean
        private MemberAuthService memberAuthService;

        @MockitoBean
        private JwtTokenProvider jwtTokenProvider;

        @MockitoBean
        private JwtCookieUtils jwtCookieUtils;

        @Test
        @WithMockUser(username = "adminUser", roles = { "ADMIN" })
        @DisplayName("관리자 마이페이지 접근 테스트 - 권한이 있는 경우")
        void adminMyPageAccessSuccess() throws Exception {
                Member mockMember = Member.builder()
                                .userId(1L)
                                .loginId("adminUser")
                                .userName("관리자")
                                .build();

                AdminDashboardDto mockDashboardData = new AdminDashboardDto();
                mockDashboardData.setAdminId(1L);
                mockDashboardData.setAdminName("관리자");
                mockDashboardData.setAdminEmail("admin@metanet.com");
                mockDashboardData.setPendingCount(5);
                mockDashboardData.setTodayUploadCount(10);
                mockDashboardData.setPendingFiles(new ArrayList<>());

                given(memberAuthService.getMember("adminUser")).willReturn(mockMember);
                given(adminService.getDashboardData(1L)).willReturn(mockDashboardData);

                mockMvc.perform(get("/admin/mypage"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(view().name("admin/mypage"))
                                .andExpect(model().attribute("adminName", "관리자"))
                                .andExpect(model().attribute("pendingCount", 5))
                                .andExpect(model().attribute("activeMenu", "mypage"));
        }

        @Test
        @WithMockUser(username = "normalUser", roles = { "USER" })
        @DisplayName("관리자 마이페이지 접근 테스트 - 권한이 없는 경우(403)")
        void adminMyPageAccessDenied() throws Exception {

                Member mockMember = Member.builder()
                                .userId(2L)
                                .loginId("normalUser")
                                .userName("일반사용자")
                                .build();
                given(memberAuthService.getMember("normalUser")).willReturn(mockMember);
                given(adminService.getDashboardData(2L)).willReturn(new AdminDashboardDto());

                mockMvc.perform(get("/admin/mypage"))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }
}
