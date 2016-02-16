package com.dev.backend.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dev.backend.dao.*;
import com.dev.backend.domain.*;
import com.dev.backend.service.SalesOrderService;

public class SalesOrderServiceImpl implements SalesOrderService {
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
	 * For each product added : Quantities that have been requested are less than or equal current inventory balance.
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
	public void insert(SalesOrder salesOrder) {
		// Update customer credit
		Customer c = customerDAO.findCustomerById(salesOrder.getCustomerId());
		c.setCredit(c.getCredit() + salesOrder.getTotalPrice());
		customerDAO.update(c);
		
		// Insert order
		salesOrderDAO.insert(salesOrder);
		
		List<OrderLine> lines = salesOrder.getOrderList();
    	if(lines != null && !lines.isEmpty()){
    		for(OrderLine line : lines){
    			
    			// Update product quantity
    			Product p = productDAO.findProductById(line.getProductId());
    			p.setQuantity(p.getQuantity() - line.getQuantity());
    			productDAO.update(p);
    			
    			// Insert order line
    			orderLineDAO.insert(line);
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
		// Update customer credit : New credit = Current credit + (current total - old total)
		Customer c = customerDAO.findCustomerById(salesOrder.getCustomerId());
		float oldTotal = salesOrderDAO.findSalesOrderById(salesOrder.getOrderNumber()).getTotalPrice();
		c.setCredit(c.getCredit() + (salesOrder.getTotalPrice() - oldTotal));
		customerDAO.update(c);
		
		// update sales order
		salesOrderDAO.update(salesOrder);
		
		List<OrderLine> lines = salesOrder.getOrderList();
    	if(lines != null && !lines.isEmpty()){
    		for(OrderLine line : lines){
    			// Update product quantity if such record exist, otherwise add new order line
    			OrderLine oldLine = orderLineDAO.findOrderLineById(salesOrder.getOrderNumber(), line.getProductId());
    			if(oldLine != null){
    				Product p = productDAO.findProductById(line.getProductId());
    				int oldQuantity = oldLine.getQuantity();
    				p.setQuantity(p.getQuantity() - (line.getQuantity() - oldQuantity));
        			productDAO.update(p);
    			}else{
    				orderLineDAO.insert(line);
    			}
    		}
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
