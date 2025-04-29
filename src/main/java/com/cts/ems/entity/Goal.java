package com.cts.ems.entity;

import java.util.ArrayList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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
public class Goal {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalID;
    
	@NotNull(message = "Employee ID is required")
    private Long employeeID;
	
	@ManyToOne // Add this relationship
    @JoinColumn(name = "employeeID", referencedColumnName = "employeeID", insertable = false, updatable = false)
    private User employee;
    
	@NotBlank(message = "Goal description is required")
    private String goalDesc;
    
	@NotBlank(message = "Target date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Target date must be in the format YYYY-MM-DD")
    private String targetDate;
    
	@NotBlank(message = "Progress status is required")
    private String progressStatus;
    
    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference //for parent entity
    private List<Milestone> milestones = new ArrayList<>();
    
    private Long assignedByManager;
}
