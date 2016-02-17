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

import com.dev.backend.dao.CustomerDAO;
import com.dev.backend.domain.Customer;

public class CustomerDAOImpl implements CustomerDAO{
	private static final Logger logger = Logger.getLogger(CustomerDAOImpl.class);
	private SessionFactory sessionFactory;
	 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean insert(Customer customer) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(customer);
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
	public List<Customer> selectAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Customer.class);
		List<Customer> customers = (List<Customer>) criteria.list();
		session.getTransaction().commit();
		return customers;
	}

	@Override
	@Transactional
	public boolean delete(Customer customer) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.delete(customer);
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
	public boolean update(Customer customer) {
		Session session = getSessionFactory().getCurrentSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			if(findCustomerById(customer.getId()) != null){
				session.merge(customer);
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
	public Customer findCustomerById(String id) {
		try{
			Session session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Customer.class);
			criteria.add(Restrictions.eq("id", id));
			List<Customer> customerList = (List<Customer>) criteria.list();
			
	        if (customerList != null && !customerList.isEmpty()) {
	            return customerList.get(0);
	        }
		}catch(ConstraintViolationException  e){
			logger.error(e);
		}catch(HibernateException  e){
			logger.error(e);
		}
		
        return null;
	}
}
