package com.org.tzl.service;

import com.org.tzl.model.CustomerEntity;
import com.org.tzl.repo.CustomerRepo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("CustomerService")
public class CustomerServiceImpl implements CustomerService {
	
    @Autowired
    private CustomerRepo customerRepo;

    public CustomerEntity findAllByFirstNameIsLike(String name) {
        return customerRepo.findAllByFirstNameIsLike(name);
    }
    
    @Transactional
    public CustomerEntity addCustomer(CustomerEntity customer) {
    	
       // brand.setId(brandRepo.getNextSeriesId().intValue());
    	System.out.println("Inside repo addbrand method");
        return customerRepo.save(customer);
    }

    @Transactional
    public CustomerEntity updateCustomer(CustomerEntity customer) {
        return customerRepo.save(customer);
    }



	@Override
	public CustomerEntity findByCustomerId(int id) {
		
		  return customerRepo.findByCustomerId(id);
	}

	@Override
	public CustomerEntity deleteCustomer(String customerId) {
		
		 return customerRepo.deleteByCustomerId(customerId);
	}
}
