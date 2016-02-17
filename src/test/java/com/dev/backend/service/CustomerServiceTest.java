package com.dev.backend.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dev.backend.domain.Customer;

@ContextConfiguration(locations = "classpath:ws-servlet-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerServiceTest {
	private final String CID = "C001_test";
	
	@Autowired
	CustomerService customerService;

	private Customer createDummyCustomer(String id){
		Customer c = new Customer();
        c.setId(id);
        c.setName("Test name 1");
        c.setPhone1("012345698");
        c.setCredit(1000);
        c.setLimit(10000);
        return c;
	}
	
	@Before
	public void createDummyData(){
		customerService.insert(createDummyCustomer(CID));
        Assert.assertNotNull(customerService.findCustomerById(CID));
	}
	
	@Test
	public void testAddAndRemove(){
		Customer c = createDummyCustomer("I002");
		customerService.insert(c);
        
        Customer c1 = customerService.findCustomerById("I002");
        Assert.assertNotNull(c1);
        
        customerService.delete(c1);
        Assert.assertNull(customerService.findCustomerById("I002"));
    }
	
	@Test
	public void testAdd_Error_EmptyName(){
		Customer c = createDummyCustomer("I002");
		c.setName(null);
		customerService.insert(c);
        Assert.assertNull(customerService.findCustomerById("I002"));
    }
	
	@Test
	public void testAdd_Error_EmptyID(){
		Customer c = createDummyCustomer(null);
		customerService.insert(c);
        Assert.assertNull(customerService.findCustomerById("I002"));
    }
	
	@Test
	public void testFind(){
		Assert.assertEquals(1000, customerService.findCustomerById(CID).getCredit(), 1.0f);
	}
	
	@Test
	public void testUpdate(){
		Customer c = customerService.findCustomerById(CID);
		c.setCredit(1005);
		customerService.update(c);
		
		Assert.assertEquals(1005, customerService.findCustomerById(CID).getCredit(), 1.0f);
	}
	
	@Test
	public void testUpdate_Error_EmptyName(){
		Customer c = customerService.findCustomerById(CID);
		c.setName(null);
		c.setCredit(1005);
		customerService.update(c);
		
		Assert.assertEquals(1000, customerService.findCustomerById(CID).getCredit(), 1.0f);
	}
	
	@Test
	public void testUpdate_Error_InvalidID(){
		Customer c = createDummyCustomer("I000000");
		customerService.update(c);
		Assert.assertNull(customerService.findCustomerById("I000000"));
	}
	
	@Test
    public void testDelete_Error_InvalidID(){
		int size0 = customerService.selectAll().size();
		Customer c = createDummyCustomer("I000000");
		customerService.delete(c);
		Assert.assertNull(customerService.findCustomerById("I000000"));
		Assert.assertEquals(size0, customerService.selectAll().size());
	}
	
	@Test
	public void testSelectAll(){
		int size0 = customerService.selectAll().size();
		customerService.insert(createDummyCustomer("I002"));
		customerService.insert(createDummyCustomer("I003"));
		Assert.assertEquals(size0+2, customerService.selectAll().size());
		customerService.delete(customerService.findCustomerById("I002"));
		customerService.delete(customerService.findCustomerById("I003"));
		Assert.assertEquals(size0, customerService.selectAll().size());
	}
	
	@Test
	public void testIsCustomerExist(){
		customerService.insert(createDummyCustomer("I002"));
		Assert.assertTrue(customerService.isCustomerExist(createDummyCustomer("I002")));
		Assert.assertFalse(customerService.isCustomerExist(createDummyCustomer("I003")));
		customerService.delete(customerService.findCustomerById("I002"));
		Assert.assertFalse(customerService.isCustomerExist(createDummyCustomer("I002")));
	}

	@After
	public void tearDown(){
		customerService.delete(customerService.findCustomerById(CID));
		Assert.assertNull(customerService.findCustomerById(CID));
	}
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
