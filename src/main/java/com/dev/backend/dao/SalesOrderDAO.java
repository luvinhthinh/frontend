package com.dev.backend.dao;

import java.util.List;
import com.dev.backend.domain.SalesOrder;

public interface SalesOrderDAO {
	
	public boolean insert(SalesOrder salesOrder);
	public boolean delete(SalesOrder salesOrder);
	public boolean update(SalesOrder salesOrder);
	public List<SalesOrder> selectAll();
	public SalesOrder findSalesOrderById(String id);
}
