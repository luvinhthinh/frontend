package com.dev.backend.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {
	private static final long serialVersionUID = -2784189889183530138L;

	private String id, name, address, phone1, phone2;
	private float credit;
	private int limit;
	
	public Customer(){}
	
	public Customer(String id, String name, String address, String phone1, String phone2, float credit, int limit){
		this.id = id;
		this.name = name;
		this.address = address;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.credit = credit;
		this.limit = limit;
	}
	
	@Id
	@Column(name = "CUSTOMER_ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "PHONE1")
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	
	@Column(name = "PHONE2")
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	
	@Column(name = "CREDIT")
	public float getCredit() {
		return credit;
	}
	public void setCredit(float credit) {
		this.credit = credit;
	}
	
	@Column(name = "CREDIT_LIMIT")
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", address=" + address + 
				", phone1=" + phone1 + ", phone2=" + phone2 + ", credit=" + credit + ", limit=" + limit + "]";
	}
	
	public String[] toArray(){
		return new String[] {id, name, phone1, credit+""};
	}
}
