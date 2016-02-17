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

import com.dev.backend.dao.OrderLineDAO;
import com.dev.backend.domain.OrderLine;

public class OrderLineDAOImpl implements OrderLineDAO {
	private static final Logger logger = Logger.getLogger(OrderLineDAOImpl.class);
	private SessionFactory sessionFactory;
	 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean insert(OrderLine orderLine) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(orderLine);
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
	public boolean delete(OrderLine orderLine) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(orderLine);
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
	public boolean update(OrderLine orderLine) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			OrderLine ol = findOrderLineById(orderLine.getOrderId(), orderLine.getProductId()); 
			if(ol != null){
				ol.setQuantity(orderLine.getQuantity());
				session.merge(ol);
			}
			session.getTransaction().commit();
			return true;
		}catch(Exception e){
			logger.error(e);
			tx.rollback();
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrderLine> selectAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(OrderLine.class);
		List<OrderLine> lines = (List<OrderLine>) criteria.list();
		session.getTransaction().commit();
		return lines;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrderLine> findProductByOrderId(String orderId) {
		List<OrderLine> orderList = null;
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(OrderLine.class);
			criteria.add(Restrictions.eq("orderId", orderId));
			orderList = (List<OrderLine>) criteria.list();
		}catch(ConstraintViolationException  e){
			logger.error(e);
		}catch(HibernateException  e){
			logger.error(e);
		}

		return orderList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public OrderLine findOrderLineById(String orderId, String itemId) {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(OrderLine.class);
			criteria.add(Restrictions.eq("orderId", orderId));
			criteria.add(Restrictions.eq("productId", itemId));
			List<OrderLine> orderList = (List<OrderLine>) criteria.list();
	         
	        if (orderList != null && !orderList.isEmpty()) {
	            return orderList.get(0);
	        }
		}catch(ConstraintViolationException  e){
			logger.error(e);
		}catch(HibernateException  e){
			logger.error(e);
		}
         
        return null;
	}

}
