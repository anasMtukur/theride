package com.anas.theride.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.anas.theride.exceptions.UserAlreadyExistException;
import com.anas.theride.payloads.AccountCredentials;
import com.anas.theride.payloads.AuthenticationResponse;
import com.anas.theride.payloads.RegistrationPayload;
import com.anas.theride.role.RoleName;
import com.anas.theride.security.services.JwtTokenService;
import com.anas.theride.security.services.JwtUserDetailsService;
import com.anas.theride.user.EndUser;
import com.anas.theride.user.EndUserRepository;
import com.anas.theride.user.EndUserService;

@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RequestMapping("/v1/auth")
public class JwtAuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenService jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private EndUserRepository endUserRepository;

	@Autowired
	private EndUserService endUserService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping( value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AccountCredentials authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok( new AuthenticationResponse(token, userDetails) );
	}

	@PostMapping(
			  value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity< EndUser > registration(@RequestBody RegistrationPayload payload) throws Exception {

		if( endUserRepository.existsByEmail( payload.getEmail() ) ){
			throw new UserAlreadyExistException("Email", payload.getEmail());
		}

		if( endUserRepository.existsByMobile( payload.getMobile() ) ){
			throw new UserAlreadyExistException("Mobile No.", payload.getMobile());
		}

		EndUser entity = new EndUser();
		String fullname = payload.getFirstName() + " " + payload.getLastName();
		String password = passwordEncoder.encode( payload.getPassword() );

		entity.setFullName(fullname);
		entity.setUsername(payload.getEmail());
		entity.setEmail( payload.getEmail() );
		entity.setMobile( payload.getMobile() );
		entity.setPassword( password );

		endUserRepository.saveAndFlush( entity );

		List<RoleName> roles = new ArrayList<>( Arrays.asList(
			RoleName.ROLE_USER
		));

		if( entity.getId() != null ) {
			endUserService.addEndUserRoles(entity, roles);
		}
		
		return ResponseEntity.ok( entity );
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
