package com.dev.backend.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dev.backend.domain.Product;

@ContextConfiguration(locations = "classpath:data-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductDAOTest {
	
	@Autowired
    private ProductDAO productDAO;
	
	private Product createDummyProduct(String id){
		Product p = new Product();
		p.setId(id);
		p.setDescription("Description");
		p.setPrice(10);
		p.setQuantity(10);
		return p;
	}
	
	@Before
	@Transactional
    @Rollback(true)
	public void createDummyData(){
		productDAO.insert(createDummyProduct("P001"));
        Assert.assertNotNull(productDAO.findProductById("P001"));
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testAddAndRemove(){
		Product c = createDummyProduct("P002");
        productDAO.insert(c);
        
        Product c1 = productDAO.findProductById("P002");
        Assert.assertNotNull(c1);
        
        productDAO.delete(c1);
        Assert.assertNull(productDAO.findProductById("P002"));
        
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testFind(){
		Assert.assertEquals(10, productDAO.findProductById("P001").getQuantity());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate(){
		Product c = productDAO.findProductById("P001");
		c.setQuantity(50);
		productDAO.update(c);
		
		Assert.assertEquals(50, productDAO.findProductById("P001").getQuantity());
	}
	
	@After
    @Transactional
    @Rollback(true)
    public void tearDown(){
		productDAO.delete(productDAO.findProductById("P001"));
	}
}
