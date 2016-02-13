package com.dev.backend.dao;

import java.util.List;
import com.dev.backend.domain.OrderLine;

public interface OrderLineDAO {
	
	public void insert(OrderLine customer);
	public void delete(OrderLine customer);
	public void update(OrderLine customer);
	public List<OrderLine> selectAll();
	public OrderLine findOrderLineById(String orderId, String itemId);
}
