package com.dev.backend.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dev.backend.dao.CustomerDAO;
import com.dev.backend.domain.Customer;

public class CustomerDAOImpl implements CustomerDAO{
	
	private SessionFactory sessionFactory;
	 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void insert(Customer customer) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(customer);
		session.getTransaction().commit();
	}
		 
	@SuppressWarnings("unchecked")
	@Override
	public List<Customer> selectAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Customer.class);
		List<Customer> persons = (List<Customer>) criteria.list();
		session.getTransaction().commit();
		return persons;
	}

	@Override
	public void delete(Customer customer) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.delete(customer);
		session.getTransaction().commit();
	}

	@Override
	public void update(Customer customer) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.update(customer);
		session.getTransaction().commit();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Customer findCustomerById(String id) {
		String hql = "from CUSTOMER where CUSTOMER_ID=" + id;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        List<Customer> customerList = (List<Customer>) query.list();
         
        if (customerList != null && !customerList.isEmpty()) {
            return customerList.get(0);
        }
         
        return null;
	}
}