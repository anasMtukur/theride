package com.anas.theride.exceptions;

public class EntityNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -4961899491780449361L;

	public EntityNotFoundException( String message ){
		super( message );
	}
}
