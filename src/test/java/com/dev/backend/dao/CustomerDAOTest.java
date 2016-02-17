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
	private final String CID = "I001";
	
	@Autowired
    private CustomerDAO customerDAO;
	
	private Customer createDummyCustomer(String id){
		return new Customer(id, "Test name 1", null, "012345698", null, 1000, 10000);
	}
	
	@Before
	@Transactional
    @Rollback(true)
	public void createDummyData(){
        customerDAO.insert(createDummyCustomer(CID));
        Assert.assertNotNull(customerDAO.findCustomerById(CID));
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
    public void testAdd_Error_EmptyName(){
		Customer c = createDummyCustomer("I002");
		c.setName(null);
		customerDAO.insert(c);
        
        Customer c1 = customerDAO.findCustomerById("I002");
        Assert.assertNull(c1);
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testAdd_Error_EmptyID(){
		int size0 = customerDAO.selectAll().size();
		Customer c = createDummyCustomer(null);
		customerDAO.insert(c);
		Assert.assertEquals(size0, customerDAO.selectAll().size());
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testFind(){
		Assert.assertEquals(1000, customerDAO.findCustomerById(CID).getCredit(), 1.0f);
		Assert.assertNull(customerDAO.findCustomerById("I200000"));
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate(){
		Customer c = customerDAO.findCustomerById(CID);
		c.setCredit(1005);
		customerDAO.update(c);
		
		Assert.assertEquals(1005, customerDAO.findCustomerById(CID).getCredit(), 1.0f);
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_EmptyID(){
		Customer c = customerDAO.findCustomerById(CID);
		c.setId(null);
		c.setCredit(1500);
		customerDAO.update(c);
		
		Assert.assertEquals(1000, customerDAO.findCustomerById(CID).getCredit(), 1.0f);
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_EmptyName(){
		Customer c = customerDAO.findCustomerById(CID);
		c.setName(null);
		c.setCredit(1500);
		customerDAO.update(c);
		
		Assert.assertEquals(1000, customerDAO.findCustomerById(CID).getCredit(), 1.0f);
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_InvalidID(){
		Customer c = createDummyCustomer("I000000");
		customerDAO.update(c);
		Assert.assertNull(customerDAO.findCustomerById("I000000"));
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testDelete_Error_InvalidID(){
		int size0 = customerDAO.selectAll().size();
		Customer c = createDummyCustomer("I000000");
		customerDAO.delete(c);
		Assert.assertNull(customerDAO.findCustomerById("I000000"));
		Assert.assertEquals(size0, customerDAO.selectAll().size());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testSelectAll(){
		int size0 = customerDAO.selectAll().size();
		customerDAO.insert(createDummyCustomer("I002"));
		customerDAO.insert(createDummyCustomer("I003"));
		Assert.assertEquals(size0+2, customerDAO.selectAll().size());
		customerDAO.delete(customerDAO.findCustomerById("I002"));
		customerDAO.delete(customerDAO.findCustomerById("I003"));
		Assert.assertEquals(size0, customerDAO.selectAll().size());
	}
	
	@After
    @Transactional
    @Rollback(true)
    public void tearDown(){
		customerDAO.delete(customerDAO.findCustomerById(CID));
		Assert.assertNull(customerDAO.findCustomerById(CID));
		Assert.assertEquals(0, customerDAO.selectAll().size());
	}
}