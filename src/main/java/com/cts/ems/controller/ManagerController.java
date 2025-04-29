package com.cts.ems.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.ems.dto.UserRequest;
import com.cts.ems.entity.Goal;
import com.cts.ems.entity.User;
import com.cts.ems.entity.Report;
import com.cts.ems.entity.Milestone;
import com.cts.ems.entity.PerformanceReview;
import com.cts.ems.service.GoalService;
import com.cts.ems.service.MilestoneService;
import com.cts.ems.service.ReportService;
import com.cts.ems.service.ReviewService;
import com.cts.ems.service.UserService;
import com.cts.ems.exception.UserNotFoundException;
import com.cts.ems.exception.ReportNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse; // Use jakarta if on Spring Boot 3+
import jakarta.servlet.http.HttpServletResponse; 


@RestController
@RequestMapping("/manager")
//@PreAuthorize("hasAuthority('MANAGER')")
public class ManagerController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private GoalService goalservice;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ReportService reportService;
	
	
	//manager is adding employee
	
	@PostMapping("/addEmployee")
    public ResponseEntity<String> addEmployee(@RequestBody UserRequest request) {
        logger.info("Manager is adding a new employee:{}", request.getUsername());
        userService.register(request.getUsername(), request.getEmail(), request.getPassword(), request.getDept(), request.getMobileNo(), request.getRole());
        logger.info("Employee {} added successfully", request.getUsername());
        return new ResponseEntity<>("{\"message\": \"Employee added successfully\"}", HttpStatus.CREATED);
    }
	
	//manager is assigning goal to the employee 
	
	
	@PostMapping("/addGoal")
    public ResponseEntity<String> addGoal(@RequestBody Goal goal) {
        logger.info("Manager is assigning a new goal: {}", goal);
        goalservice.addGoal(goal);
        logger.info("Goal assigned successfully with ID: {}", goal.getGoalID()); // Assuming Goal has a getGoalId() method
        return new ResponseEntity<>("{\"message\": \"Goal assigned successfully\"}", HttpStatus.CREATED);
    }
	
	
	//manager will give a performance review to the employee
	
	
	@PostMapping("/giveReview")
    public ResponseEntity<String> giveReview(@RequestBody PerformanceReview review) {
        logger.info("Submitting a performance review for employee: {}", review.getEmployeeID());
        reviewService.addReview(review);
        logger.info("Review submitted successfully for employee: {}", review.getEmployeeID());
        return new ResponseEntity<>("{\"message\": \"Review submitted successfully\"}", HttpStatus.OK);
    }
	
	//manager will the report(performance) by the employeeeID
	
	
	@GetMapping("/getReportByID/{employeeID}")
	public ResponseEntity<Report> getReportByID(@PathVariable("employeeID") Long employeeID) {
	    logger.info("Generating report for employee ID: {}", employeeID);
	    try {
	        Report report = reportService.getReportByID(employeeID);
	        logger.info("Report generated successfully for employee ID: {}", report.getEmployeeID()); // Assuming Report has getEmployeeID()
	        return new ResponseEntity<>(report, HttpStatus.OK);
	    } catch (UserNotFoundException e) {
	        logger.error("Error generating report: Employee not found - {}", e.getMessage());
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } catch (ReportNotFoundException e) {
	        logger.error("Error generating report: Report data not found - {}", e.getMessage());
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        logger.error("Error generating report for employee ID {}: {}", employeeID, e.getMessage());
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	@GetMapping("/profile")
    public ResponseEntity<User> getManagerProfile(HttpServletResponse response) {
        // Set cache control headers
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User manager = userService.getUserByUsername(username);
            if (manager != null) {
                manager.setPassword(null); // Exclude password
                return new ResponseEntity<>(manager, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
	
	 @GetMapping("/assigned-goals")
	    public ResponseEntity<List<Goal>> getAssignedGoalsByManager() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication != null && authentication.isAuthenticated()) {
	            String managerUsername = authentication.getName();
	            User manager = userService.getUserByUsername(managerUsername);
	            if (manager != null) {
	                List<Goal> assignedGoals = goalservice.getGoalsByAssigningManager(manager.getEmployeeID());
	                return new ResponseEntity<>(assignedGoals, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	        } else {
	            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	        }
	    }
}
