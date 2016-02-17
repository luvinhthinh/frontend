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
	private final String PID = "P001";
	
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
		productDAO.insert(createDummyProduct(PID));
        Assert.assertNotNull(productDAO.findProductById(PID));
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testAddAndRemove(){
		Product p = createDummyProduct("P002");
        productDAO.insert(p);
        
        Product p1 = productDAO.findProductById("P002");
        Assert.assertNotNull(p1);
        
        productDAO.delete(p1);
        Assert.assertNull(productDAO.findProductById("P002"));
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testAdd_Error_EmptyID(){
		int size0 = productDAO.selectAll().size();
		Product p = createDummyProduct(null);
        productDAO.insert(p);
        Assert.assertEquals(size0, productDAO.selectAll().size());
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testAdd_Error_EmptyDescription(){
		Product p = createDummyProduct("P002");
		p.setDescription(null);
        productDAO.insert(p);
        Assert.assertNull(productDAO.findProductById("P002"));
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testFind(){
		Assert.assertEquals(10, productDAO.findProductById(PID).getQuantity());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate(){
		Product c = productDAO.findProductById(PID);
		c.setQuantity(50);
		productDAO.update(c);
		
		Assert.assertEquals(50, productDAO.findProductById(PID).getQuantity());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_EmptyID(){
		Product p = productDAO.findProductById(PID);
		p.setId(null);
		productDAO.update(p);
		
		Assert.assertEquals(10, productDAO.findProductById(PID).getQuantity());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_EmptyDescription(){
		Product p = productDAO.findProductById(PID);
		p.setDescription(null);
		productDAO.update(p);
		
		Assert.assertEquals(10, productDAO.findProductById(PID).getQuantity());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_InvalidID(){
		Product p = productDAO.findProductById(PID);
		p.setId("P1234");
		productDAO.update(p);
		
		Assert.assertEquals(10, productDAO.findProductById(PID).getQuantity());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testDelete_Error_InvalidID(){
		int size0 = productDAO.selectAll().size();
		Product p = createDummyProduct("P00001");
		productDAO.delete(p);
		Assert.assertNull(productDAO.findProductById(p.getId()));
		Assert.assertEquals(size0, productDAO.selectAll().size());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testSelectAll(){
		int size0 = productDAO.selectAll().size();
		productDAO.insert(createDummyProduct("P002"));
		productDAO.insert(createDummyProduct("P003"));
		Assert.assertEquals(size0+2, productDAO.selectAll().size());
		productDAO.delete(productDAO.findProductById("P002"));
		productDAO.delete(productDAO.findProductById("P003"));
		Assert.assertEquals(size0, productDAO.selectAll().size());
	}
	
	@After
    @Transactional
    @Rollback(true)
    public void tearDown(){
		productDAO.delete(productDAO.findProductById(PID));
		Assert.assertEquals(0, productDAO.selectAll().size());
	}
}
