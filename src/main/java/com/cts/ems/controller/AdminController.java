package com.cts.ems.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.ems.entity.User;
import com.cts.ems.exception.UserNotFoundException;
import com.cts.ems.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private UserService userService;

	//Admin is getting all users 
	
	@GetMapping("/users")
	public List<User> getAllUsers() {
		
		logger.info("Admin is trying to get all users");
		
	    try {
	        return userService.getAllUser();
	    } catch (IllegalArgumentException ex) {
	        throw new RuntimeException("Error fetching users: " + ex.getMessage());
	    }
	}
	
	//Admin can delete the user by username

	@DeleteMapping("/delete/{username}")
	public String deleteByUsername(@PathVariable String username) {
		
		logger.info("Admin deleting a particular user");
		
	    try {
	        userService.deleteByUsername(username);
	        return "User deleted successfully";
	    } 
	    catch (UserNotFoundException ex) {
	        throw new RuntimeException("Username not found" + ex.getMessage());
    }
	}
	
}
