package com.dev.frontend.panels.list;

import java.util.List;

import com.dev.backend.domain.SalesOrder;
import com.dev.frontend.services.Services;


public class SalesOrderDataModel extends ListDataModel
{
	private static final long serialVersionUID = 7526529951747614655L;

	public SalesOrderDataModel() 
	{
		super(new String[]{"Order Number","Customer","Total Price"}, 0);
	}

	@Override
	public int getObjectType() {
		return Services.TYPE_SALESORDER;
	}

	@Override
	public String[][] convertRecordsListToTableModel(List<Object> list) 
	{
		/*
		 * This method use list returned by Services.listCurrentRecords and should convert it to array of rows
		 * each row is another array of columns of the row
		 */
		int noOfItem = list.size();
		String[][] data = new String[noOfItem][4];
		for(int i=0; i < noOfItem; i++){ 
			data[i] = ((SalesOrder)list.get(i)).toArray();
		}
		return data;
		
//		String[][] sampleData = new String [][]{{"22423","(01)Customer 1","122.5"},{"22424","(02)Customer 2","3242.5"}};
//		return sampleData;
	}
}
