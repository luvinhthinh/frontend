package com.dev.backend.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ORDER_LINE")
public class OrderLine implements Serializable {

	private static final long serialVersionUID = -8136210230693664750L;
	@Id	private String orderId;
	@Id private String productId;
	private int quantity;
	
	@Column(name = "ORDER_ID")
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Column(name = "PRODUCT_ID")
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	@Column(name = "QUANTITY")
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "OrderLine [orderId=" + orderId + ", productId=" + productId + ", quantity=" + quantity + "]";
	}
}
