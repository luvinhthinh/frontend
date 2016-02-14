package com.dev.backend.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.dev.backend.dao.OrderLineDAO;
import com.dev.backend.domain.OrderLine;

public class OrderLineDAOImpl implements OrderLineDAO {

	private SessionFactory sessionFactory;
	 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void insert(OrderLine orderLine) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(orderLine);
		session.getTransaction().commit();
	}

	@Override
	public void delete(OrderLine orderLine) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(orderLine);
		session.getTransaction().commit();
	}

	@Override
	public void update(OrderLine orderLine) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.update(orderLine);
		session.getTransaction().commit();
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
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(OrderLine.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		List<OrderLine> orderList = (List<OrderLine>) criteria.list();
		
		return orderList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public OrderLine findOrderLineById(String orderId, String itemId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(OrderLine.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.add(Restrictions.eq("productId", itemId));
		List<OrderLine> orderList = (List<OrderLine>) criteria.list();
         
        if (orderList != null && !orderList.isEmpty()) {
            return orderList.get(0);
        }
         
        return null;
	}

}
