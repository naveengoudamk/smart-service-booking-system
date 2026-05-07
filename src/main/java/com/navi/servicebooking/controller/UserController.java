package com.navi.servicebooking.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/test")
    public String test() {
        return "User API working ✅";
    }
}