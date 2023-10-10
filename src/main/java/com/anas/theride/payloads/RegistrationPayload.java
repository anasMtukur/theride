package com.anas.theride.payloads;

import java.util.List;

import com.anas.theride.role.RoleName;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationPayload {
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String password;
	private List<RoleName> roles;
}