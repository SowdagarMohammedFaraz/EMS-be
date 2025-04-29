package com.cts.ems.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MilestoneDTO {

	@NotNull(message = "Milestone ID is required")
	private Long milestoneID;
	
	@NotNull(message = "Employee ID is required")
	private Long employeeID;
	
	@NotNull(message = "Goal ID is required")
	private Long goalID;
	
	@NotBlank(message = "Progress status is required")
	private String progressStatus;
}
