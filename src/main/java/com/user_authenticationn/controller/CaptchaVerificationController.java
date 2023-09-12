package com.user_authenticationn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class CaptchaVerificationController {
    @PostMapping("/verify-captcha")
    public ModelAndView verifyCaptcha(@RequestParam String captchaInput, HttpSession session) {
        String storedCaptcha = (String) session.getAttribute("captcha");
        if (captchaInput.equalsIgnoreCase(storedCaptcha)) {
            // CAPTCHA is correct
            return new ModelAndView("success");
        } else {
            // CAPTCHA is incorrect
            return new ModelAndView("error");
        }
    }
}
