package com.example.imgurapp.image.service;

import com.example.imgurapp.image.entity.User;
import com.example.imgurapp.image.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegsiterUserSuccess() {
        String userName = "john123";
        String name = "John Doe";
        String email = "john@example.com";
        String password = "password123";

        User mockUser = new User();
        mockUser.setUserName(userName);
        mockUser.setName(name);
        mockUser.setEmail(email);
        mockUser.setPassword(password);

        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User result = userService.registerUser(userName, name, email, password);

        assertNotNull(result);
        assertEquals(userName, result.getUserName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testAuthenticateUserFailureByUserNotFound() {
        String userName = "john123";
        String password = "wrongPassword";

        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        Optional<User> result = userService.authenticateUser(userName, password);

        assertFalse(result.isPresent());
    }
}
