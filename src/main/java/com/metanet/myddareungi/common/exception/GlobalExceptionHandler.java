package com.metanet.myddareungi.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 401 - 비밀번호 불일치 (에러페이지 말고 상태코드 반환)
    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    // 나머지 예외는 에러페이지로
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("message", ex.getMessage());
        return mav;
    }
}