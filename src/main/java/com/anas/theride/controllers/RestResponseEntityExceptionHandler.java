package com.anas.theride.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.anas.theride.exceptions.DriverRequestAlreadySentException;
import com.anas.theride.exceptions.InvalidDriverStatusActivityException;
import com.anas.theride.exceptions.InvalidValueException;
import com.anas.theride.exceptions.UserAlreadyExistException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict( RuntimeException ex ) {
        return new ResponseEntity<>( ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }

	@ExceptionHandler(value = UserAlreadyExistException.class )
    protected ResponseEntity<Object> handleUserAlreadyExist( UserAlreadyExistException ex ) {
        return new ResponseEntity<>( ex.getMessage(), HttpStatus.FORBIDDEN );
    }

	@ExceptionHandler(value = InvalidDriverStatusActivityException.class )
    protected ResponseEntity<Object> handleInvalidDriverStatus( InvalidDriverStatusActivityException ex ) {
        return new ResponseEntity<>( ex.getMessage(), HttpStatus.FORBIDDEN );
    }

	@ExceptionHandler(value = InvalidValueException.class )
    protected ResponseEntity<Object> handleInvalidValueException( InvalidValueException ex ) {
        return new ResponseEntity<>( ex.getMessage(), HttpStatus.FORBIDDEN );
    }

	@ExceptionHandler(value = DriverRequestAlreadySentException.class )
    protected ResponseEntity<Object> handleDriverRequestAlreadySent( DriverRequestAlreadySentException ex ) {
        return new ResponseEntity<>( ex.getMessage(), HttpStatus.FORBIDDEN );
    }
}