package com.Timelyne.controller;

import com.Timelyne.model.User;
import com.Timelyne.repository.UserRepository;
import com.Timelyne.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.Timelyne.dto.LoginRequest;
import com.Timelyne.dto.SignupRequest;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 🔐 LOGIN Endpoint
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String token = jwtUtil.generateToken(request.getEmail());

            response.put("token", token);
            return response;
        } catch (AuthenticationException e) {
            response.put("error", "Invalid email or password");
            return response;
        }
    }

    // 🆕 SIGNUP Endpoint
    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody SignupRequest request) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByEmail(request.getEmail()) != null) {
            response.put("error", "Email already exists");
            return response;
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        response.put("message", "User registered successfully!");
        return response;
    }

   /*  // DTOs
    @Getter @Setter
    static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter @Setter
    static class SignupRequest {
        private String email;
        private String password;
    }  */
}
