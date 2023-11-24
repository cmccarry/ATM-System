package model;

public class SavingAccount {
	// attributes
	private int savingsAccId;
	private double accBalance;
	private double interestRate;
	
	// constructor
	public SavingAccount(int id, double balance, double rate) {
		savingsAccId = id;
		accBalance = balance;
		interestRate = rate;
	}
	
	// getters
	public int getId() {
		return savingsAccId;
	}
	public double getBalance() {
		return accBalance;
	}
	public double getRate() {
		return interestRate;
	}
	
	// setters
	public void setId(int id) {
		savingsAccId = id;
	}
	public void setBalance(double balance) {
		accBalance = balance;
	}
	public void setRate(double rate) {
		interestRate = rate;
	}
	
	// methods
	public void withdraw(double withdrawAmt) {
		accBalance -= withdrawAmt;
	}
	public void deposit(double depositAmt) {
		accBalance += depositAmt;
	}
	// to transfer money into another savings account
	public void transfer(SavingAccount toAccount, double transferAmt) {
		this.withdraw(transferAmt);
		toAccount.deposit(transferAmt);
	}
	// to transfer money into a checking account
	public void transfer(CheckingAccount toAccount, double transferAmt) {
		this.withdraw(transferAmt);
		toAccount.deposit(transferAmt);
	}
}
