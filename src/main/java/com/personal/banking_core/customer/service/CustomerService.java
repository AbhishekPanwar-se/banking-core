package com.personal.banking_core.customer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.banking_core.customer.dto.CreateCustomerRequest;
import com.personal.banking_core.customer.dto.UpdateCustomerRequest;
import com.personal.banking_core.customer.entity.Customer;
import com.personal.banking_core.customer.entity.CustomerStatus;
import com.personal.banking_core.customer.exception.CustomerAlreadyExistsException;
import com.personal.banking_core.customer.exception.CustomerNotFoundException;
import com.personal.banking_core.customer.repository.CustomerRepository;

@Service
public class CustomerService {
	
	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Customer createCustomer(CreateCustomerRequest createDto) {
		if(customerRepository.existsByEmail(createDto.getEmail())) {
			throw new CustomerAlreadyExistsException("Customer with email already exists: " + createDto.getEmail());
		}
		if(customerRepository.existsByPhone(createDto.getPhone())) {
			throw new CustomerAlreadyExistsException("Customer with phone already exists: " + createDto.getPhone());
		}
		Customer customer = new Customer(createDto.getFirstName(), createDto.getLastName(), createDto.getEmail(), createDto.getPhone(), createDto.getDateOfBirth(), CustomerStatus.ACTIVE);
		Customer savedCustomer = customerRepository.save(customer);
		return savedCustomer;
	}

	public Customer getCustomerById(long id) {
		Optional<Customer> optional = customerRepository.findById(id);
		if(optional.isEmpty()) {
			throw new CustomerNotFoundException("No customer present with provided ID: " + id);
		}
		return optional.get();
	}
	
	public List<Customer> getAllCustomer(){
		List<Customer> list = customerRepository.findAll();
		return list;
	}
	
	public Customer updateCustomer(long id, UpdateCustomerRequest updateDto) {
		Optional<Customer> optional = customerRepository.findById(id);
		if(optional.isEmpty()) {
			throw new CustomerNotFoundException("No customer present with provided ID: " + id);
		}
		Customer customer =  optional.get();
		customer.setFirstName(updateDto.getFirstName());
		customer.setLastName(updateDto.getLastName());
		customer.setPhone(updateDto.getPhone());
		customer.setEmail(updateDto.getEmail());
		
		Customer updatedCustomer = customerRepository.save(customer);
		return updatedCustomer;
	}
	
	public Customer deleteCustomer(long id) {
		Optional<Customer> optional = customerRepository.findById(id);
		if(optional.isEmpty()) {
			throw new CustomerNotFoundException("No customer present with provided ID: " + id);
		}
		Customer customer =  optional.get();
		customer.setStatus(CustomerStatus.CLOSED);
		
		return customerRepository.save(customer);
	}

}
