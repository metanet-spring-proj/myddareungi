package com.metanet.myddareungi.common.exception.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
    @GetMapping("/error/denied")
    public ModelAndView accessDenied(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("message", request.getAttribute("message"));
        return mav;
    }
}