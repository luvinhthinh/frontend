package com.dev.backend.service;

import java.util.List;

import com.dev.backend.domain.Customer;

public interface CustomerService {
	public void insert(Customer customer);
	public void delete(Customer customer);
	public void update(Customer customer);
	public List<Customer> selectAll();
	public Customer findCustomerById(String id);
	public boolean isCustomerExist(Customer customer);
}
