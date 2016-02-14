package com.dev.frontend.panels.list;

import java.util.List;

import com.dev.backend.domain.Product;
import com.dev.frontend.services.Services;


public class ProductDataModel extends ListDataModel
{
	private static final long serialVersionUID = 7526529951747614655L;

	public ProductDataModel() 
	{
		super(new String[]{"Code","Description","Price","Quantity"}, 0);
	}

	@Override
	public int getObjectType() {
		return Services.TYPE_PRODUCT;
	}

	@Override
	public String[][] convertRecordsListToTableModel(List<Object> list) 
	{
		/*
		 * This method use list returned by Services.listCurrentRecords and should convert it to array of rows
		 * each row is another array of columns of the row
		 */
//		String[][] sampleData = new String [][]{{"01","Product 1","12.5","25"},{"02","Product 2","10","8"}};
		int noOfItem = list.size();
		String[][] data = new String[noOfItem][4];
		for(int i=0; i < noOfItem; i++){
			data[i] = ((Product)list.get(i)).toArray();
		}
		return data;
	}
}
