package com.personal.banking_core.account.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.personal.banking_core.common.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AccountExceptionHandler {
	
	@ExceptionHandler(AccountOpeningNotAllowedException.class)
	public ResponseEntity<ErrorResponse> handleAccountOpeningNotAllowed(AccountOpeningNotAllowedException ex, HttpServletRequest request){
		ErrorResponse errResponse = new ErrorResponse(LocalDateTime.now(),409, "AccountOpeningNotAllowed", ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorResponse>(errResponse,HttpStatus.CONFLICT);
	}
}
