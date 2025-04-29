package com.cts.ems.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor //it automatically defines constructor with all the arguements
@NoArgsConstructor // it automatically defines constructor with no parameters
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedbackID;
	
	private Long fromemployeeID;
	
	private Long employeeID;
	
	@NotBlank(message = "Feedback type is required")
	private String feedbacktype;
	
	@NotBlank(message = "Comments are required")
	private String comments;
}
