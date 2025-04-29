package com.cts.ems.service;
 
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ems.controller.AuthController;
import com.cts.ems.entity.PerformanceReview;
import com.cts.ems.exception.ReviewNotSavedException;
import com.cts.ems.repository.ReviewRepository;
 
@Service
public class ReviewService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class); 
 
    @Autowired
    private ReviewRepository reviewrepository;
 
    public void addReview(PerformanceReview review) {
    	
    	logger.info("Manager is adding review for the employee ID:{}",review.getEmployeeID());
        try {
        	review.setDate(LocalDate.now());
            reviewrepository.save(review);
            logger.info("Performance Review added successfully for the employeeID:{}",review.getEmployeeID());
            
        } catch (Exception e) {
            throw new ReviewNotSavedException("Failed to save the performance review: " + e.getMessage());
        }
    }
}