package com.cts.ems.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cts.ems.entity.User;
import com.cts.ems.enums.Role;
import com.cts.ems.exception.UserNotFoundException;
import com.cts.ems.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtilService jwtService;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setDept("IT");
        user.setMobileNo("1234567890");
        user.setPassword(encoder.encode("password"));
        user.setRoles(Collections.singleton(Role.EMPLOYEE));
    }

    @Test
    public void testRegister_UserAlreadyExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.register("testuser", "testuser@example.com", "password", "IT", "1234567890", Role.EMPLOYEE);
        });

        assertEquals("Username already exists. Please choose a different username.", exception.getMessage());
    }

    @Test
    public void testRegister_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.register("testuser", "testuser@example.com", "password", "IT", "1234567890", Role.EMPLOYEE);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("testuser@example.com", registeredUser.getEmail());
    }

//    @Test
//    public void testLogin_Success() {
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//        when(userRepository.findByUsername("testuser")).thenReturn(user);
//        when(jwtService.generateToken(anyLong(), anyString(), anySet())).thenReturn("token");
//
//        String token = userService.login("testuser", "password");
//
//        assertNotNull(token);
//        assertEquals("token", token);
//    }

    @Test
    public void testLogin_InvalidCredentials() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new UserNotFoundException("Invalid Login Credentials..."));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.login("testuser", "wrongpassword");
        });

        assertEquals("Invalid Login Credentials...", exception.getMessage());
    }

    @Test
    public void testUpdatePassword_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updatePassword("testuser", "oldpassword", "newpassword");
        });

        assertEquals("No User Found With Username: testuser", exception.getMessage());
    }

    @Test
    public void testUpdatePassword_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updatePassword("testuser", "password", "newpassword");

        verify(userRepository).save(user);
        assertTrue(encoder.matches("newpassword", user.getPassword()));
    }

    @Test
    public void testDeleteByUsername_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteByUsername("testuser");
        });

        assertEquals("User with user name not found", exception.getMessage());
    }

    @Test
    public void testDeleteByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        userService.deleteByUsername("testuser");

        verify(userRepository).delete(user);
    }
}