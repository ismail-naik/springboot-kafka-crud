package com.org.tzl.model;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "customer")
public class CustomerEntity {
	
	private String apiKey;
    private int customerId;    
    private String firstName;
    private String lastName;    
    private String middleName;
    private String mobileNumber;
    private String gender;
    private int ssno;
    
    
  
   
	
    @Basic
    @Column(name = "apikey")
	  public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Id
    @Basic
    @Column(name = "customerId")
	public int getCustomerId() {
		return customerId;
	}	
	
    public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
    

    @Basic
    @Column(name = "firstName")
	public String getFirstName() {
		return firstName;
	}	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	 @Basic
	 @Column(name = "lastName")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	 @Basic
	 @Column(name = "middleName")
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	@Basic
	 @Column(name = "mobileNumber")
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	@Basic
	 @Column(name = "gender")
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Basic
	 @Column(name = "ssno")
	public int getSsno() {
		return ssno;
	}
	public void setSsno(int ssno) {
		this.ssno = ssno;
	}
	
	public CustomerEntity() {
		super();
	}
	public CustomerEntity(String apiKey, int customerId, String firstName, String lastName, String middleName,
			String mobileNumber, String gender, int ssno) {
		super();
		this.apiKey = apiKey;
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.mobileNumber = mobileNumber;
		this.gender = gender;
		this.ssno = ssno;
	}
	@Override
	public String toString() {
		return "CustomerEntity [apiKEY=" + apiKey + ", customerId=" + customerId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", middleName=" + middleName + ", mobileNumber=" + mobileNumber
				+ ", gender=" + gender + ", ssno=" + ssno + "]";
	} 
    
  
    

   
}
