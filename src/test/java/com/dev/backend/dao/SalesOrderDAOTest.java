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
	private final String CID="SC001";
	private final String PID="SP001";
	private final String OID="SO001";
	
	@Autowired
    private CustomerDAO customerDAO;
	
	@Autowired
	SalesOrderDAO salesOrderDAO;
	
	@Autowired
	OrderLineDAO orderLineDAO;
	
	@Autowired
    private ProductDAO productDAO;
	
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
        customerDAO.insert(createDummyCustomer());
        productDAO.insert(createDummyProduct());
        salesOrderDAO.insert(createDummySalesOrder(OID));
        orderLineDAO.insert(createDummyOrder(OID));
        
        Assert.assertNotNull(salesOrderDAO.findSalesOrderById(OID));
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testAdd_Error_EmptyID(){
		int size0 = salesOrderDAO.selectAll().size();
		SalesOrder so = createDummySalesOrder(null);
		salesOrderDAO.insert(so);
        Assert.assertEquals(size0, salesOrderDAO.selectAll().size());
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testAdd_Error_EmptyCustomerId(){
		SalesOrder so = createDummySalesOrder("SO002");
		so.setCustomerId(null);
		salesOrderDAO.insert(so);
        Assert.assertNull(salesOrderDAO.findSalesOrderById("SO002"));
    }
	
	@Test
    @Transactional
    @Rollback(true)
    public void testFind(){
		Assert.assertEquals(100.0f, salesOrderDAO.findSalesOrderById(OID).getTotalPrice(), 1.0f);
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate(){
		SalesOrder so = salesOrderDAO.findSalesOrderById(OID);
		Assert.assertEquals(100.0f, so.getTotalPrice(), 1.0f);
		so.setTotalPrice(200.0f);
		salesOrderDAO.update(so);
		
		Assert.assertEquals(200.0f, salesOrderDAO.findSalesOrderById(OID).getTotalPrice(), 1.0f);
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_EmptyId(){
		SalesOrder so = salesOrderDAO.findSalesOrderById(OID);
		so.setOrderNumber(null);
		salesOrderDAO.update(so);
		
		Assert.assertEquals(OID, salesOrderDAO.findSalesOrderById(OID).getOrderNumber());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_EmptyCustomerId(){
		SalesOrder so = salesOrderDAO.findSalesOrderById(OID);
		so.setCustomerId(null);
		salesOrderDAO.update(so);
		
		Assert.assertEquals(CID, salesOrderDAO.findSalesOrderById(OID).getCustomerId());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testUpdate_Error_InvalidID(){
		SalesOrder so = salesOrderDAO.findSalesOrderById(OID);
		so.setOrderNumber("SO00000");
		salesOrderDAO.update(so);
		
		Assert.assertEquals(OID, salesOrderDAO.findSalesOrderById(OID).getOrderNumber());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testDelete_Error_InvalidId(){
		int size0 = salesOrderDAO.selectAll().size();
		SalesOrder so = createDummySalesOrder("SO00000");
		salesOrderDAO.delete(so);
		Assert.assertNull(salesOrderDAO.findSalesOrderById("SO00000"));
		Assert.assertEquals(size0, salesOrderDAO.selectAll().size());
	}
	
	@Test
    @Transactional
    @Rollback(true)
    public void testSelectAll(){
		int size0 = salesOrderDAO.selectAll().size();
		salesOrderDAO.insert(createDummySalesOrder("SO00001"));
		salesOrderDAO.insert(createDummySalesOrder("SO00002"));
		Assert.assertEquals(size0+2, salesOrderDAO.selectAll().size());
		salesOrderDAO.delete(salesOrderDAO.findSalesOrderById("SO00001"));
		salesOrderDAO.delete(salesOrderDAO.findSalesOrderById("SO00002"));
		Assert.assertEquals(size0, salesOrderDAO.selectAll().size());
	}
	
	@After
    @Transactional
    @Rollback(true)
    public void tearDown(){
		orderLineDAO.delete(orderLineDAO.findOrderLineById(OID, PID));
		salesOrderDAO.delete(salesOrderDAO.findSalesOrderById(OID));
		customerDAO.delete(customerDAO.findCustomerById(CID));
		productDAO.delete(productDAO.findProductById(PID));
		
		Assert.assertEquals(0, orderLineDAO.selectAll().size());
		Assert.assertEquals(0, salesOrderDAO.selectAll().size());
		Assert.assertEquals(0, customerDAO.selectAll().size());
		Assert.assertEquals(0, productDAO.selectAll().size());
	}
}
