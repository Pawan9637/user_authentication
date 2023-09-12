package com.user_authenticationn.controller;

import com.user_authenticationn.entity.User;
import com.user_authenticationn.payload.LoginDto;
import com.user_authenticationn.payload.SignUpDto;
import com.user_authenticationn.repository.RoleRepository;
import com.user_authenticationn.repository.UserRepository;
import com.user_authenticationn.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginAttemptService loginAttemptService;

    private final int MAX_ATTEMPTS = 3;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        if (loginAttemptService.isBlocked(loginDto.getUsernameOrEmail())) {
            return new ResponseEntity<>("Account is temporarily locked due to multiple failed login attempts.", HttpStatus.BAD_REQUEST);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            loginAttemptService.loginSucceeded(loginDto.getUsernameOrEmail());
            return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
        } catch (AuthenticationException e) {
            // Authentication failed, handle the error
            loginAttemptService.loginFailed(loginDto.getUsernameOrEmail());

            int attemptsLeft = MAX_ATTEMPTS - loginAttemptService.getAttempts(loginDto.getUsernameOrEmail());

            if (attemptsLeft > 0) {
                String message = "Incorrect password. You have " + attemptsLeft + " attempts left.";
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            } else {
                // If no attempts left, lock the account
                return new ResponseEntity<>("Account is temporarily locked due to multiple failed login attempts.", HttpStatus.BAD_REQUEST);
            }
        }
    }



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        Boolean emailExist = userRepository.existsByEmail(signUpDto.getEmail());
        if (emailExist) {
            return new ResponseEntity<>("Email id is already taken", HttpStatus.BAD_REQUEST);
        }

        Boolean userNameExist = userRepository.existsByUsername(signUpDto.getUsername());
        if (userNameExist) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        User user = new User();

        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());

        // Encode the user's password using BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        return new ResponseEntity<>("User is registered", HttpStatus.CREATED);
    }

    @PostMapping("/api/logout")
    public String logout() {
        // Invalidate the user's session
        SecurityContextHolder.clearContext();

        return "Logout successful";
    }

}
