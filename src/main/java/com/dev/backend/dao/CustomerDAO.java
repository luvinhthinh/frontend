package com.dev.backend.dao;

import java.util.List;
import com.dev.backend.domain.Customer;

public interface CustomerDAO {
	
	public boolean insert(Customer customer);
	public boolean delete(Customer customer);
	public boolean update(Customer customer);
	public List<Customer> selectAll();
	public Customer findCustomerById(String id);
}
