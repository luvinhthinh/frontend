package com.dev.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.dev.backend.domain.Customer;
import com.dev.backend.service.CustomerService;

@RestController
public class CustomerController {
	
	CustomerService customerService;
	
	//-------------------Retrieve All Customers--------------------------------------------------------
    
    @RequestMapping(value = "/customer/", method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> listAllCustomers() {
    	System.out.println("------------------------------------------------------------------------------------");
        List<Customer> customers = customerService.selectAll();
        if(customers.isEmpty()){
            return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }
 
 
    //-------------------Retrieve Single Customer--------------------------------------------------------
     
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
 
     
     
    //-------------------Create a Customer--------------------------------------------------------
     
    @RequestMapping(value = "/customer/", method = RequestMethod.POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Customer " + customer.getName());
 
        if (customerService.isCustomerExist(customer)) {
            System.out.println("A Customer with name " + customer.getName() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
 
        customerService.insert(customer);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/customer/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
 
     
    //------------------- Update a Customer --------------------------------------------------------
     
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
 
    //------------------- Delete a Customer --------------------------------------------------------
     
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
