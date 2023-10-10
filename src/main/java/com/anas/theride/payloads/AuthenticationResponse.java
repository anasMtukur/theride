package com.anas.theride.payloads;

import java.io.Serializable;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponse implements Serializable {
	private static final long serialVersionUID = -8091879091924046844L;
	
	private String jwttoken;
	private UserDetails user;
}
