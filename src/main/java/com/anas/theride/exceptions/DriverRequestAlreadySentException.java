package com.anas.theride.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.anas.theride.driver.DriverApprovalStatus;

@ResponseStatus( HttpStatus.FORBIDDEN )
public class DriverRequestAlreadySentException  extends Exception{
	private static final long serialVersionUID = -4961899491780449361L;

	public DriverRequestAlreadySentException( DriverApprovalStatus status ){
		super( "You've already sent a driver request that is currently " + status );
	}
}
