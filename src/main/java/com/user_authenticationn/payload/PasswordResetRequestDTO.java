package com.user_authenticationn.payload;

public class PasswordResetRequestDTO {
    private String email;
    private String token; // Add the 'token' field here
    private String password;

    // Constructors, getters, and setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
