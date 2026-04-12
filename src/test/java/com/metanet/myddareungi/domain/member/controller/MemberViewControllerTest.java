package com.metanet.myddareungi.domain.member.controller;

import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.config.LocaleConfig;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.domain.files.service.IUploadFileService;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseCookie;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MemberViewController.class)
@Import({ SecurityConfig.class, LocaleConfig.class })
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

    @Test
    @DisplayName("GET /signup - 비로그인 사용자 → 회원가입 페이지 반환")
    void signupPage_비로그인_회원가입페이지() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/signup"));
    }

    @Test
    @DisplayName("GET /login?logout=1 - 로그아웃 안내 메시지 반환")
    void loginPage_로그아웃_안내메시지() throws Exception {
        mockMvc.perform(get("/login").param("logout", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/login"))
                .andExpect(content().string(containsString("로그아웃되었습니다. 다시 로그인해주세요.")));
    }

    @Test
    @DisplayName("GET /login?logout=1&lang=en - 영문 로그아웃 안내 메시지 반환")
    void loginPage_영문_로그아웃_안내메시지() throws Exception {
        mockMvc.perform(get("/login").param("logout", "1").param("lang", "en"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/login"))
                .andExpect(content().string(containsString("You have been signed out. Please log in again.")));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /login?logout=1 - 인증된 사용자도 로그인 페이지를 본다")
    void loginPage_로그인상태_로그아웃파라미터_로그인페이지() throws Exception {
        mockMvc.perform(get("/login").param("logout", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/login"));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("GET /mypage - 로그인 사용자 → 마이페이지 반환")
    void myPage_로그인_마이페이지() throws Exception {
        stubMyPageUser("testUser");

        mockMvc.perform(get("/mypage"))
                .andExpect(status().isOk())
                .andExpect(view().name("mypage/user-mypage"))
                .andExpect(model().attribute("userName", "홍길동"))
                .andExpect(model().attribute("userEmail", "test@test.com"));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("GET /mypage - 사이드바에 로그아웃 버튼과 스크립트가 렌더링된다")
    void myPage_로그인_로그아웃버튼렌더링() throws Exception {
        stubMyPageUser("testUser");

        mockMvc.perform(get("/mypage"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data-logout-url=\"/api/v1/auth/logout\"")))
                .andExpect(content().string(containsString("data-login-url=\"/login?logout=1\"")))
                .andExpect(content().string(containsString("/js/common/logout.js")));
    }

    @Test
    @DisplayName("GET /mypage - 비로그인 사용자 → 로그인 페이지로 리디렉션")
    void myPage_비로그인_로그인리디렉션() throws Exception {
        mockMvc.perform(get("/mypage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("GET /mypage/edit - 로그인 사용자 → 수정 페이지 반환")
    void updatePage_로그인_수정페이지() throws Exception {
        Member fakeMember = Member.builder()
                .userId(1L)
                .loginId("testUser")
                .userName("홍길동")
                .role("USER")
                .build();
        when(memberAuthService.getMember("testUser")).thenReturn(fakeMember);

        mockMvc.perform(get("/mypage/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/update"))
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attributeExists("userRole"));
    }

    @Test
    @DisplayName("GET /mypage/edit - 비로그인 사용자 → 로그인 페이지로 리디렉션")
    void updatePage_비로그인_로그인리디렉션() throws Exception {
        mockMvc.perform(get("/mypage/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/logout - XSRF 쿠키와 헤더로 요청 시 쿠키를 삭제하고 로그인 페이지로 리다이렉트")
    void logout_xsrf쿠키와헤더_쿠키삭제_로그인리디렉트() throws Exception {
        when(jwtCookieUtils.createExpiredAccessTokenCookie()).thenReturn(expiredAccessTokenCookie());
        Cookie csrfCookie = issueCsrfCookie("testUser");

        mockMvc.perform(post("/api/v1/auth/logout")
                        .with(user("testUser"))
                        .cookie(csrfCookie)
                        .header("X-XSRF-TOKEN", csrfCookie.getValue()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout=1"))
                .andExpect(accessTokenCookieExpired());
    }

    @Test
    @DisplayName("POST /api/v1/auth/logout - CSRF 헤더가 없으면 거부된다")
    void logout_csrf없음_거부() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .with(user("testUser")))
                .andExpect(status().isForbidden());
    }

    private ResultMatcher accessTokenCookieExpired() {
        return header().stringValues(
                "Set-Cookie",
                hasItem(allOf(
                        containsString("ACCESS_TOKEN="),
                        containsString("Max-Age=0"))));
    }

    private ResponseCookie expiredAccessTokenCookie() {
        return ResponseCookie.from("ACCESS_TOKEN", "")
                .path("/")
                .maxAge(0)
                .build();
    }

    private void stubMyPageUser(String username) {
        Member fakeMember = Member.builder()
                .userId(1L)
                .loginId(username)
                .userName("홍길동")
                .email("test@test.com")
                .role("USER")
                .build();

        when(memberAuthService.getMember(username)).thenReturn(fakeMember);
        when(uploadFileService.countFilesByUploaderId(1L)).thenReturn(0);
        when(uploadFileService.getFilesByUploaderIdPaged(1L, 0, 5)).thenReturn(List.of());
    }

    private Cookie issueCsrfCookie(String username) throws Exception {
        stubMyPageUser(username);

        MvcResult result = mockMvc.perform(get("/mypage").with(user(username)))
                .andExpect(status().isOk())
                .andReturn();

        Cookie csrfCookie = result.getResponse().getCookie("XSRF-TOKEN");
        assertNotNull(csrfCookie);
        return csrfCookie;
    }
}
