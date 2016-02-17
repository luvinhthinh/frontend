package com.dev.backend.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.dev.backend.dao.ProductDAO;
import com.dev.backend.domain.Product;

public class ProductDAOImpl implements ProductDAO {
	private static final Logger logger = Logger.getLogger(ProductDAOImpl.class);
	private SessionFactory sessionFactory;
	 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean insert(Product product) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(product); 
			session.getTransaction().commit();
			return true;
		}catch(Exception e){
			logger.error(e);
			tx.rollback();
			return false;
		}
		
	}

	@Override
	@Transactional
	public boolean delete(Product product) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(product);
			session.getTransaction().commit();
			return true;
		}catch(Exception e){
			logger.error(e);
			tx.rollback();
			return false;
		}
	}

	@Override
	@Transactional
	public boolean update(Product product) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			if(findProductById(product.getId()) != null){
				session.merge(product);
			}
			session.getTransaction().commit();
			return true;
		}catch(Exception e){
			logger.error(e);
			tx.rollback();
			return false;
		}
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
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Product.class);
			criteria.add(Restrictions.eq("id", id));
			List<Product> productList = (List<Product>) criteria.list();
			
	        if (productList != null && !productList.isEmpty()) {
	            return productList.get(0);
	        } 
		}catch(ConstraintViolationException  e){
			logger.error(e);
		}catch(HibernateException  e){
			logger.error(e);
		}
        return null;
	}

}
