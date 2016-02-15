package com.dev.frontend.services;


import java.util.ArrayList;
import java.util.List;

import com.dev.backend.domain.*;

import com.dev.frontend.panels.ComboBoxItem;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Services 
{
	public static final int TYPE_PRODUCT = 1;
	public static final int TYPE_CUSTOMER = 2;
	public static final int TYPE_SALESORDER = 3;
	
	
	public static Object save(Object object,int objectType)
	{
		/*
		 * This method is called eventually after you click save on any edit screen
		 * object parameter is the return object from calling method guiToObject on edit screen
		 * and the type is identifier of the object type and may be TYPE_PRODUCT ,
		 * TYPE_CUSTOMER or TYPE_SALESORDER 
		 */ 
		try{
			ObjectMapper mapper = new ObjectMapper();
			if(objectType == TYPE_PRODUCT){
				String body = mapper.writeValueAsString((Product)object);
				Utils.httpPost("/product", body);
				return object;
			}else if (objectType == TYPE_CUSTOMER){
				String body = mapper.writeValueAsString((Customer)object);
				Utils.httpPost("/customer", body);
				return object;
			}else{
				SalesOrder so = (SalesOrder)object;
				String soBody = mapper.writeValueAsString(so);
				Utils.httpPost("/salesOrder", soBody);
				
				return object;
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	public static Object readRecordByCode(String code,int objectType)
	{
		/*
		 * This method is called when you select record in list view of any entity
		 * and also called after you save a record to re-bind the record again
		 * the code parameter is the first column of the row you have selected
		 * and the type is identifier of the object type and may be TYPE_PRODUCT ,
		 * TYPE_CUSTOMER or TYPE_SALESORDER */ 
		try{
			if(objectType == TYPE_PRODUCT){
				String line = Utils.httpGet("/product/"+code);
				return Utils.convertToObject(line, Product.class);
			}else if (objectType == TYPE_CUSTOMER){
				String line = Utils.httpGet("/customer/"+code);
				return Utils.convertToObject(line, Customer.class);
			}else{
				String line = Utils.httpGet("/salesOrder/"+code);
				SalesOrder so = (SalesOrder)Utils.convertToObject(line, SalesOrder.class);
				return so;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	public static boolean deleteRecordByCode(String code,int objectType)
	{
		/*
		 * This method is called when you click delete button on an edit view
		 * the code parameter is the code of (Customer - PRoduct ) or order number of Sales Order
		 * and the type is identifier of the object type and may be TYPE_PRODUCT ,
		 * TYPE_CUSTOMER or TYPE_SALESORDER
		 */ 
		try{
			if(objectType == TYPE_PRODUCT){
				Utils.httpDelete("/product/"+code);
			}else if (objectType == TYPE_CUSTOMER){
				Utils.httpDelete("/customer/"+code);
			}else{
				Utils.httpDelete("/salesOrder/"+code);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
	
	public static List<Object> listCurrentRecords(int objectType)
	{
		try{
			if(objectType == TYPE_PRODUCT){
				return Utils.convertToList(Utils.httpGet("/product/"), Product.class);
			}else if (objectType == TYPE_CUSTOMER){
				return Utils.convertToList(Utils.httpGet("/customer/"), Customer.class);
			}else{
				return Utils.convertToList(Utils.httpGet("/salesOrder/"), SalesOrder.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return new ArrayList<Object>();
	}
	public static List<ComboBoxItem> listCurrentRecordRefernces(int objectType) 
	{	
		/*
		 * This method is called when a Combo Box need to be initialized and should
		 * return list of ComboBoxItem which contains code and description/name for all records of specified type
		 */
		try{
			if(objectType == TYPE_PRODUCT){
				List<Object> pList =  Utils.convertToList(Utils.httpGet("/product/"), Product.class);
				if(pList != null && !pList.isEmpty()){
					List<ComboBoxItem> comboList = new ArrayList<ComboBoxItem>();
					for(Object o : pList){
						Product p = (Product)o;
						ComboBoxItem cb = new ComboBoxItem(p.getId(), p.getDescription());
						comboList.add(cb);
					}
					return comboList;
				}
				
			}else if (objectType == TYPE_CUSTOMER){
				List<Object> cList =  Utils.convertToList(Utils.httpGet("/customer/"), Customer.class);
				if(cList != null && !cList.isEmpty()){
					List<ComboBoxItem> comboList = new ArrayList<ComboBoxItem>();
					for(Object o : cList){
						Customer p = (Customer)o;
						ComboBoxItem cb = new ComboBoxItem(p.getId(), p.getName());
						comboList.add(cb);
					}
					return comboList;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return new ArrayList<ComboBoxItem>();
	}
	public static double getProductPrice(String productCode) {
		/*
		 * This method is used to get unit price of product with the code passed as a parameter
		 */
		String line = Utils.httpGet("/product/"+productCode);
		Product p = (Product) Utils.convertToObject(line, Product.class);
		
		return p.getPrice();
	}
}
