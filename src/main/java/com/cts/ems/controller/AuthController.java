package com.cts.ems.controller;

import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cts.ems.dto.LoginRequestDTO;
import com.cts.ems.dto.UserRequest;
import com.cts.ems.enums.Role;
import com.cts.ems.exception.UserNotFoundException;
import com.cts.ems.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth") //maps http requests to handler methods
public class AuthController {

	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	UserService userService;
	
	//user can register only if the role is admin or manager
	
	@PostMapping("/register")
	public String registerUser(@RequestBody UserRequest request) {
	    
			logger.info("Received registration request for username:{}",request.getUsername());
			
			if(request.getRole().equals(Role.EMPLOYEE)) {
				
				logger.warn("Employee tried to register themselves:{}",request.getUsername());
				
				return "Employee cannot register themselves.Contact a Manager";
			}
			
	        userService.register(request.getUsername(), request.getEmail(), request.getPassword(), request.getDept(),request.getMobileNo(),request.getRole());
	        
	        logger.info("User {} registered successsfully with role {}",request.getUsername(),request.getRole());
	        return "User registered successfully";
	    
	}
	
	//user can login using username and password
	
	@PostMapping("/login")
	public String loginUser(@RequestBody LoginRequestDTO request) {
	    try {
	        return userService.login(request.getUsername(), request.getPassword());
	    } 
	    catch (UserNotFoundException e) {
	        throw new RuntimeException("User not found: " + e.getMessage());
	    }
	    
	}
	
	@PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>("Logout successful", HttpStatus.OK);
    }
	
	@PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            try {
                userService.updatePassword(username, oldPassword, newPassword);
                logger.info("Password updated successfully for user: {}", username);
                return ResponseEntity.ok("{\"message\": \"Password updated successfully\"}");
            } catch (UserNotFoundException e) {
                logger.error("Error updating password for user {}: User not found", username, e.getMessage());
                return new ResponseEntity<>("{\"error\": \"User not found\"}", HttpStatus.NOT_FOUND);
            } catch (RuntimeException e) {
                logger.error("Error updating password for user {}: {}", username, e.getMessage());
                return new ResponseEntity<>("{\"error\": \"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
            }
        } else {
            logger.warn("Unauthorized attempt to update password.");
            return new ResponseEntity<>("{\"error\": \"Unauthorized\"}", HttpStatus.UNAUTHORIZED);
        }
    }
	
}
