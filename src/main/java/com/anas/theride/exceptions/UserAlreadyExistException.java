package com.anas.theride.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.FORBIDDEN )
public class UserAlreadyExistException  extends Exception{
	private static final long serialVersionUID = -4961899491780449361L;

	public UserAlreadyExistException( String field, String value ){
		super( "A user already exist with the " + field + " " + value );
	}
}
