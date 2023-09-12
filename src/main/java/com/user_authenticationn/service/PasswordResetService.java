package com.user_authenticationn.service;

import com.user_authenticationn.entity.PasswordResetToken;
import com.user_authenticationn.entity.User;

public interface PasswordResetService {
    PasswordResetToken createPasswordResetToken(User user);
    void resetPassword(String token, String newPassword);
    // Other methods
}
