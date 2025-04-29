package com.cts.ems.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportID;
	
	@NotNull(message = "Employee ID is required")
	private Long employeeID;
	
	@NotNull(message = "Date is required")
	private LocalDate date;
	
	@NotBlank(message = "Performance summary is required")
	private String performancesummary;
	
	@NotBlank(message = "Feedback summary is required")
	private String feedbacksummary;

}
