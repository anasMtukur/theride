package com.anas.theride.user;

import java.util.List;

import javax.validation.constraints.Null;

import com.anas.theride.role.RoleName;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payload {
	@Null
	private String id;
	private String fullName;
	private String username;
	private String mobile;
	private String email;
	@Null
	private String password;
	private String blockCode;
	private boolean isBlocked;
	@Null
	private List<RoleName> roles;
}
