package com.user_authenticationn.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
