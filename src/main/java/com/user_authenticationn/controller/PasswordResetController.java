package com.user_authenticationn.controller;

import com.user_authenticationn.entity.PasswordResetToken;
import com.user_authenticationn.entity.User;
import com.user_authenticationn.exception.InvalidTokenException;
import com.user_authenticationn.payload.PasswordResetRequestDTO;
import com.user_authenticationn.repository.UserRepository;
import com.user_authenticationn.service.EmailService;
import com.user_authenticationn.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // Implement this service to send emails

    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            // Handle the case where the email doesn't exist in the database
            return new ResponseEntity<>("Email not found", HttpStatus.BAD_REQUEST);
        }

        PasswordResetToken token = passwordResetService.createPasswordResetToken(user);

        // Send an email with the password reset link
        String resetLink = "http://yourapp.com/reset-password?token=" + token.getToken();
        String emailContent = "Click the following link to reset your password: " + resetLink;
        emailService.sendEmail(email, "Password Reset Request", emailContent);

        return new ResponseEntity<>("Password reset instructions sent to your email.", HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequestDTO requestDTO) {
        try {
            passwordResetService.resetPassword(requestDTO.getToken(), requestDTO.getPassword());
            return new ResponseEntity<>("Password reset successful.", HttpStatus.OK);
        } catch (InvalidTokenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
