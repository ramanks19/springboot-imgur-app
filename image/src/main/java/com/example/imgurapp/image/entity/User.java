package com.example.imgurapp.image.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "APP_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotBlank(message = "User name cannot be blank")
    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String userName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be blank")
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

}
