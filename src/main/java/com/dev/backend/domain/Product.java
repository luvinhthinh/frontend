package com.dev.backend.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT")
public class Product implements Serializable{
	private static final long serialVersionUID = 3814954153915001505L;
	
	private String id, description;
	private float price;
	private int quantity;
	
	@Id
	@Column(name = "PRODUCT_ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "PRICE")
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
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
		return "Product [id=" + id + ", description=" + description + ", price=" + price + ", quantity=" + quantity + "]";
	}
}
	
