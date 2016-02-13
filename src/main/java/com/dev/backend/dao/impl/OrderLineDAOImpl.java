package com.dev.backend.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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
		String hql = "from ORDER_LINE where ORDER_ID=" + orderId;
        Query query = session.createQuery(hql);
        List<OrderLine> lines = (List<OrderLine>) query.list();
        session.getTransaction().commit();
		return lines;
	}

	@Override
	@SuppressWarnings("unchecked")
	public OrderLine findOrderLineById(String orderId, String itemId) {
		Session session = getSessionFactory().getCurrentSession();
		String hql = "from ORDER_LINE where ORDER_ID=" + orderId + "AND PRODUCT_ID="+itemId;
        Query query = session.createQuery(hql);
        List<OrderLine> line = (List<OrderLine>) query.list();
        session.getTransaction().commit();
         
        if (line != null && !line.isEmpty()) {
            return line.get(0);
        }
         
        return null;
	}

}
