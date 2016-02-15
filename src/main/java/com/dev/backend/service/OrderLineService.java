package com.dev.backend.service;

import java.util.List;

import com.dev.backend.domain.OrderLine;

public interface OrderLineService {
	public void insert(OrderLine orderLine);
	public void delete(OrderLine orderLine);
	public void update(OrderLine orderLine);
	public List<OrderLine> selectAll();
	public List<OrderLine> findOrderLineByOrderNumber(String orderNumber);
	public OrderLine findOrderLine(String orderNumber, String productId);
	public boolean isOrderLineExist(OrderLine orderLine);
}
