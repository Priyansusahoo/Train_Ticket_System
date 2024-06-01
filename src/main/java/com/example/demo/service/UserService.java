package com.example.demo.service;

import com.example.demo.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    Users createUser(Users users);

    Optional<Users> getUserById(Long id);

    Boolean deleteUser(Long id);
}
