package com.dev.backend.service.impl;

import java.util.List;

import com.dev.backend.dao.ProductDAO;
import com.dev.backend.domain.Product;
import com.dev.backend.service.ProductService;

public class ProductServiceImpl implements ProductService {

	private ProductDAO productDAO;
	
	@Override
	public void insert(Product product) {
		productDAO.insert(product);
	}

	@Override
	public void delete(Product product) {
		productDAO.delete(product);
	}

	@Override
	public void update(Product product) {
		productDAO.update(product);
	}

	@Override
	public List<Product> selectAll() {
		return productDAO.selectAll();
	}

	@Override
	public Product findProductById(String id) {
		return productDAO.findProductById(id);
	}

	@Override
	public boolean isProductExist(Product product) {
		return findProductById(product.getId()) != null;
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

}
