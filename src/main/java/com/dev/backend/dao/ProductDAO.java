package com.dev.backend.dao;

import java.util.List;
import com.dev.backend.domain.Product;

public interface ProductDAO {
	
	public boolean insert(Product product);
	public boolean delete(Product product);
	public boolean update(Product product);
	public List<Product> selectAll();
	public Product findProductById(String id);
}
