package com.dev.backend.service;

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

import com.dev.backend.dao.*;
import com.dev.backend.domain.Customer;
import com.dev.backend.domain.OrderLine;
import com.dev.backend.domain.Product;
import com.dev.backend.domain.SalesOrder;
import com.dev.backend.service.impl.SalesOrderServiceImpl;

@ContextConfiguration(locations = "classpath:ws-servlet-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SalesOrderServiceImplTest {
	
	private final String CID = "001";
	private final String PID = "001";
	private final String OID = "001";
	
	
	@Autowired
    private CustomerDAO customerDAO;
	
	@Autowired
	SalesOrderDAO salesOrderDAO;
	
	@Autowired
	OrderLineDAO orderLineDAO;
	
	@Autowired
    private ProductDAO productDAO;
	
	private SalesOrderServiceImpl soSVCImpl = new SalesOrderServiceImpl();
	
	private Customer createDummyCustomer(){
		Customer c = new Customer();
        c.setId(CID);
        c.setName("Test name 1");
        c.setPhone1("012345698");
        c.setCredit(1000);
        c.setLimit(2000);
        return c;
	}
	
	private Product createDummyProduct(){
		Product p = new Product();
		p.setId(PID);
		p.setDescription("Description");
		p.setPrice(10);
		p.setQuantity(10);
		return p;
	}
	
	private OrderLine createDummyOrder(String orderId){
		OrderLine o = new OrderLine();
		o.setOrderId(orderId);
		o.setProductId(PID);
		o.setQuantity(10);
		return o;
	}
	
	private SalesOrder createDummySalesOrder(String orderId){
		SalesOrder so = new SalesOrder();
		so.setOrderNumber(orderId);
		so.setCustomerId(CID);
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
		soSVCImpl.setCustomerDAO(customerDAO);
		soSVCImpl.setProductDAO(productDAO);
		soSVCImpl.setSalesOrderDAO(salesOrderDAO);
		soSVCImpl.setOrderLineDAO(orderLineDAO);
		
        customerDAO.insert(createDummyCustomer());
        productDAO.insert(createDummyProduct());
        salesOrderDAO.insert(createDummySalesOrder(OID));
        orderLineDAO.insert(createDummyOrder(OID));
        
        Assert.assertNotNull(salesOrderDAO.findSalesOrderById(OID));
	}
	
	@Test
    public void testIsValidQuantity(){
		Assert.assertTrue(soSVCImpl.isValidQuantity(PID, 1));
		Assert.assertTrue(soSVCImpl.isValidQuantity(PID, 9));
		Assert.assertTrue(soSVCImpl.isValidQuantity(PID, 10));
		Assert.assertFalse(soSVCImpl.isValidQuantity(PID, 11));
		Assert.assertFalse(soSVCImpl.isValidQuantity(PID, 100));
	}
	
	@Test
    public void testIsValidCredit(){
		Assert.assertTrue(soSVCImpl.isValidCredit(CID, 100));
		Assert.assertTrue(soSVCImpl.isValidCredit(CID, 999));
		Assert.assertTrue(soSVCImpl.isValidCredit(CID, 1000));
		Assert.assertFalse(soSVCImpl.isValidCredit(CID, 1001));
		Assert.assertFalse(soSVCImpl.isValidCredit(CID, 2000));
	}
	
	@Test
	public void testValidate_valid(){
		SalesOrder so = createDummySalesOrder(OID);
		Assert.assertTrue(soSVCImpl.validate(so));
	}
	
	@Test
	public void testValidate_invalid_credit(){
		SalesOrder so = createDummySalesOrder(OID);
		so.setTotalPrice(1000.1f);
		Assert.assertFalse(soSVCImpl.validate(so));
	}
	
	@Test
	public void testValidate_invalid_quantity(){
		SalesOrder so = createDummySalesOrder(OID);
		so.getOrderList().get(0).setQuantity(11);
		Assert.assertFalse(soSVCImpl.validate(so));
	}

	@Test
	public void testConsolidateOrderLine1(){
		List<OrderLine> olist = new ArrayList<OrderLine>();
		olist.add(createDummyOrder(OID));
		olist.add(createDummyOrder(OID));
		
		List<OrderLine> newOList = soSVCImpl.consolidateOrderLine(olist);
		Assert.assertEquals(1, newOList.size());
		Assert.assertEquals(20, newOList.get(0).getQuantity());
	}
	
	@Test
	public void testConsolidateOrderLine2(){
		List<OrderLine> olist = new ArrayList<OrderLine>();
		olist.add(createDummyOrder(OID));
		olist.add(createDummyOrder(OID));
		
		OrderLine ol1 = createDummyOrder(OID);
		ol1.setProductId("002");
		ol1.setQuantity(50);
		olist.add(ol1);
		
		OrderLine ol2 = createDummyOrder(OID);
		ol2.setProductId("002");
		ol2.setQuantity(50);
		olist.add(ol2);
		
		List<OrderLine> newOList = soSVCImpl.consolidateOrderLine(olist);
		Assert.assertEquals(2, newOList.size());
		Assert.assertEquals(20, newOList.get(0).getQuantity());
		Assert.assertEquals(100, newOList.get(1).getQuantity());
	}
	
	@Test
	public void testInsertNewSalesOrder(){
		soSVCImpl.insert(createDummySalesOrder("O002"));
		Assert.assertEquals(1100, customerDAO.findCustomerById(CID).getCredit(), 1.0f);
		Assert.assertEquals(0, productDAO.findProductById(PID).getQuantity());
	}
	
	@Test
	public void testUpdateSalesOrder_Increase(){
		SalesOrder so = createDummySalesOrder(OID);
		so.setTotalPrice(200);
		so.getOrderList().get(0).setQuantity(15);
		
		soSVCImpl.update(so);
		Assert.assertEquals(1100, customerDAO.findCustomerById(CID).getCredit(), 1.0f);
		Assert.assertEquals(5, productDAO.findProductById(PID).getQuantity());
	}
	
	@Test
	public void testUpdateSalesOrder_Decrease(){
		SalesOrder so = createDummySalesOrder(OID);
		so.setTotalPrice(50);
		so.getOrderList().get(0).setQuantity(5);
		
		soSVCImpl.update(so);
		Assert.assertEquals(950, customerDAO.findCustomerById(CID).getCredit(), 1.0f);
		Assert.assertEquals(15, productDAO.findProductById(PID).getQuantity());
	}
	
	@After
    @Transactional
    @Rollback(true)
    public void tearDown(){
		orderLineDAO.delete(orderLineDAO.findOrderLineById(OID, PID));
		salesOrderDAO.delete(salesOrderDAO.findSalesOrderById(OID));
		customerDAO.delete(customerDAO.findCustomerById(CID));
		productDAO.delete(productDAO.findProductById(PID));
	}
}
