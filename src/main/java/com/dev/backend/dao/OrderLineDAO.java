package com.dev.backend.dao;

import java.util.List;
import com.dev.backend.domain.OrderLine;

public interface OrderLineDAO {
	
	public void insert(OrderLine orderLine);
	public void delete(OrderLine orderLine);
	public void update(OrderLine orderLine);
	public List<OrderLine> selectAll();
	public OrderLine findOrderLineById(String orderId, String productId);
	public List<OrderLine> findProductByOrderId(String orderId);
}
