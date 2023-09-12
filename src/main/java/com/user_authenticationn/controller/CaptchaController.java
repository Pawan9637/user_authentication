package com.user_authenticationn.controller;

import com.user_authenticationn.security.CaptchaGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class CaptchaController {
    @GetMapping("/captcha")
    @ResponseBody
    public String captcha(HttpSession session) {
        String captcha = CaptchaGenerator.generateCaptcha();
        session.setAttribute("captcha", captcha);

        return captcha;
    }
}
