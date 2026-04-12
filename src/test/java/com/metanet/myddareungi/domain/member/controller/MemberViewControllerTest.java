package com.metanet.myddareungi.domain.member.controller;

import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.domain.files.service.IUploadFileService;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberViewController.class)
@Import(SecurityConfig.class)
class MemberViewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    MemberAuthService memberAuthService;

    @MockitoBean
    IUploadFileService uploadFileService;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    JwtCookieUtils jwtCookieUtils;

    // ── GET /login ────────────────────────────
    @Test
    @DisplayName("GET /login - 비로그인 사용자 → 로그인 페이지 반환")
    void loginPage_비로그인_로그인페이지() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/login"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /login - 로그인 사용자 → 대시보드로 리디렉션")
    void loginPage_로그인_대시보드리디렉션() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    // ── GET /signup ───────────────────────────
    @Test
    @DisplayName("GET /signup - 비로그인 사용자 → 회원가입 페이지 반환")
    void signupPage_비로그인_회원가입페이지() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/signup"));
    }

    // ── GET /user-mypage ──────────────────────
    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("GET /user-mypage - 로그인 사용자 → 마이페이지 반환")
    void myPage_로그인_마이페이지() throws Exception {
        // given
        Member fakeMember = Member.builder()
                .userId(1L)
                .loginId("testUser")
                .userName("홍길동")
                .email("test@test.com")
                .role("USER")
                .build();

        when(memberAuthService.getMember("testUser")).thenReturn(fakeMember);
        when(uploadFileService.getAllFilesByUploaderId(1L)).thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/user-mypage"))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/user-mypage"))
                .andExpect(model().attribute("userName", "홍길동"))
                .andExpect(model().attribute("userEmail", "test@test.com"));
    }

    @Test
    @DisplayName("GET /user-mypage - 비로그인 사용자 → 로그인 페이지로 리디렉션")
    void myPage_비로그인_로그인리디렉션() throws Exception {
        mockMvc.perform(get("/user-mypage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // ── GET /users/update ─────────────────────
    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("GET /users/update - 로그인 사용자 → 수정 페이지 반환")
    void updatePage_로그인_수정페이지() throws Exception {
        // given
        Member fakeMember = Member.builder()
                .userId(1L)
                .loginId("testUser")
                .userName("홍길동")
                .build();
        when(memberAuthService.getMember("testUser")).thenReturn(fakeMember);

        // when & then
        mockMvc.perform(get("/users/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/update"))
                .andExpect(model().attributeExists("member"));
    }

    @Test
    @DisplayName("GET /users/update - 비로그인 사용자 → 로그인 페이지로 리디렉션")
    void updatePage_비로그인_로그인리디렉션() throws Exception {
        mockMvc.perform(get("/users/update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}