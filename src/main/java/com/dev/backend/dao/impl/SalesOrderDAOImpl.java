package com.dev.backend.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.dev.backend.dao.SalesOrderDAO;
import com.dev.backend.domain.SalesOrder;

public class SalesOrderDAOImpl implements SalesOrderDAO {

	private SessionFactory sessionFactory;
	 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void insert(SalesOrder salesOrder) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(salesOrder);
		session.getTransaction().commit();
	}

	@Override
	public void delete(SalesOrder salesOrder) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(salesOrder);
		session.getTransaction().commit();
	}

	@Override
	public void update(SalesOrder salesOrder) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.update(salesOrder);
		session.getTransaction().commit();
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
		session.beginTransaction();
		Criteria criteria = session.createCriteria(SalesOrder.class);
		criteria.add(Restrictions.eq("orderNumber", id));
		List<SalesOrder> orderList = (List<SalesOrder>) criteria.list();
		
        if (orderList != null && !orderList.isEmpty()) {
            return orderList.get(0);
        } 
		         
        return null;
	}

}
