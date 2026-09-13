package com.personal.banking_core.customer.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex, HttpServletRequest request){
		ErrorResponse errResponse = new ErrorResponse(LocalDateTime.now(),404, "Customer Not Found", ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorResponse>(errResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CustomerAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleCustomerAlreadyExists(CustomerAlreadyExistsException ex, HttpServletRequest request){
		ErrorResponse errResponse = new ErrorResponse(LocalDateTime.now(),409, "Customer Already Exists", ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorResponse>(errResponse, HttpStatus.CONFLICT);
	}

}
