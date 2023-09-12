package com.user_authenticationn.service.impl;

import com.user_authenticationn.entity.PasswordResetToken;
import com.user_authenticationn.entity.User;
import com.user_authenticationn.exception.InvalidTokenException;
import com.user_authenticationn.repository.PasswordResetTokenRepository;
import com.user_authenticationn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public PasswordResetToken createPasswordResetToken(User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(calculateExpiryDate());
        tokenRepository.save(token);
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token);
        if (passwordResetToken == null || passwordResetToken.getExpiryDate().before(new Date())) {
            // Handle expired or invalid tokens
            throw new InvalidTokenException("Invalid or expired password reset token.");
        }

        User user = passwordResetToken.getUser();
        // Update the user's password with the new one
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        // Delete the used token
        tokenRepository.delete(passwordResetToken);
    }

    private Date calculateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1); // Set the token to expire in 1 hour
        return calendar.getTime();
    }
}
