package com.navi.servicebooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.navi.servicebooking.entity.User;
import com.navi.servicebooking.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setRole("USER");
        userService.registerUser(user);
        return "User registered successfully";
    }
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        userService.loginUser(user.getEmail(), user.getPassword());

        return "Login successful";
    }
}