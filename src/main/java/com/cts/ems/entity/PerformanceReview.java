package com.cts.ems.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewID;
	
	@NotNull(message = "Employee ID is required")
	private Long employeeID;
	
	@NotNull(message = "Manager ID is required")
	private Long managerID;
	
	
	private LocalDate date;
	
	@NotBlank(message = "Performance score is required")
	private String performanceScore;
	
	@NotBlank(message = "Feedback is required")
	private String feedback;
	

}
