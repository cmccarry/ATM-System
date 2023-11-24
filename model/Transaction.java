package model;

import java.sql.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Transaction
{
	// class variables
	private Date date;
	private StringProperty type;
	private StringProperty amount;
	private double balanceAfter;
	
	// constructor
	public Transaction(Date transDate, String type, String amount, double balanceAfter) {
		this.date = transDate;
		this.type = new SimpleStringProperty(type);
		this.amount = new SimpleStringProperty(amount);
		this.balanceAfter = balanceAfter; 
	}
	
	// getters
	public Date getDate() {
		return date;
	}
	public String getType() {
		return type.get();
	}
	public String getAmount() {
		return amount.get();
	}
	public double getBalanceAfter() {
		return balanceAfter;
	}
	
	// setters
	public void setDate(Date date) {
		this.date = date;
	}
	public void setType(String type) {
		this.type = new SimpleStringProperty(type);
	}
	public void setAmount(String amount) {
		this.amount = new SimpleStringProperty(amount);
	}
	public void setBalanceAfter(double balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	// 
	public StringProperty balanceProperty() {
		StringProperty balance = new SimpleStringProperty(String.valueOf(String.format("%.2f", balanceAfter)));
		return balance;
	}
}
