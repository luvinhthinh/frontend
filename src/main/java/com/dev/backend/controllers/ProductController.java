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

import com.dev.backend.domain.Product;
import com.dev.backend.service.ProductService;

@RestController
public class ProductController {
	private static final Logger logger = Logger.getLogger(ProductController.class);
	
	ProductService productService;

	@RequestMapping(value = "/product", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> listAllProducts() {
    	logger.info("Fetching All Product");
        List<Product> products = productService.selectAll();
        
        if(products.isEmpty()){
        	logger.info("Zero record found !");
            return new ResponseEntity<List<Product>>(HttpStatus.NO_CONTENT);
        }
        logger.info("Record found : " + products.size());
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
        logger.info("Fetching Product with id " + id);
        Product product = productService.findProductById(id);
        if (product == null) {
            logger.info("Product with id " + id + " not found");
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        if (productService.isProductExist(product)) {
            logger.info("A Product with name " + product.getDescription() + " already exist");
            logger.info("Try updating ..." + product.toString());
            
            Product p = productService.findProductById(product.getId());
            p.setId(product.getId());
			p.setDescription(product.getDescription());
			p.setPrice(product.getPrice());
			p.setQuantity(product.getQuantity());
            
            productService.update(p);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }else{
        	logger.info("Try creating ..." + product.toString());
        	productService.insert(product);
        	return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
    }
 
    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id) {
        logger.info("Fetching & Deleting Product with id " + id);
 
        Product product = productService.findProductById(id);
        if (product == null) {
            logger.info("Unable to delete. Product with id " + id + " not found");
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
 
        productService.delete(product);
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }

	
	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
