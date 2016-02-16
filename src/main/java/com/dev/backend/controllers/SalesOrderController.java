package com.dev.backend.controllers;

import java.util.List;

import org.apache.log4j.Logger;
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
	private static final Logger logger = Logger.getLogger(SalesOrderController.class);
	
	SalesOrderService salesOrderService;
	OrderLineService orderLineService;

	@RequestMapping(value = "/salesOrder", method = RequestMethod.GET)
    public ResponseEntity<List<SalesOrder>> listAllSalesOrders() {
    	logger.info("Fetching All SalesOrder");
        List<SalesOrder> salesOrders = salesOrderService.selectAll();
        
        if(salesOrders.isEmpty()){
        	logger.info("Zero record found !");
            return new ResponseEntity<List<SalesOrder>>(HttpStatus.NO_CONTENT);
        }
        logger.info("Record found : " + salesOrders.size());
        return new ResponseEntity<List<SalesOrder>>(salesOrders, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/salesOrder/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SalesOrder> getSalesOrder(@PathVariable("id") String id) {
        logger.info("Fetching SalesOrder with id " + id);
        SalesOrder salesOrder = salesOrderService.findSalesOrderById(id);
        logger.info(salesOrder);
        if (salesOrder == null) {
            logger.info("SalesOrder with id " + id + " not found");
            return new ResponseEntity<SalesOrder>(HttpStatus.NOT_FOUND);
        }
        
        List<OrderLine> lines = orderLineService.findOrderLineByOrderNumber(id);
        logger.info("numberof order lines found in sales order : " + lines.size());
        salesOrder.setOrderList(lines);
        
        return new ResponseEntity<SalesOrder>(salesOrder, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/salesOrder", method = RequestMethod.POST)
    public ResponseEntity<Void> createSalesOrder(@RequestBody SalesOrder salesOrder) {
        List<OrderLine> oList = salesOrderService.consolidateOrderLine(salesOrder.getOrderList());
        salesOrder.setOrderList(oList);
        
    	if (salesOrderService.isSalesOrderExist(salesOrder)) {
            logger.info("A SalesOrder with id " + salesOrder.getOrderNumber() + " already exist");
            logger.info("Try updating ..." + salesOrder.toString());
            if(salesOrderService.validate(salesOrder)){
            	SalesOrder so = salesOrderService.findSalesOrderById(salesOrder.getOrderNumber());
                so.setOrderNumber(salesOrder.getOrderNumber());
                so.setCustomerId(salesOrder.getCustomerId());
                so.setTotalPrice(salesOrder.getTotalPrice());
                so.setOrderList(salesOrder.getOrderList());
                salesOrderService.update(so);

                return new ResponseEntity<Void>(HttpStatus.OK);
            }else{
        		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        	}
        }else{
        	logger.info("Try creating ..." + salesOrder.toString());
        	if(salesOrderService.validate(salesOrder)){
        		salesOrderService.insert(salesOrder);
            	return new ResponseEntity<Void>(HttpStatus.CREATED);
        	}else{
        		return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        	}
        }
    }

    /*
    @RequestMapping(value = "/orderLine", method = RequestMethod.POST)
//    public ResponseEntity<Void> createOrderLine(@RequestBody OrderLine orderLine) {
//        if (orderLineService.isOrderLineExist(orderLine)) {
//            logger.info("A OrderLine with id " + orderLine.getOrderId() + " already exist");
//            logger.info("Try updating ..." + orderLine.toString());
//            
//            OrderLine ol = orderLineService.findOrderLine(orderLine.getOrderId(), orderLine.getProductId());
//            ol.setOrderId(orderLine.getOrderId());
//            ol.setProductId(orderLine.getProductId());
//            ol.setQuantity(orderLine.getQuantity());
//            
//            orderLineService.update(ol);
//            return new ResponseEntity<Void>(HttpStatus.OK);
//        }else{
//        	logger.info("Try creating ..." + orderLine.toString());
//        	orderLineService.insert(orderLine);
//        	return new ResponseEntity<Void>(HttpStatus.CREATED);
//        }
//    }
    
//    @RequestMapping(value = "/orderLine/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<OrderLine>> getOrderLineByOrderNumber(@PathVariable("id") String id) {
//        logger.info("Fetching SalesOrder with id " + id);
//        List<OrderLine> orderLines = orderLineService.findOrderLineByOrderNumber(id);
//        
//        if(orderLines.isEmpty()){
//        	logger.info("Zero order line found !");
//            return new ResponseEntity<List<OrderLine>>(HttpStatus.NO_CONTENT);
//        }
//        logger.info("Record found : " + orderLines.size());
//        return new ResponseEntity<List<OrderLine>>(orderLines, HttpStatus.OK);
//    }
 
 */
    @RequestMapping(value = "/salesOrder/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<SalesOrder> deleteSalesOrder(@PathVariable("id") String id) {
        logger.info("Fetching & Deleting SalesOrder with id " + id);
 
        SalesOrder salesOrder = salesOrderService.findSalesOrderById(id);
        if (salesOrder == null) {
            logger.info("Unable to delete. SalesOrder with id " + id + " not found");
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
