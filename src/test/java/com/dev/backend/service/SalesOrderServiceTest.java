package com.dev.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dev.backend.domain.Customer;
import com.dev.backend.domain.OrderLine;
import com.dev.backend.domain.Product;
import com.dev.backend.domain.SalesOrder;

@ContextConfiguration(locations = "classpath:ws-servlet-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SalesOrderServiceTest {
	private final String CID = "C001_test";
	private final String PID = "P001_test";
	private final String OID = "O001_test";
	
	@Autowired
	SalesOrderService salesOrderService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	OrderLineService orderLineService;
	
	private Customer createDummyCustomer(){
		return new Customer(CID, "Test name 1", null, "012345698", null, 1000, 10000);
	}
	
	private Product createDummyProduct(){
		return new Product(PID, "Description", 10, 10);
	}
	
	private OrderLine createDummyOrder(String orderId){
		OrderLine o = new OrderLine();
		o.setOrderId(orderId);
		o.setProductId(PID);
		o.setQuantity(10);
		return o;
	}
	
	private SalesOrder createDummySalesOrder(String orderId){
		SalesOrder so = new SalesOrder(orderId, CID, 100);
		
		List<OrderLine> olist = new ArrayList<OrderLine>();
		olist.add(createDummyOrder(orderId));
		so.setOrderList(olist);
		
		return so;
	}

	@Before
	public void createDummyData(){
		customerService.insert(createDummyCustomer());
		productService.insert(createDummyProduct());
		salesOrderService.insert(createDummySalesOrder(OID));
		orderLineService.insert(createDummyOrder(OID));
        
        Assert.assertNotNull(salesOrderService.findSalesOrderById(OID));
	}
	
	@Test
    public void testAdd_Error_EmptyID(){
		int size0 = salesOrderService.selectAll().size();
		SalesOrder so = createDummySalesOrder(null);
		salesOrderService.insert(so);
        Assert.assertEquals(size0, salesOrderService.selectAll().size());
    }
	
	@Test
    public void testAdd_Error_EmptyCustomerId(){
		SalesOrder so = createDummySalesOrder("SO002");
		so.setCustomerId(null);
		salesOrderService.insert(so);
        Assert.assertNull(salesOrderService.findSalesOrderById("SO002"));
    }
	
	@Test
    public void testFind(){
		Assert.assertEquals(100.0f, salesOrderService.findSalesOrderById(OID).getTotalPrice(), 1.0f);
	}
	
	@Test
    public void testUpdate(){
		SalesOrder so = salesOrderService.findSalesOrderById(OID);
		Assert.assertEquals(100.0f, so.getTotalPrice(), 1.0f);
		so.setTotalPrice(200.0f);
		salesOrderService.update(so);
		
		Assert.assertEquals(200.0f, salesOrderService.findSalesOrderById(OID).getTotalPrice(), 1.0f);
	}
	
//	@Test
    public void testUpdate_Error_EmptyId(){
		SalesOrder so = salesOrderService.findSalesOrderById(OID);
		so.setOrderNumber(null);
		salesOrderService.update(so);
		
		Assert.assertEquals(OID, salesOrderService.findSalesOrderById(OID).getOrderNumber());
	}
	
	@Test
    public void testUpdate_Error_EmptyCustomerId(){
		SalesOrder so = salesOrderService.findSalesOrderById(OID);
		so.setCustomerId(null);
		salesOrderService.update(so);
		
		Assert.assertEquals(CID, salesOrderService.findSalesOrderById(OID).getCustomerId());
	}
	
	
	@After
    public void tearDown(){
		orderLineService.delete(orderLineService.findOrderLine(OID, PID));
		salesOrderService.delete(salesOrderService.findSalesOrderById(OID));
		customerService.delete(customerService.findCustomerById(CID));
		productService.delete(productService.findProductById(PID));
		
		Assert.assertEquals(0, orderLineService.selectAll().size());
		Assert.assertEquals(0, salesOrderService.selectAll().size());
		Assert.assertEquals(0, customerService.selectAll().size());
		Assert.assertEquals(0, productService.selectAll().size());
	}
}
