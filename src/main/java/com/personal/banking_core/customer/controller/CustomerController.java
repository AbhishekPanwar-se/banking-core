package com.personal.banking_core.customer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.personal.banking_core.customer.dto.CreateCustomerRequest;
import com.personal.banking_core.customer.dto.UpdateCustomerRequest;
import com.personal.banking_core.customer.entity.Customer;
import com.personal.banking_core.customer.service.CustomerService;

import jakarta.validation.Valid;

@RestController
public class CustomerController {
	
	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	
	@PostMapping("/api/v1/customers")
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CreateCustomerRequest customerDto) {
		Customer customer = customerService.createCustomer(customerDto);
		return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
	}
	
	@GetMapping("/api/v1/customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable long id) {
		Customer customer = customerService.getCustomerById(id);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	@GetMapping("/api/v1/customers")
	public ResponseEntity<List<Customer>> getAllCustomer(){
		List<Customer> customerList = customerService.getAllCustomer();
		return new ResponseEntity<List<Customer>>(customerList, HttpStatus.OK);
	}
	
	@PutMapping("/api/v1/customers/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable long id, @Valid @RequestBody UpdateCustomerRequest updateDto) {
		Customer customer = customerService.updateCustomer(id,updateDto);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	@DeleteMapping("/api/v1/customers/{id}")
	public ResponseEntity<Customer> deleteCustomer(@PathVariable long id) {
		Customer customer = customerService.deleteCustomer(id);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
}
