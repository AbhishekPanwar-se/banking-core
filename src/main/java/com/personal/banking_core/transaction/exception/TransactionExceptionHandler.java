package com.personal.banking_core.transaction.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.personal.banking_core.common.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class TransactionExceptionHandler {
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
	
	@ExceptionHandler(SortingNotAllowedException.class)
	public ResponseEntity<ErrorResponse> handleSortingNotAllowed(SortingNotAllowedException ex, HttpServletRequest request) {
			ErrorResponse errResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "SortingNotAllowed",
	            ex.getMessage(),
	            request.getRequestURI());

	    return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleHandlerMethodValidation(ConstraintViolationException ex, HttpServletRequest request) {
			ErrorResponse errResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "ValidationFailed",
	            ex.getMessage(),
	            request.getRequestURI());

	    return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(TransactionNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleTransactionNotFound(TransactionNotFoundException ex, HttpServletRequest request) {
			ErrorResponse errResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "TransactionNotFound",
	            ex.getMessage(),
	            request.getRequestURI());

	    return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
			ErrorResponse errResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "MethodArgumentTypeMismatch",
	            "invalid operation",
	            request.getRequestURI());

	    return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
	}
}
