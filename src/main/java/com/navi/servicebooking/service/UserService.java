package com.navi.servicebooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navi.servicebooking.entity.User;
import com.navi.servicebooking.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {

        User existing = userRepository.findByEmail(user.getEmail());

        if (existing != null) {
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(user);
    }
    
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}