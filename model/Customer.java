package model;
import java.sql.Date;

public class Customer
{
	private int customerNum;
	private String fullName;
	private Date birthDate;
	private String address;
	private String phone;
	private String email;
	
	public Customer(int customerNum, String fullName, Date birthDate,
			String address, String phone, String email) {
		this.customerNum = customerNum;
		this.fullName = fullName;
		this.birthDate = birthDate; 
		this.address = address;
		this.phone = phone;
		this.email = email;
	}
	
	//getters
	public int getCustomerNum() {
		return customerNum;
	}
	public String getFullName() {
		return fullName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public String getAddress() {
		return address;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	
	//setters
	public void setCustomerNum(int customerNum) {
		this.customerNum = customerNum;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}