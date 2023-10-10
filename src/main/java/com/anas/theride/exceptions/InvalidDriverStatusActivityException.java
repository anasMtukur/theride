package com.anas.theride.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.anas.theride.driver.DriverApprovalStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus( HttpStatus.CONFLICT )
public class InvalidDriverStatusActivityException extends Exception{
	private static final long serialVersionUID = -4961899491780449361L;

	public InvalidDriverStatusActivityException( DriverApprovalStatus status ){
		super("Driver with status: " + String.valueOf( status ) + " is not allowed.");
	}
}
