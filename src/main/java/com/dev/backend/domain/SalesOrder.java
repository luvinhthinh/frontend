package com.dev.backend.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SALES_ORDER")
public class SalesOrder implements Serializable {

	private static final long serialVersionUID = 87341779192489148L;

	private String orderNumber, customerId;
	private float totalPrice;
	
	@Id
	@Column(name = "ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@Column(name = "CUSTOMER_ID")
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Column(name = "TOTAL_PRICE")
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	@Override
	public String toString() {
		return "SalesOrder [orderNumber=" + orderNumber + ", customerId=" + customerId + ", totalPrice=" + totalPrice + "]";
	}
}
