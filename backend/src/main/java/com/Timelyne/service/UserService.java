package com.Timelyne.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Timelyne.model.User;
import com.Timelyne.repository.UserRepository;

@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String registerUser(User user){
        if(userRepository.findByEmail(user.getEmail()) !=null){
            return "Email already exists";
        }
        String encrypted=passwordEncoder.encode(user.getPassword());
        user.setPassword(encrypted);
        
        userRepository.save(user);
        return "User registered with encrypted password successfully";
    }

    public String LoginUser(User user){
        User existingUser = userRepository.findByEmail(user.getEmail());
        // No code needed here for login logic, proceed to user existence check
        if(existingUser==null){
            return "User not found";
        }


        if(!passwordEncoder.matches(user.getPassword(),existingUser.getPassword())){
            return "Invalid password";
        } else {
            return "Login successful";
        }
    }

}
