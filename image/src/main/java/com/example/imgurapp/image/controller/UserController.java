package com.example.imgurapp.image.controller;

import com.example.imgurapp.image.entity.User;
import com.example.imgurapp.image.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User newUser = userService.registerUser(user.getName(), user.getUserName(),
                user.getEmail(), user.getPassword());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestParam String userName,
                                                   @RequestParam String password) {
        return userService.authenticateUser(userName, password)
                .map(user -> ResponseEntity.ok("Authentication Successful"))
                .orElse(ResponseEntity.status(401).body("Invalid userName or password"));
    }

    @GetMapping("/{userName}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userName) {
        return userService.getUserByUserName(userName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
