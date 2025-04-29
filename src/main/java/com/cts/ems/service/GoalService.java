package com.cts.ems.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cts.ems.controller.AuthController;
import com.cts.ems.entity.Goal;
import com.cts.ems.entity.Milestone;
import com.cts.ems.entity.User;
import com.cts.ems.exception.InvalidEmployeeRoleException;
import com.cts.ems.repository.GoalRepository;
import com.cts.ems.repository.UserRepository;

@Service
public class GoalService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private GoalRepository goalrepository;
	
	@Autowired
	private UserRepository userrepository; //
	
	// Adding a new goals
	
	public void addGoal(Goal goal){

		logger.info("Adding new goal for employee ID:{}",goal.getEmployeeID());

		User employee = userrepository.findById(goal.getEmployeeID()).orElseThrow(()->new RuntimeException("Employee not found"));

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String managerUsername = authentication.getName();
			User manager = userrepository.findByUsername(managerUsername);
			if (manager != null) {
				goal.setAssignedByManager(manager.getEmployeeID()); // Set the assignedByManager here
				logger.info("Goal will be assigned by manager ID: {}", manager.getEmployeeID());
			} else {
				logger.warn("Could not determine the assigning manager.");
			}
		}

		for(Milestone milestone:goal.getMilestones()) {
			milestone.setGoal(goal);
			milestone.setUser(employee); // Use the found employee
		}

		boolean isEmployee = employee.getRoles().stream().anyMatch(role -> role.name().equalsIgnoreCase("EMPLOYEE"));

		if(!isEmployee) {
			throw new InvalidEmployeeRoleException("Invalid goal assignment");
		}

		goalrepository.save(goal);

		logger.info("Goal added successfully with ID:{}",goal.getGoalID());
	}


	
	
	public List<Goal> getGoalsByAssigningManager(Long managerId) {
        return goalrepository.findByAssignedByManager(managerId);
    }
	//Get all goals by employee ID
	
	
	
	public List<Goal> getAllGoal(Long employeeID){
		
		logger.info("Fetching all goals for employee ID:{}",employeeID);
		
		return goalrepository.findByEmployeeID(employeeID);
	}
}
