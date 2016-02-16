package com.dev.backend.dao;

import java.util.ArrayList;
import java.util.List;

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

import com.dev.backend.domain.Customer;
import com.dev.backend.domain.OrderLine;
import com.dev.backend.domain.Product;
import com.dev.backend.domain.SalesOrder;

@ContextConfiguration(locations = "classpath:data-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SalesOrderDAOTest {
	
	@Autowired
    private CustomerDAO customerDAO;
	
	@Autowired
	SalesOrderDAO salesOrderDAO;
	
	@Autowired
	OrderLineDAO orderLineDAO;
	
	@Autowired
    private ProductDAO productDAO;
	
	private Customer createDummyCustomer(){
		Customer c = new Customer();
        c.setId("001");
        c.setName("Test name 1");
        c.setPhone1("012345698");
        c.setCredit(1000);
        c.setLimit(10000);
        return c;
	}
	
	private Product createDummyProduct(){
		Product p = new Product();
		p.setId("001");
		p.setDescription("Description");
		p.setPrice(10);
		p.setQuantity(10);
		return p;
	}
	
	private OrderLine createDummyOrder(String orderId){
		OrderLine o = new OrderLine();
		o.setOrderId(orderId);
		o.setProductId("001");
		o.setQuantity(10);
		return o;
	}
	
	private SalesOrder createDummySalesOrder(String orderId){
		SalesOrder so = new SalesOrder();
		so.setOrderNumber(orderId);
		so.setCustomerId("001");
		so.setTotalPrice(100);
		
		List<OrderLine> olist = new ArrayList<OrderLine>();
		olist.add(createDummyOrder(orderId));
		so.setOrderList(olist);
		
		return so;
	}
	
	@Before
	@Transactional
    @Rollback(true)
	public void createDummyData(){
        customerDAO.insert(createDummyCustomer());
        productDAO.insert(createDummyProduct());
        salesOrderDAO.insert(createDummySalesOrder("001"));
        orderLineDAO.insert(createDummyOrder("001"));
        
        Assert.assertNotNull(salesOrderDAO.findSalesOrderById("001"));
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testFind(){
		Assert.assertEquals(100.0f, salesOrderDAO.findSalesOrderById("001").getTotalPrice(), 1.0f);
	}
	
	@After
    @Transactional
    @Rollback(true)
    public void tearDown(){
		orderLineDAO.delete(orderLineDAO.findOrderLineById("001", "001"));
		salesOrderDAO.delete(salesOrderDAO.findSalesOrderById("001"));
		customerDAO.delete(customerDAO.findCustomerById("001"));
		productDAO.delete(productDAO.findProductById("001"));
	}
}
