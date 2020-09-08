package com.lucas.springdemo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lucas.springdemo.entity.Customer;

@Repository
public class CustomerDaoImpl implements CustomerDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Customer> getCustomers(){
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Customer> theQuery = currentSession.createQuery("from Customer"
															+ " order by lastName",
																Customer.class);
		
		List<Customer> customers = theQuery.getResultList();
		
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		
		Session currentSession = sessionFactory.getCurrentSession();
		
		currentSession.saveOrUpdate(theCustomer);
		
	}

	@Override
	public Customer getCustomer(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		
		Customer theCustomer = currentSession.get(Customer.class, theId);
		
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query theQuery = currentSession.createQuery("delete from Customer where id=:customerId");
		
		theQuery.setParameter("customerId", theId);
		
		theQuery.executeUpdate();
		
	}

	@Override
	public List<Customer> searchCustomer(String theSearchName) {
		Session currentSession = sessionFactory.getCurrentSession();
		
		List<Customer> customers = new ArrayList<Customer>();
		
		Query<Customer> theQuery = null;
		
		if(theSearchName != null && theSearchName.trim().length() > 0) {
			theQuery = currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) "
					+ "like :theName", Customer.class);
			
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
			customers = theQuery.getResultList();
			return customers;
		}
		
		theQuery = currentSession.createQuery("from Customer", Customer.class);
		customers = theQuery.getResultList();
		
		return customers;
		
		
	}
	
}
