package com.dev.backend.service.impl;

import java.util.List;

import com.dev.backend.dao.OrderLineDAO;
import com.dev.backend.domain.OrderLine;
import com.dev.backend.service.OrderLineService;

public class OrderLineServiceImpl implements OrderLineService {
	OrderLineDAO orderLineDAO;
	
	@Override
	public void insert(OrderLine orderLine) {
		orderLineDAO.insert(orderLine);
	}

	@Override
	public void delete(OrderLine orderLine) {
		orderLineDAO.delete(orderLine);
	}

	@Override
	public void update(OrderLine orderLine) {
		orderLineDAO.update(orderLine);
	}

	@Override
	public List<OrderLine> selectAll() {
		return orderLineDAO.selectAll();
	}

	public OrderLineDAO getOrderLineDAO() {
		return orderLineDAO;
	}

	public void setOrderLineDAO(OrderLineDAO orderLineDAO) {
		this.orderLineDAO = orderLineDAO;
	}

	@Override
	public List<OrderLine> findOrderLineByOrderNumber(String orderNumber) {
		return this.orderLineDAO.findProductByOrderId(orderNumber);
	}

	@Override
	public OrderLine findOrderLine(String orderNumber, String productId) {
		return orderLineDAO.findOrderLineById(orderNumber, productId);
	}

	@Override
	public boolean isOrderLineExist(OrderLine orderLine) {
		return findOrderLine(orderLine.getOrderId(), orderLine.getProductId()) != null;
	}

}
