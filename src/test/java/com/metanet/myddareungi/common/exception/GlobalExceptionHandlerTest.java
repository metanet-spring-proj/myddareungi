package com.metanet.myddareungi.common.exception;

import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.config.LocaleConfig;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionHandlerTest.FakeController.class)
@Import({ SecurityConfig.class, LocaleConfig.class, GlobalExceptionHandlerTest.FakeController.class })
class GlobalExceptionHandlerTest {

    @Controller
    static class FakeController {
        @GetMapping("/test-error")
        public String throwException() {
            throw new RuntimeException("테스트 에러 메시지입니다.");
        }
    }

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    JwtCookieUtils jwtCookieUtils;

    @MockitoBean
    MemberAuthService memberAuthService;

    @Test
    @WithMockUser
    @DisplayName("예외 발생 시 error/error 뷰를 반환한다")
    void exception_에러뷰_반환() throws Exception {
        mockMvc.perform(get("/test-error"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"));
    }

    @Test
    @WithMockUser
    @DisplayName("예외 발생 시 model에 message 속성이 담긴다")
    void exception_모델에_메시지_포함() throws Exception {
        mockMvc.perform(get("/test-error"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"));
    }

    @Test
    @WithMockUser
    @DisplayName("예외 발생 시 에러 메시지가 응답 본문에 렌더링된다")
    void exception_메시지_렌더링() throws Exception {
        mockMvc.perform(get("/test-error"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("테스트 에러 메시지입니다.")));
    }
}