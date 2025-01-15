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

    /**
     * Registers a user by checking if the username already exists or not
     *
     * @param name The name of the user
     * @param userName The username for the user. It should be unique
     * @param email The email of the user
     * @param password The password for the user
     * @return The created User object
     * @throws RuntimeException If the username provided by the user already exists.
     *
     * */
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

    /**
     * Authenticates an user by its userName and password
     *
     * @param userName The userName of the user
     * @param password The password provided by the user
     * @return An optional containing the User if it is successfully authenticated, otherwise an
     * empty body is returned
     * */
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

    /**
     * Retrieves a user by its userName
     *
     * @param userName The user name of the user
     * @return An optional containing the User object if found
     * */
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
