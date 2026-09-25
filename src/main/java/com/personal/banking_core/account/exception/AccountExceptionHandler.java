package com.personal.banking_core.account.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.personal.banking_core.common.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AccountExceptionHandler {
	
	@ExceptionHandler(OperationNotAllowedException.class)
	public ResponseEntity<ErrorResponse> handleAccountOpeningNotAllowed(OperationNotAllowedException ex, HttpServletRequest request){
		ErrorResponse errResponse = new ErrorResponse(LocalDateTime.now(),HttpStatus.CONFLICT.value(), "OperationNotAllowed", ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorResponse>(errResponse,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex, HttpServletRequest request){
		ErrorResponse errResponse = new ErrorResponse(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(), "AccountNotFound", ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorResponse>(errResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<ErrorResponse> handleOptimisticLocking(ObjectOptimisticLockingFailureException ex, HttpServletRequest request) {
			ErrorResponse errResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.CONFLICT.value(),
	            "OptimisticLockingFailure",
	            "Account was modified by another transaction. Please retry the operation.",
	            request.getRequestURI());

	    return new ResponseEntity<>(errResponse, HttpStatus.CONFLICT);
	}
}
