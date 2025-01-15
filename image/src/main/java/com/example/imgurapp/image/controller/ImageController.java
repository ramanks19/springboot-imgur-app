package com.example.imgurapp.image.controller;

import com.example.imgurapp.image.entity.Image;
import com.example.imgurapp.image.entity.User;
import com.example.imgurapp.image.service.ImageService;
import com.example.imgurapp.image.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(@RequestParam String userName,
                                             @RequestParam String password,
                                             @RequestParam("file") MultipartFile file) {
        //Authenticate User
        Optional<User> userOpt = userService.authenticateUser(userName, password);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = userOpt.get();
        Image uploadedImage = imageService.uploadImage(user, file);
        return new ResponseEntity<>(uploadedImage, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<Image>> getImagesByUser(@PathVariable String userName,
                                                       @RequestParam String password) {
        //Authenticate User
        Optional<User> userOpt = userService.authenticateUser(userName, password);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = userOpt.get();
        List<Image> images = imageService.getImagesByUser(user);
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<Void> deleteImage(@RequestParam String userName,
                                            @RequestParam String password,
                                            @PathVariable Long imageId) {
        //Authenticate User
        Optional<User> userOpt = userService.authenticateUser(userName, password);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = userOpt.get();
        Optional<Image> imageOpt = imageService.getImagesByUser(user).stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst();

        if (imageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        imageService.deleteImage(imageOpt.get());
        return ResponseEntity.noContent().build();
    }
}
