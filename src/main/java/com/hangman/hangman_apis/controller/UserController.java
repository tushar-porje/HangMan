package com.hangman.hangman_apis.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hangman.hangman_apis.dto.LoginDto;
import com.hangman.hangman_apis.dto.UserDto;
import com.hangman.hangman_apis.entity.User;
import com.hangman.hangman_apis.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        User user = userService.registerUser(userDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        Optional<User> userOptional = userService.findByUsername(loginDto.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }
}
