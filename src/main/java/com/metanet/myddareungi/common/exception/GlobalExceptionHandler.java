package com.metanet.myddareungi.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 예외 발생시 에러페이지로
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletResponse response) {
    	// 응답이 이미 커밋된 경우 (SSE, 스트리밍 등) 뷰 렌더링 불가 → null 반환으로 건너뜀
        if (response.isCommitted()) {
            return null;
        }
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("message", ex.getMessage());
        return mav;
    }
}