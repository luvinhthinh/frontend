package com.dev.backend.dao;

import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dev.backend.domain.Customer;

@ContextConfiguration(locations = "classpath:data-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerDAOTest {
	
	@Autowired
    private CustomerDAO customerDAO;
	
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
	@Transactional
    @Rollback(true)
	public void createDummyData(){
        customerDAO.insert(createDummyCustomer("I001"));
        Assert.assertNotNull(customerDAO.findCustomerById("I001"));
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testAddAndRemove(){
		Customer c = createDummyCustomer("I002");
        customerDAO.insert(c);
        
        Customer c1 = customerDAO.findCustomerById("I002");
        Assert.assertNotNull(c1);
        
        customerDAO.delete(c1);
        Assert.assertNull(customerDAO.findCustomerById("I002"));
        
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testFind(){
		Assert.assertEquals(1000, customerDAO.findCustomerById("I001").getCredit(), 1.0f);
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate(){
		Customer c = customerDAO.findCustomerById("I001");
		c.setCredit(1005);
		customerDAO.update(c);
		
		Assert.assertEquals(1005, customerDAO.findCustomerById("I001").getCredit(), 1.0f);
	}
	
	@After
    @Transactional
    @Rollback(true)
    public void tearDown(){
		customerDAO.delete(customerDAO.findCustomerById("I001"));
	}
}
