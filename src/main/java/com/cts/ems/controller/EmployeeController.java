package com.cts.ems.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cts.ems.dto.MilestoneDTO;
import com.cts.ems.dto.PasswordDTO;
import com.cts.ems.entity.Feedback;
import com.cts.ems.entity.Goal;
import com.cts.ems.entity.User;
import com.cts.ems.entity.Milestone;
import com.cts.ems.service.FeedbackService;
import com.cts.ems.service.GoalService;
import com.cts.ems.service.MilestoneService;
import com.cts.ems.service.UserService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private GoalService goalService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private MilestoneService milestoneService;
	
	//Employee can update password by providing username,oldpassword,newpassword
	
	@PostMapping("/updatePassword")
	public String updatePassword(@RequestBody PasswordDTO password) {
		userService.updatePassword(password.getUserName(),password.getOldPassword(),password.getNewPassword());
		return "Password updated successfully";
	}
	
	//employee will get all goals by giving their employeeID
	
	@GetMapping("/getAllGoal/{employeeID}")
	public List<Goal> getAllGoal(@PathVariable("employeeID") Long employeeID) {
		return goalService.getAllGoal(employeeID);
	}
	
	//employee will get all milestones by giving the particular goalID
	
	@GetMapping("/getAllMilestones/{goalID}")
	public List<Milestone> getAllMilestones(@PathVariable("goalID") Long goalID){
		return milestoneService.getAllMilestones(goalID);
	}
	
	//employee can give feedback to their peers
	
	@PostMapping("/giveFeedback")
    public ResponseEntity<String> giveFeedback(@RequestBody Feedback feedback) {
        logger.info("Submitting feedback for: {}", feedback.getEmployeeID());
        feedbackService.giveFeedback(feedback);
        logger.info("Feedback submitted successfully for: {}", feedback.getEmployeeID());
        return new ResponseEntity<>("{\"message\": \"Feedback submitted successfully\"}", HttpStatus.OK);
    }
	
	//employee can update milestone once they completed the milestone of the particular goalID
	
	@PostMapping("/updateMilestone")
	public ResponseEntity<String> updateMilestone(@RequestBody MilestoneDTO milestoneDTO) {
	    milestoneService.updateMilestone(milestoneDTO);
	    return new ResponseEntity<>("{\"message\": \"Milestone updated successfully\"}", HttpStatus.OK);
	}
	
	@GetMapping("/profile/{employeeId}")
    public ResponseEntity<User> getEmployeeProfile(@PathVariable Long employeeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User loggedInEmployee = userService.getUserByUsername(username);
            if (loggedInEmployee != null && loggedInEmployee.getEmployeeID().equals(employeeId)) {
                User employee = userService.getUserByEmployeeId(employeeId);
                if (employee != null) {
                    employee.setPassword(null); // Exclude password
                    return new ResponseEntity<>(employee, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
