package com.dev.backend.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.dev.backend.dao.*;
import com.dev.backend.domain.*;
import com.dev.backend.service.SalesOrderService;

public class SalesOrderServiceImpl implements SalesOrderService {
	private static final Logger logger = Logger.getLogger(SalesOrderServiceImpl.class);
	
	SalesOrderDAO salesOrderDAO;
	OrderLineDAO orderLineDAO;
	ProductDAO productDAO;
	CustomerDAO customerDAO;
	
	/**
	 * To validate if : Quantities that have been requested are less than or equal current inventory balance.
	 * @param productId
	 * @param quantity
	 * @return
	 */
	public boolean isValidQuantity(String productId, int quantity){
		Product p =  productDAO.findProductById(productId);
		return p != null && p.getQuantity() >= quantity;
	}
	
	/**
	 * To validate if : Total price of sales order is less than or equal (Customer Credit Limit - Customer Current Credit).
	 * @param customerId
	 * @param credit
	 * @return
	 */
	public boolean isValidCredit(String customerId, float credit){
		Customer c = customerDAO.findCustomerById(customerId);
		return c != null && (c.getLimit()-c.getCredit()) >= credit;
	}
	
	/**
	 * To validate input order if it satisfies : 
	 * For each product added, check: 
	 * Quantities that have been requested are less than or equal current inventory balance.
	 * Total price of sales order is less than or equal (Customer Credit Limit - Customer Current Credit).
	 * @param salesOrder
	 * @return
	 */
	public boolean validate(SalesOrder salesOrder){
		boolean criteria1 = isValidCredit(salesOrder.getCustomerId(), salesOrder.getTotalPrice());
		boolean criteria2 = true;
		
		List<OrderLine> lines = salesOrder.getOrderList();
		if(lines != null && !lines.isEmpty()){
			for(OrderLine line : lines){
				criteria2 &= isValidQuantity(line.getProductId(), line.getQuantity());
			}
		}
		
		return criteria1 && criteria2;
	}
	
	/**
	 * To consolidate list of order lines which contains duplicated product id
	 * e.g. input : [{'order01', 'P01', 10}, {'order01', 'P01', 5}, {'order01', 'P02', 10}]
	 * output : [{'order01', 'P01', 15}, {'order01', 'P02', 10}]
	 * @param lines
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<OrderLine> consolidateOrderLine(List<OrderLine> lines){
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<OrderLine> newLines = new ArrayList<OrderLine>();
		
		
		if(lines != null && !lines.isEmpty()){
			String orderId = lines.get(0).getOrderId();
			// construct map of product id and its quantity
			for(OrderLine line : lines){
				String key = line.getProductId();
				if(map.containsKey(key)){
					map.put(key, map.get(key) + line.getQuantity());
				}else{
					map.put(key, line.getQuantity());
				}
			}
			
			Iterator<Entry<String, Integer>> it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        OrderLine ol = new OrderLine();
		        ol.setOrderId(orderId);
		        ol.setProductId((String)pair.getKey());
		        ol.setQuantity((int) pair.getValue());
		        it.remove();
		        newLines.add(ol);
		    }
		}
		
		return newLines;
	}
	
	
	@Override
	@Transactional
	public void insert(SalesOrder salesOrder) {
		boolean action1 = true, action2 = true, action3 = true, action4 = true;
		
		action1 = salesOrderDAO.insert(salesOrder);
		
		//Successfully add new sales order
		if(action1){
			Customer c = customerDAO.findCustomerById(salesOrder.getCustomerId());
			float bk_credit = c.getCredit();
			c.setCredit(bk_credit + salesOrder.getTotalPrice());
			action2 = customerDAO.update(c);
			
			//Successfully update credit
			if(action2){
				List<OrderLine> lines = salesOrder.getOrderList();
		    	if(lines != null && !lines.isEmpty()){
		    		for(OrderLine line : lines){
		    			// Insert order line
		    			action4 &= orderLineDAO.insert(line);
		    			
		    			//Successfully insert new order line
		    			if(action4){
		    				// Update product quantity
			    			Product p = productDAO.findProductById(line.getProductId());
			    			int bk_quantity = p.getQuantity();
			    			p.setQuantity(bk_quantity - line.getQuantity());
			    			action3 &= productDAO.update(p);
			    			
			    			//Fail to update product stock
			    			// -> Rollback (remove new order line)
			    			if(!action3){
			    				orderLineDAO.delete(line);
			    			}
		    			}else{
		    				break;
		    			}
		    		}
		    	}
		    	
		    	// Fail to update product stock or insert new order line
		    	// -> Rollback (revert customer credit and remove new sales order)
		    	if(!action3 || !action4){
					c.setCredit(bk_credit);
					customerDAO.update(c);
					
					salesOrderDAO.delete(salesOrder);
				}
			}
			//Fail to update credit -> Rollback ( remove new sales order)
			else{
				salesOrderDAO.delete(salesOrder);
			}
		}
		
	}

	@Override
	public void delete(SalesOrder salesOrder) {
		List<OrderLine> lines = salesOrder.getOrderList();
    	if(lines != null && !lines.isEmpty()){
    		for(OrderLine line : lines){
    			orderLineDAO.delete(line);
    		}
    	}
		
		salesOrderDAO.delete(salesOrder);
	}

	@Override
	public void update(SalesOrder salesOrder) {
		boolean action1 = true, action2 = true, action3 = true, action4 = true;
		
		try{
			// Preserve data from DB
			SalesOrder oldSalesOrder = salesOrderDAO.findSalesOrderById(salesOrder.getOrderNumber());
			String oldCustomerId = oldSalesOrder.getCustomerId();
			float oldTotalPrice = oldSalesOrder.getTotalPrice();
			
			// update sales order
			action2 = salesOrderDAO.update(salesOrder);
			if(action2){
				Customer c = customerDAO.findCustomerById(salesOrder.getCustomerId());
				float bk_credit = c.getCredit();
				c.setCredit(bk_credit + (salesOrder.getTotalPrice() - oldTotalPrice));
				action1 = customerDAO.update(c);
				
				// Successfully update customer credit
				if(action1){
					List<OrderLine> lines = salesOrder.getOrderList();
			    	if(lines != null && !lines.isEmpty()){
			    		for(OrderLine line : lines){
			    			
			    			// Update product quantity if such record exist, otherwise add new order line
			    			OrderLine oldLine = orderLineDAO.findOrderLineById(salesOrder.getOrderNumber(), line.getProductId());
			    			Product p = productDAO.findProductById(line.getProductId());
			    			int oldStock = p.getQuantity();
			    			
			    			//Existing record
			    			if(oldLine != null){
			    				int oldQuantity = oldLine.getQuantity();
			    				p.setQuantity(p.getQuantity() - (line.getQuantity() - oldQuantity));
			    				action3 &= productDAO.update(p);
			    				action4 &= orderLineDAO.update(line);
			    				
			    				//If fails to update new product stock or update order line
			    				// -> Rollback 
			    				if(!action3 || !action4){
				    				p.setQuantity(oldStock);
				    				productDAO.update(p);
				    				
				    				line.setQuantity(oldQuantity);
				    				orderLineDAO.update(line);
				    			}
			    			}
			    			// New record
			    			else{
				    			p.setQuantity(oldStock - line.getQuantity());
				    			action3 &= productDAO.update(p);
				    			action4 &= orderLineDAO.insert(line);
				    			
				    			//If fails to update new product stock or add order line
			    				// -> Rollback 
				    			if(!action3 || !action4){
				    				p.setQuantity(oldStock);
				    				productDAO.update(p);
				    				orderLineDAO.delete(line);
				    			}
			    			}
			    			
			    		}
			    	}
				
			    	// Fail to update product stock or insert/update new order line
			    	// -> Rollback (revert customer credit and remove new sales order)
			    	if(!action3 || !action4){
						c.setCredit(bk_credit);
						customerDAO.update(c);
						
						oldSalesOrder.setCustomerId(oldCustomerId);
						oldSalesOrder.setTotalPrice(oldTotalPrice);
						salesOrderDAO.update(oldSalesOrder);
					}
				}
				// Fail to update customer credit
				// -> Rollback (Revert update of sales order)
				else{
					oldSalesOrder.setCustomerId(oldCustomerId);
					oldSalesOrder.setTotalPrice(oldTotalPrice);
					salesOrderDAO.update(oldSalesOrder);
				}
			}
		}catch(Exception e){
			logger.error(e);
		}
	}

	@Override
	public List<SalesOrder> selectAll() {
		return salesOrderDAO.selectAll();
	}

	public SalesOrderDAO getSalesOrderDAO() {
		return salesOrderDAO;
	}

	public void setSalesOrderDAO(SalesOrderDAO salesOrderDAO) {
		this.salesOrderDAO = salesOrderDAO;
	}

	@Override
	public SalesOrder findSalesOrderById(String id) {
		return salesOrderDAO.findSalesOrderById(id);
	}

	@Override
	public boolean isSalesOrderExist(SalesOrder salesOrder) {
		return findSalesOrderById(salesOrder.getOrderNumber()) != null;
	}

	public OrderLineDAO getOrderLineDAO() {
		return orderLineDAO;
	}

	public void setOrderLineDAO(OrderLineDAO orderLineDAO) {
		this.orderLineDAO = orderLineDAO;
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

}
