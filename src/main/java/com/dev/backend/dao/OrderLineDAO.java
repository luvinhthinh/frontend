package com.dev.backend.dao;

import java.util.List;
import com.dev.backend.domain.OrderLine;

public interface OrderLineDAO {
	
	public boolean insert(OrderLine orderLine);
	public boolean delete(OrderLine orderLine);
	public boolean update(OrderLine orderLine);
	public List<OrderLine> selectAll();
	public OrderLine findOrderLineById(String orderId, String productId);
	public List<OrderLine> findProductByOrderId(String orderId);
}
