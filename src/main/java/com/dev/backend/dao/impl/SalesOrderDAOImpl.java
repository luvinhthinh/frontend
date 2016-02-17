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

import com.dev.backend.dao.SalesOrderDAO;
import com.dev.backend.domain.SalesOrder;

public class SalesOrderDAOImpl implements SalesOrderDAO {
	private static final Logger logger = Logger.getLogger(SalesOrderDAOImpl.class);
	private SessionFactory sessionFactory;
	 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean insert(SalesOrder salesOrder) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(salesOrder);
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
	public boolean delete(SalesOrder salesOrder) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(salesOrder);
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
	public boolean update(SalesOrder salesOrder) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			if(findSalesOrderById(salesOrder.getOrderNumber()) != null){
				session.merge(salesOrder);
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
	public List<SalesOrder> selectAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(SalesOrder.class);
		List<SalesOrder> orders = (List<SalesOrder>) criteria.list();
		session.getTransaction().commit();
		return orders;
	}

	@Override
	@SuppressWarnings("unchecked")
	public SalesOrder findSalesOrderById(String id) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(SalesOrder.class);
			criteria.add(Restrictions.eq("orderNumber", id));
			List<SalesOrder> orderList = (List<SalesOrder>) criteria.list();
			
	        if (orderList != null && !orderList.isEmpty()) {
	            return orderList.get(0);
	        } 
		}catch(ConstraintViolationException  e){
			logger.error(e);
			tx.rollback();
		}catch(HibernateException  e){
			logger.error(e);
			tx.rollback();
		}
        return null;
	}

}
