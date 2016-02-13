package com.dev.backend.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dev.backend.dao.ProductDAO;
import com.dev.backend.domain.Product;

public class ProductDAOImpl implements ProductDAO {

	private SessionFactory sessionFactory;
	 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void insert(Product product) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(product);
		session.getTransaction().commit();
	}

	@Override
	public void delete(Product product) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(product);
		session.getTransaction().commit();
	}

	@Override
	public void update(Product product) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.update(product);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> selectAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
		List<Product> products = (List<Product>) criteria.list();
		session.getTransaction().commit();
		return products;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Product findProductById(String id) {
		String hql = "from PRODUCT where PRODUCT_ID=" + id;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        List<Product> productList = (List<Product>) query.list();
         
        if (productList != null && !productList.isEmpty()) {
            return productList.get(0);
        }
         
        return null;
	}

}
