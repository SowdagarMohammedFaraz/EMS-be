package com.cts.ems.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
 
import com.cts.ems.entity.User;
import com.cts.ems.enums.Role;
import com.cts.ems.repository.UserRepository;
import com.cts.ems.service.CustomUserDetailsService;
 
import java.util.Set;
 
public class CustomUserDetailsServiceTest {
 
    @Mock
    private UserRepository userRepository;
 
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testLoadUserByUsername_UserExists() {
        // Arrange
    	User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Set.of(Role.MANAGER)); // Using Role.MANAGER
 
        when(userRepository.findByUsername("testuser")).thenReturn(user);
 
        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");
 
        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("MANAGER")));
    }
 
    @Test
    public void testLoadUserByUsername_UserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);
 
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistentuser");
        });
    }
}
