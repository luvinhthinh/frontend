package com.dev.backend.service;

import java.util.List;

import com.dev.backend.domain.SalesOrder;

public interface SalesOrderService {
	public void insert(SalesOrder salesOrder);
	public void delete(SalesOrder salesOrder);
	public void update(SalesOrder salesOrder);
	public List<SalesOrder> selectAll();
	public SalesOrder findSalesOrderById(String id);
	public boolean isSalesOrderExist(SalesOrder salesOrder);
}
