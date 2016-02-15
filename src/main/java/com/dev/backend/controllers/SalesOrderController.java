package com.dev.backend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.backend.domain.OrderLine;
import com.dev.backend.domain.SalesOrder;
import com.dev.backend.service.OrderLineService;
import com.dev.backend.service.SalesOrderService;

@RestController
public class SalesOrderController {
	SalesOrderService salesOrderService;
	OrderLineService orderLineService;

	@RequestMapping(value = "/salesOrder", method = RequestMethod.GET)
    public ResponseEntity<List<SalesOrder>> listAllSalesOrders() {
    	System.out.println("Fetching All SalesOrder");
        List<SalesOrder> salesOrders = salesOrderService.selectAll();
        
        if(salesOrders.isEmpty()){
        	System.out.println("Zero record found !");
            return new ResponseEntity<List<SalesOrder>>(HttpStatus.NO_CONTENT);
        }
        System.out.println("Record found : " + salesOrders.size());
        return new ResponseEntity<List<SalesOrder>>(salesOrders, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/salesOrder/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SalesOrder> getSalesOrder(@PathVariable("id") String id) {
        System.out.println("Fetching SalesOrder with id " + id);
        SalesOrder salesOrder = salesOrderService.findSalesOrderById(id);
        System.out.println(salesOrder);
        if (salesOrder == null) {
            System.out.println("SalesOrder with id " + id + " not found");
            return new ResponseEntity<SalesOrder>(HttpStatus.NOT_FOUND);
        }
        
        List<OrderLine> lines = orderLineService.findOrderLineByOrderNumber(id);
        System.out.println("numberof order lines found in sales order : " + lines.size());
        salesOrder.setOrderList(lines);
        
        return new ResponseEntity<SalesOrder>(salesOrder, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/salesOrder", method = RequestMethod.POST)
    public ResponseEntity<Void> createSalesOrder(@RequestBody SalesOrder salesOrder) {
        if (salesOrderService.isSalesOrderExist(salesOrder)) {
            System.out.println("A SalesOrder with id " + salesOrder.getOrderNumber() + " already exist");
            System.out.println("Try updating ..." + salesOrder.toString());
            
            SalesOrder so = salesOrderService.findSalesOrderById(salesOrder.getOrderNumber());
            so.setOrderNumber(salesOrder.getOrderNumber());
            so.setCustomerId(salesOrder.getCustomerId());
            so.setTotalPrice(salesOrder.getTotalPrice());
            so.setOrderList(salesOrder.getOrderList());
            salesOrderService.update(so);

            return new ResponseEntity<Void>(HttpStatus.OK);
        }else{
        	System.out.println("Try creating ..." + salesOrder.toString());
        	salesOrderService.insert(salesOrder);
        	
        	return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
    }

    /*
    @RequestMapping(value = "/orderLine", method = RequestMethod.POST)
//    public ResponseEntity<Void> createOrderLine(@RequestBody OrderLine orderLine) {
//        if (orderLineService.isOrderLineExist(orderLine)) {
//            System.out.println("A OrderLine with id " + orderLine.getOrderId() + " already exist");
//            System.out.println("Try updating ..." + orderLine.toString());
//            
//            OrderLine ol = orderLineService.findOrderLine(orderLine.getOrderId(), orderLine.getProductId());
//            ol.setOrderId(orderLine.getOrderId());
//            ol.setProductId(orderLine.getProductId());
//            ol.setQuantity(orderLine.getQuantity());
//            
//            orderLineService.update(ol);
//            return new ResponseEntity<Void>(HttpStatus.OK);
//        }else{
//        	System.out.println("Try creating ..." + orderLine.toString());
//        	orderLineService.insert(orderLine);
//        	return new ResponseEntity<Void>(HttpStatus.CREATED);
//        }
//    }
    
//    @RequestMapping(value = "/orderLine/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<OrderLine>> getOrderLineByOrderNumber(@PathVariable("id") String id) {
//        System.out.println("Fetching SalesOrder with id " + id);
//        List<OrderLine> orderLines = orderLineService.findOrderLineByOrderNumber(id);
//        
//        if(orderLines.isEmpty()){
//        	System.out.println("Zero order line found !");
//            return new ResponseEntity<List<OrderLine>>(HttpStatus.NO_CONTENT);
//        }
//        System.out.println("Record found : " + orderLines.size());
//        return new ResponseEntity<List<OrderLine>>(orderLines, HttpStatus.OK);
//    }
 
 */
    @RequestMapping(value = "/salesOrder/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<SalesOrder> deleteSalesOrder(@PathVariable("id") String id) {
        System.out.println("Fetching & Deleting SalesOrder with id " + id);
 
        SalesOrder salesOrder = salesOrderService.findSalesOrderById(id);
        if (salesOrder == null) {
            System.out.println("Unable to delete. SalesOrder with id " + id + " not found");
            return new ResponseEntity<SalesOrder>(HttpStatus.NOT_FOUND);
        }
 
        salesOrderService.delete(salesOrder);
        return new ResponseEntity<SalesOrder>(HttpStatus.NO_CONTENT);
    }
	
	public SalesOrderService getSalesOrderService() {
		return salesOrderService;
	}

	public void setSalesOrderService(SalesOrderService salesOrderService) {
		this.salesOrderService = salesOrderService;
	}

	public OrderLineService getOrderLineService() {
		return orderLineService;
	}

	public void setOrderLineService(OrderLineService orderLineService) {
		this.orderLineService = orderLineService;
	}
}
