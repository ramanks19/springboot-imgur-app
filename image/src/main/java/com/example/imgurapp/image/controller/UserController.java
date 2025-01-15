package com.example.imgurapp.image.controller;

import com.example.imgurapp.image.entity.User;
import com.example.imgurapp.image.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A Controller class to create, authenticate and fetch user
 * */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registers a new user
     *
     * @param user The user object from the user body, validated by using @Valid
     * @return A Response Entity containing the newly created user and an HTTP Status of 201
     * */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User newUser = userService.registerUser(user.getName(), user.getUserName(),
                user.getEmail(), user.getPassword());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Authenticates a user
     *
     * @param userName The userName of the user
     * @param password The password of the user
     * @return A ResponseEntity with a success message and HttpStatus of 200 if the user exists.
     *         If not, then an error message with an HttpStatus of 401
     * */
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestParam String userName,
                                                   @RequestParam String password) {
        return userService.authenticateUser(userName, password)
                .map(user -> ResponseEntity.ok("Authentication Successful"))
                .orElse(ResponseEntity.status(401).body("Invalid userName or password"));
    }

    /**
     * Fetches user details
     *
     * @param userName The userName of the user
     * @return A ResponseEntity containing the user object and HttpStatus of 200, if the user exists.
     *         If it doesn't a HttpStatus of 404 is returned
     * */
    @GetMapping("/{userName}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userName) {
        return userService.getUserByUserName(userName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
