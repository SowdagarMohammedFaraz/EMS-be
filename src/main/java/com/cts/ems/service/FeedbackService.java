package com.cts.ems.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ems.controller.AuthController;
import com.cts.ems.entity.Feedback;
import com.cts.ems.repository.FeedbackRepository;

@Service
public class FeedbackService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private FeedbackRepository feedbackRepository;

    public void giveFeedback(Feedback feedback) {
    	
    	logger.info("Employee with EmpID:{} is giving feedback to the employee with EmpID:{}",feedback.getFromemployeeID(),feedback.getEmployeeID());
    	
    	
        if (feedback.getFromemployeeID() == null || feedback.getEmployeeID() == null) {
            throw new IllegalArgumentException("Employee IDs cannot be null");
        }

        if (feedback.getFeedbacktype() == null || feedback.getFeedbacktype().isEmpty()) {
            throw new IllegalArgumentException("Feedback type is required");
        }

        if (feedback.getComments() == null || feedback.getComments().isEmpty()) {
            throw new IllegalArgumentException("Comments are required");
        }

        feedbackRepository.save(feedback);
       logger.info("Feedback submitted successfully for EmployeeID:{} ",feedback.getEmployeeID());
    }
}
