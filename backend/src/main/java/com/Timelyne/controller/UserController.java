package com.Timelyne.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Timelyne.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // 👇 Old login API (no longer needed, handled by AuthController)
    // @PostMapping("/login")
    // public String login(@RequestBody User user){
    //     return userService.LoginUser(user);
    // }

    // In future: add user-related APIs like /profile, /update, etc.
}
