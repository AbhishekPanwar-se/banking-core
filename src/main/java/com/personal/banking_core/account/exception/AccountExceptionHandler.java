package com.personal.banking_core.account.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.personal.banking_core.common.exception.ErrorResponse;
import com.personal.banking_core.transaction.exception.SortingNotAllowedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class AccountExceptionHandler {
	
	@ExceptionHandler(OperationNotAllowedException.class)
	public ResponseEntity<ErrorResponse> handleOperationNotAllowed(OperationNotAllowedException ex, HttpServletRequest request){
		ErrorResponse errResponse = new ErrorResponse(LocalDateTime.now(),HttpStatus.CONFLICT.value(), "OperationNotAllowed", ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorResponse>(errResponse,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex, HttpServletRequest request){
		ErrorResponse errResponse = new ErrorResponse(LocalDateTime.now(),HttpStatus.NOT_FOUND.value(), "AccountNotFound", ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorResponse>(errResponse,HttpStatus.NOT_FOUND);
	}
	
}
