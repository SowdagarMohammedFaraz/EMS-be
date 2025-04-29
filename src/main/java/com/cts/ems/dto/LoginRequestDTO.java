package com.cts.ems.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// DTO helps in transferring objects of class type to another as an input
public class LoginRequestDTO {

	@NotBlank (message = "Username is required")
	private String username;
	
	@NotBlank(message = "Password is required")
	private String password;
}
