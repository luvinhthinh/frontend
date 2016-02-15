package com.dev.backend.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "SALES_ORDER")
public class SalesOrder implements Serializable {

	private static final long serialVersionUID = 87341779192489148L;

	private String orderNumber, customerId;
	private float totalPrice;
	
	private List<OrderLine> orderList;
	
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
		String s = "SalesOrder [orderNumber=" + orderNumber + ", customerId=" + customerId + ", totalPrice=" + totalPrice + "] \n";
		List<OrderLine> lines = getOrderList();
		if(lines != null && !lines.isEmpty()){
			for(OrderLine line : lines){
				s += "  "+ line.toString();
			}
		}
		return s;
	}
	
	public String[] toArray(){
		return new String[] {orderNumber, customerId, totalPrice+""};
	}
	
	@Transient
	public List<OrderLine> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderLine> orderList) {
		this.orderList = orderList;
	}
}
