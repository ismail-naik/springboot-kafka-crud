package com.org.tzl.controller;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.tzl.kafka.KafkaConsumer;
import com.org.tzl.kafka.KafkaProducer;

import com.org.tzl.model.CustomMessage;
import com.org.tzl.model.CustomerEntity;


import com.org.tzl.service.CustomerService;
import com.org.tzl.util.ArrayListCustomMessage;
import com.org.tzl.util.CustomErrorType;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerController {
    public static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;
      

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    KafkaConsumer kafkaConsumer;

    @Value("${spring.kafka.consumer.group-id}")
    String kafkaGroupId;

    @Value("${customers.kafka.post}")
    String postCustomerTopic;

    @Value("${customers.kafka.put}")
    String putCustomerTopic;

    @Value("${customers.kafka.patch}")
    String patchCustomerTopic;
    
    @Value("${customers.kafka.delete}")
    String deleteCustomerTopic;


//    @GetMapping(value="")
//    public ResponseEntity<?> getAllByBrandName(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "sort", defaultValue = "brandName,asc") String sort, PagedResourcesAssembler pagedResourcesAssembler, @RequestHeader("User-Agent") String userAgent){
//        logger.info("Fetching all brands");
//        Page<BrandEntity> brand = null;
//        try {
//            brand = brandService.getAllByBrandName(page, size, sort);
//        } catch (Exception e){
//            logger.error("An error occurred! {}", e.getMessage());
//            CustomErrorType.returnResponsEntityError(e.getMessage());
//        }
//        MultiValueMap<String, String> headers = new HttpHeaders();
//        headers.put(HttpHeaders.USER_AGENT, Arrays.asList(userAgent));
//        PagedResources<MultiResource> pagedResources = pagedResourcesAssembler.toResource(brand);
//        return new ResponseEntity<PagedResources>(pagedResources, headers, HttpStatus.OK);
//    }

    @GetMapping(value="/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") int id){
        logger.info("Fetching customer with ID {}", id);
        CustomerEntity customer = null;
        try{
        	customer = kafkaConsumer.getCustomerEntityKafka(id);
        	System.out.println("Customer Details Customer ID and ID" + customer.getCustomerId() + "--" + customer.getId());
            if (customer.getCustomerId() == 0) customer = customerService.findByCustomerId(id);
        } catch (Exception e){
            logger.error("An error occurred! {}", e.getMessage());
            CustomErrorType.returnResponsEntityError(e.getMessage());
        }
        return new ResponseEntity<CustomerEntity>(customer, HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> addCustomer(@RequestBody CustomerEntity customerEntity){
        logger.info(("Process add new customer"));
       // Resources<CustomMessage> res = null;
        List<CustomMessage> customMessageList = null;
        try {
        	
        	System.out.println("inside post with out kafka");
        	customerService.addCustomer(customerEntity);
            
            customMessageList = ArrayListCustomMessage.setMessage("Created new customer", HttpStatus.CREATED);
           // res = new Resources<>(customMessageList);
          //  res.add(linkTo(BrandController.class).withSelfRel());
          //  res.add(linkTo(BrandManufacturerController.class).withRel("brand_manufacturer"));
        } catch (Exception e){
            logger.error("An error occurred! {}", e.getMessage());
            CustomErrorType.returnResponsEntityError(e.getMessage());
        }
        return new ResponseEntity<>(customMessageList, HttpStatus.OK);
    }

    @PostMapping(value = "/postToTopic", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> addCustomerByKafka(@RequestBody CustomerEntity customerEntity){
        logger.info(("Process add new brand"));
       // Resources<CustomMessage> res = null;
        List<CustomMessage> customMessageList = null;
        try {
        	System.out.println("The topics details are :" + postCustomerTopic + "--" + kafkaGroupId);
            kafkaProducer.postCustomer(postCustomerTopic, kafkaGroupId, customerEntity);
            customMessageList = ArrayListCustomMessage.setMessage("Created new customer on topic", HttpStatus.ACCEPTED);
//            res = new Resources<>(customMessageList);
//            res.add(linkTo(BrandController.class).withSelfRel());
//            res.add(linkTo(BrandManufacturerController.class).withRel("brand_manufacturer"));
        } catch (Exception e){
            logger.error("An error occurred! {}", e.getMessage());
            CustomErrorType.returnResponsEntityError(e.getMessage());
        }
        return new ResponseEntity<>(customMessageList, HttpStatus.OK);
    }

    private ResponseEntity<?> putAndPatch(CustomerEntity customerEntity, int id, int mode){
        logger.info("Process '{}' customer", (mode == 0 ? "put" : "patch"));
    
        CustomerEntity customer = null;
        List<CustomMessage> customMessageList = null;
        try {
            
        	customer = customerService.findByCustomerId(id);
            if (customer != null) {
                customMessageList = ArrayListCustomMessage.setMessage((mode == 0 ? "Put" : "Patch" ) + " customer process", HttpStatus.OK);
                customerEntity.setCustomerId(id);
                if (mode != 0) customerEntity.setFirstName(customer.getFirstName());
                kafkaProducer.postCustomer((mode == 0 ? putCustomerTopic : patchCustomerTopic), kafkaGroupId, customerEntity);
            } else {
                customMessageList = ArrayListCustomMessage.setMessage("Brand Id" + id + " Not Found!", HttpStatus.BAD_REQUEST);
              
                return new ResponseEntity<>(customMessageList,HttpStatus.BAD_REQUEST);
            }
         
        } catch (Exception e) {
            logger.error("An error occurred! {}", e.getMessage());
            CustomErrorType.returnResponsEntityError(e.getMessage());
        }
        return new ResponseEntity<>(customMessageList,HttpStatus.OK);
    }
    
    
//    private ResponseEntity<?> deleteCustomer(int id){
//       // logger.info("Process '{}' brand", (mode == 0 ? "put" : "patch"));
//    
//        CustomerEntity customer = null;
//        List<CustomMessage> customMessageList = null;
//        try {
//            
//        	customer = customerService.findByCustomerId(id);
//            if (customer != null) {
//                customMessageList = ArrayListCustomMessage.setMessage((mode == 0 ? "Put" : "Patch" ) + " customer process", HttpStatus.OK);
//                customerEntity.setCustomerId(id);
//                if (mode != 0) customerEntity.setFirstName(customer.getFirstName());
//                kafkaProducer.postCustomer((mode == 0 ? putCustomerTopic : patchCustomerTopic), kafkaGroupId, customerEntity);
//            } else {
//                customMessageList = ArrayListCustomMessage.setMessage("Brand Id" + id + " Not Found!", HttpStatus.BAD_REQUEST);
//              
//                return new ResponseEntity<>(customMessageList,HttpStatus.BAD_REQUEST);
//            }
//         
//        } catch (Exception e) {
//            logger.error("An error occurred! {}", e.getMessage());
//            CustomErrorType.returnResponsEntityError(e.getMessage());
//        }
//        return new ResponseEntity<>(customMessageList,HttpStatus.OK);
//    }
    
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> putBrand(@PathVariable("id") int id, @RequestBody CustomerEntity brandEntity){
        return putAndPatch(brandEntity, id, 0);
    }

    @PatchMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateCustomer(@PathVariable("id") int id, @RequestBody CustomerEntity customerEntity){
        return putAndPatch(customerEntity, id, 1);
    }
    
    
    
//    @DeleteMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<?> deleteCustomer(@PathVariable("id") int id, @RequestBody CustomerEntity customerEntity){
//        return delete(id);
//    }
}
