package com.Timelyne.controller;

import com.Timelyne.model.User;
import com.Timelyne.service.UserService;
import com.Timelyne.dto.AuthRequest;
import com.Timelyne.dto.AuthResponse;
import com.Timelyne.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    
    @Autowired
    private UserService userService;  
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public User signup(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request){
        User user = userService.getByUsername(request.getUsername());

        if(!userService.checkPassword(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
    

}
