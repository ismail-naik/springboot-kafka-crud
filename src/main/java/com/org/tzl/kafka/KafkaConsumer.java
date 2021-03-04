package com.org.tzl.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.tzl.model.CustomerEntity;

import com.org.tzl.service.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@Component
@Transactional
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    CustomerService customerService;


   // private CustomerEntity custEntityKafka = new CustomerEntity();

    @KafkaListener(topics = "kafka.post.customer", groupId = "customers")
    public void processPostBrand(String customerJSON){
        logger.info("received content = '{}'", customerJSON);
        try{
            ObjectMapper mapper = new ObjectMapper();
            CustomerEntity customerEntity = mapper.readValue(customerJSON, CustomerEntity.class);
            System.out.println(customerEntity);
            CustomerEntity customer = customerService.addCustomer(customerEntity);
            logger.info("Success process brand '{}' with topic '{}'", customer.getCustomerId(), "kafka.post.customer");
        } catch (Exception e){
            logger.error("An error occurred! '{}'", e.getMessage());
        }
    }

	/*
	 * @KafkaListener(topics = "kafka.put.customer" , groupId = "customers") public
	 * void processPutCustomer(String customerJSON){
	 * logger.info("received content = '{}'", customerJSON); try{ ObjectMapper
	 * mapper = new ObjectMapper(); CustomerEntity customer =
	 * mapper.readValue(customerJSON, CustomerEntity.class); custEntityKafka =
	 * customer; logger.info("Success process brand '{}' with topic '{}'",
	 * customer.getFirstName(), "customers.kafka.put"); } catch (Exception e){
	 * logger.error("An error occurred! '{}'", e.getMessage()); } }
	 * 
	 * public CustomerEntity getCustomerEntityKafka(int id){ return custEntityKafka;
	 * }
	 * 
	 * 
	 * @KafkaListener(topics = "kafka.patch.customer", groupId = "customers") public
	 * void processPatchCustomer(String customerJSON){
	 * logger.info("received content = '{}'", customerJSON); try{ ObjectMapper
	 * mapper = new ObjectMapper(); CustomerEntity customerEntity =
	 * mapper.readValue(customerJSON, CustomerEntity.class); CustomerEntity customer
	 * = customerService.updateCustomer(customerEntity);
	 * logger.info("Success process brand '{}' with topic '{}'",
	 * customer.getFirstName(), "customers.kafka.patch"); } catch (Exception e){
	 * logger.error("An error occurred! '{}'", e.getMessage()); } }
	 */

   
}