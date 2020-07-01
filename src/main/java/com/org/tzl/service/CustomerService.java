package com.org.tzl.service;
import com.org.tzl.model.CustomerEntity;

public interface CustomerService {
	CustomerEntity findAllByFirstNameIsLike(String name);
    //Page<BrandEntity> getAllByBrandName(int page, int size, String sort);
    CustomerEntity addCustomer(CustomerEntity customer);
    CustomerEntity findByCustomerId(int id);
    CustomerEntity updateCustomer(CustomerEntity customerEntity);
    CustomerEntity deleteCustomer(String customerId);
}
