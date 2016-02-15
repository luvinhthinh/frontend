package com.dev.backend.service;

import java.util.List;

import com.dev.backend.domain.Product;

public interface ProductService {
	public void insert(Product product);
	public void delete(Product product);
	public void update(Product product);
	public List<Product> selectAll();
	public Product findProductById(String id);
	public boolean isProductExist(Product product);
}
