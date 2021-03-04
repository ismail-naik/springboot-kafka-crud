package com.org.tzl.repo;

import com.org.tzl.model.CustomerEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;

public interface CustomerRepo extends JpaRepository<CustomerEntity, String> {
	
	CustomerEntity findAllByFirstNameIsLike(String name);
	CustomerEntity getAllByFirstName(Pageable pageable);
	CustomerEntity findByCustomerId(int id);
	CustomerEntity deleteByCustomerId(String customerId);
	CustomerEntity findByApiKey(String apiKey);

 
}
