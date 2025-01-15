package com.example.imgurapp.image.service;

import com.example.imgurapp.image.entity.User;
import com.example.imgurapp.image.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(String name, String userName, String email, String password) {
        log.info("Creating an user with userName: {}", userName);
        if (userRepository.findByUserName(userName).isPresent()) {
            log.error("User with this username already exists. Provide a new user name.");
            throw new RuntimeException("Username already exists.");
        }

        User user = new User();
        user.setName(name);
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        log.info("User with username {} is created.", userName);
        return savedUser;
    }

    public Optional<User> authenticateUser(String userName, String password) {
        log.info("Authenticating user: {}", userName);
        Optional<User> userOpt = userRepository.findByUserName(userName);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            log.info("User found");
            return userOpt;
        } else {
            log.error("Authentication failed for user: {}", userName);
            return Optional.empty();
        }
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
