package com.cts.ems.service;

import java.util.Collections;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cts.ems.controller.AuthController;
import com.cts.ems.dto.UserRequest;
import com.cts.ems.entity.User;
import com.cts.ems.enums.Role;
import com.cts.ems.exception.UserNotFoundException;
import com.cts.ems.repository.UserRepository;
import com.cts.ems.repository.MilestoneRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private MilestoneRepository milestoneRepository;
	
	@Autowired
	private JwtUtilService jwtService;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public User register(String username, String email, String password, String dept, String mobileno, Role role) {
	    
		logger.info("Registering new user:{}",username);
		
	    // 1. Check if the username already exists
	    if (userrepository.findByUsername(username) != null) {
	    	logger.error("Registeration failed - username {} already exsists",username);
	        throw new IllegalArgumentException("Username already exists. Please choose a different username.");
	    }

	    // 2. Validate mandatory fields
	    if (username == null || username.isEmpty()) {
	        throw new IllegalArgumentException("Username cannot be null or empty");
	    }

	    if (email == null || email.isEmpty()) {
	        throw new IllegalArgumentException("Email cannot be null or empty");
	    }

	    if (password == null || password.isEmpty()) {
	        throw new IllegalArgumentException("Password cannot be null or empty");
	    }

	    if (role == null) {
	        throw new IllegalArgumentException("Role cannot be null");
	    }
	    
	    


	    // 3. Create and save the new user
	    User newUser = new User();
	    newUser.setUsername(username);
	    newUser.setEmail(email);
	    newUser.setDept(dept);
	    newUser.setMobileNo(mobileno);
	    newUser.setPassword(encoder.encode(password));
	    newUser.setRoles(Collections.singleton(role));

	    logger.info("User {} successfully registered with role{}",username,role);
	    
	    
	    return userrepository.save(newUser);
	}
	
	

	public String login(String username, String password) {
	    try {
	        Authentication authentication = authManager.authenticate(
	            new UsernamePasswordAuthenticationToken(username, password));
	        
	        if (authentication.isAuthenticated()) {
	            User authUser = userrepository.findByUsername(username);
	            return jwtService.generateToken(authUser.getEmployeeID(), username, authUser.getRoles());
	        } else {
	            throw new UserNotFoundException("Invalid Login Credentials...");
	        }
	    }  catch (Exception e) {
	        	throw new UserNotFoundException("Invalid Login Credentials...");
	    }
	}

	
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return userrepository.findAll();
	}
	
	public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = userrepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("No User Found With Username: " + username);
        }

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Incorrect old password");
        }

        user.setPassword(encoder.encode(newPassword));
        userrepository.save(user);
    }
	
	
	@Transactional // Add transactional annotation to ensure atomicity
    public void deleteByUsername(String username) {
        User user = userrepository.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }

        // Delete all milestones associated with this user
        milestoneRepository.deleteByUser(user);

        // Now delete the user
        userrepository.delete(user);
    }
	
	
	
	public User getUserByUsername(String username) {
        return userrepository.findByUsername(username);
    }
	
	public User getUserByEmployeeId(Long employeeId) {
        return userrepository.findByEmployeeID(employeeId).orElse(null);
    }
}

