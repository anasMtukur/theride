package com.anas.theride.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
public class InvalidValueException extends Exception{
	private static final long serialVersionUID = -4961899491780449361L;

	public InvalidValueException( String field, String value ){
		super( "Invalid value provided for field " + field + "(" + value + ")" );
	}
}
