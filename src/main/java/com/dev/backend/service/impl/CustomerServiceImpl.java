package com.dev.backend.service.impl;

import java.util.List;

import com.dev.backend.dao.CustomerDAO;
import com.dev.backend.domain.Customer;
import com.dev.backend.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

	private CustomerDAO customerDAO;
	
	@Override
	public void insert(Customer customer) {
		this.customerDAO.insert(customer);
	}

	@Override
	public void delete(Customer customer) {
		this.customerDAO.delete(customer);
	}

	@Override
	public void update(Customer customer) {
		this.customerDAO.update(customer);
	}

	@Override
	public List<Customer> selectAll() {
		return this.customerDAO.selectAll();
	}

	@Override
	public Customer findCustomerById(String id) {
		return this.customerDAO.findCustomerById(id);
	}

	@Override
	public boolean isCustomerExist(Customer customer) {
		return findCustomerById(customer.getId()) != null;
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

}
