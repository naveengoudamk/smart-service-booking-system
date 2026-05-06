package com.navi.servicebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.navi.servicebooking.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}