package com.dev.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.backend.domain.Customer;
import com.dev.backend.service.CustomerService;

@RestController
public class CustomerController {
	
	CustomerService customerService;
    
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> listAllCustomers() {
    	System.out.println("Fetching All Customer");
        List<Customer> customers = customerService.selectAll();
        
        if(customers.isEmpty()){
        	System.out.println("Zero record found !");
            return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
        }
        System.out.println("Record found : " + customers.size());
        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") String id) {
        System.out.println("Fetching Customer with id " + id);
        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            System.out.println("Customer with id " + id + " not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer) {
        if (customerService.isCustomerExist(customer)) {
            System.out.println("A Customer with name " + customer.getName() + " already exist");
            System.out.println("Try updating ..." + customer.toString());
            
            Customer c = customerService.findCustomerById(customer.getId());
            c.setId(customer.getId());
			c.setName(customer.getName());
			c.setAddress(customer.getAddress());
			c.setPhone1(customer.getPhone1());
			c.setPhone2(customer.getPhone2());
			c.setLimit(customer.getLimit());
			c.setCredit(customer.getCredit());
            
            customerService.update(c);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }else{
        	System.out.println("Try creating ..." + customer.toString());
        	customerService.insert(customer);
        	return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") String id, @RequestBody Customer customer) {
        System.out.println("Updating Customer " + id);
         
        Customer currentCustomer = customerService.findCustomerById(id);
         
        if (currentCustomer==null) {
            System.out.println("Customer with id " + id + " not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
 
        currentCustomer.setName(customer.getName());
        currentCustomer.setAddress(customer.getAddress());
        currentCustomer.setPhone1(customer.getPhone1());
        currentCustomer.setPhone2(customer.getPhone2());
        currentCustomer.setCredit(customer.getCredit());
        currentCustomer.setLimit(customer.getLimit());
         
        customerService.update(currentCustomer);
        return new ResponseEntity<Customer>(currentCustomer, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") String id) {
        System.out.println("Fetching & Deleting Customer with id " + id);
 
        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            System.out.println("Unable to delete. Customer with id " + id + " not found");
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
 
        customerService.delete(customer);
        return new ResponseEntity<Customer>(HttpStatus.NO_CONTENT);
    }


	public CustomerService getCustomerService() {
		return customerService;
	}


	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
