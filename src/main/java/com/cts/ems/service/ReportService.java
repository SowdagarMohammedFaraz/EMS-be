package com.cts.ems.service;

import java.time.LocalDate;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.ems.controller.AuthController;
import com.cts.ems.entity.Feedback;
import com.cts.ems.entity.PerformanceReview;
import com.cts.ems.entity.Report;
import com.cts.ems.entity.User;
import com.cts.ems.exception.ReportNotFoundException;
import com.cts.ems.exception.UserNotFoundException;
import com.cts.ems.repository.FeedbackRepository;
import com.cts.ems.repository.ReportRepository;
import com.cts.ems.repository.ReviewRepository;
import com.cts.ems.repository.UserRepository;



@Service
public class ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	ReportRepository reportrepository;
	
	@Autowired
	ReviewRepository reviewrepository;
	
	@Autowired
	FeedbackRepository feedbackrepository;
	
	@Autowired
	UserRepository userrepository;
	
	public Report getReportByID(Long employeeID) {

	    Optional<User> empID = userrepository.findById(employeeID);

	    if (empID.isEmpty()) {
	        throw new UserNotFoundException("There is no employee with the employeeID " + employeeID);
	    }

	    logger.info("Generating report for employee ID:{}", employeeID);

	    PerformanceReview review = reviewrepository.findByEmployeeID(employeeID);
	    Feedback feed = feedbackrepository.findByEmployeeID(employeeID);

	    if (review == null || feed == null) {
	        throw new ReportNotFoundException("Report not found for employee ID: " + employeeID);
	    }

	    String comment = feed.getComments() + "," + review.getFeedback();

	    Report report = new Report();
	    report.setEmployeeID(review.getEmployeeID());
	    report.setPerformancesummary(review.getPerformanceScore());
	    report.setFeedbacksummary(comment);
	    report.setDate(LocalDate.now());

	    logger.info("Report Generated successfully for employeeID:{}", employeeID);

	    return reportrepository.save(report);
	}
	
	
}
