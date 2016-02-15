package com.dev.backend.service.impl;

import java.util.List;

import com.dev.backend.dao.OrderLineDAO;
import com.dev.backend.dao.SalesOrderDAO;
import com.dev.backend.domain.OrderLine;
import com.dev.backend.domain.SalesOrder;
import com.dev.backend.service.SalesOrderService;

public class SalesOrderServiceImpl implements SalesOrderService {
	SalesOrderDAO salesOrderDAO;
	OrderLineDAO orderLineDAO;
	
	@Override
	public void insert(SalesOrder salesOrder) {
		salesOrderDAO.insert(salesOrder);
		
		List<OrderLine> lines = salesOrder.getOrderList();
    	if(lines != null && !lines.isEmpty()){
    		for(OrderLine line : lines){
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
		salesOrderDAO.update(salesOrder);
		
		List<OrderLine> lines = salesOrder.getOrderList();
    	if(lines != null && !lines.isEmpty()){
    		for(OrderLine line : lines){
    			orderLineDAO.update(line);
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

}
